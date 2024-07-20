package com.docapis.core.parser;

import com.docapis.core.*;
import com.docapis.core.constant.ChangeFlag;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.javadoc.JavadocBlockTag;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * abstract doc generator
 * <p>
 * licence Apache 2.0,AGPL-3.0, from japidoc and doc-apis originated
 **/
public abstract class AbsControllerParser {

    private CompilationUnit compilationUnit;
    private ControllerNode controllerNode;
    private File javaFile;

    public ControllerNode parse(File javaFile) {

        this.javaFile = javaFile;
        this.compilationUnit = ParseUtils.compilationUnit(javaFile);
        this.controllerNode = new ControllerNode();

        String controllerName = Utils.getJavaFileName(javaFile);
        controllerNode.setClassName(controllerName);
        boolean isInterface = false;
        TypeDeclaration<?> typeDeclaration = Optional.ofNullable(compilationUnit)
                .map(unit -> {
                    if (unit.getTypes() != null && unit.getTypes().size() > 0) {
                        TypeDeclaration<?> type = compilationUnit.getType(0);
                        return type;
                    } else {
                        return null;
                    }
                }).orElse(null);

        if (typeDeclaration != null && typeDeclaration instanceof ClassOrInterfaceDeclaration) {
            isInterface = ((ClassOrInterfaceDeclaration) typeDeclaration).isInterface();

        }

        if (isInterface) {
            compilationUnit.getInterfaceByName(controllerName)
                    .ifPresent(c -> {
                        beforeHandleController(controllerNode, c);
                        parseClassDoc(c);
                        parseMethodDocs(c);
                        afterHandleController(controllerNode, c);
                    });
        } else {
            Optional.ofNullable(compilationUnit).flatMap(i -> i.getClassByName(controllerName))
                    .ifPresent(c -> {
                        beforeHandleController(controllerNode, c);
                        parseClassDoc(c);
                        parseMethodDocs(c);
                        afterHandleController(controllerNode, c);
                    });
        }
        return controllerNode;
    }

    File getControllerFile() {
        return javaFile;
    }

    ControllerNode getControllerNode() {
        return controllerNode;
    }

    private void parseClassDoc(ClassOrInterfaceDeclaration c) {

        c.getParentNode().get().findFirst(PackageDeclaration.class).ifPresent(pd -> {
            controllerNode.setPackageName(pd.getNameAsString());
        });

        boolean generateDocs = c.getAnnotationByName(DocApi.class.getSimpleName()).isPresent();
        controllerNode.setGenerateDocs(generateDocs);

        c.getJavadoc().ifPresent(d -> {
            String description = d.getDescription().toText();
            controllerNode.setDescription(Utils.isNotEmpty(description) ? description : c.getNameAsString());
            List<JavadocBlockTag> blockTags = d.getBlockTags();
            if (blockTags != null) {
                for (JavadocBlockTag blockTag : blockTags) {
                    if ("author".equalsIgnoreCase(blockTag.getTagName())) {
                        controllerNode.setAuthor(blockTag.getContent().toText());
                    }
                    if ("description".equalsIgnoreCase(blockTag.getTagName())) {
                        controllerNode.setDescription(blockTag.getContent().toText());
                    }
                }
            }
        });

        if (controllerNode.getDescription() == null) {
            controllerNode.setDescription(c.getNameAsString());
        }
    }

    private void parseMethodDocs(ClassOrInterfaceDeclaration c) {
        c.findAll(MethodDeclaration.class).stream()
                .forEach(m -> {
                    if (!c.isInterface() && !m.getModifiers().contains(Modifier.publicModifier())) {
                        // not interface need filter public method
                        return;
                    }

                    boolean existsDocApi = m.getAnnotationByName(DocApi.class.getSimpleName()).isPresent();
                    if (!existsDocApi && !controllerNode.getGenerateDocs() && !DocContext.getDocsConfig().getAutoGenerate()) {
                        return;
                    }

                    boolean shouldIgnoreMethod = shouldIgnoreMethod(m);
                    if (shouldIgnoreMethod) {
                        return;
                    }

                    RequestNode requestNode = new RequestNode();
                    requestNode.setControllerNode(controllerNode);
                    requestNode.setAuthor(controllerNode.getAuthor());
                    requestNode.setMethodName(m.getNameAsString());
                    requestNode.setUrl("");
                    requestNode.setDescription(requestNode.getMethodName());

                    m.getAnnotationByClass(Deprecated.class).ifPresent(f -> {
                        requestNode.setDeprecated(true);
                    });

                    m.getJavadoc().ifPresent(d -> {
                        String description = d.getDescription().toText();
                        requestNode.setDescription(description);

                        List<JavadocBlockTag> blockTagList = d.getBlockTags();
                        for (JavadocBlockTag blockTag : blockTagList) {
                            if (blockTag.getTagName().equalsIgnoreCase("param")) {
                                ParamNode paramNode = new ParamNode();
                                if (blockTag.getName().isPresent()) {
                                    paramNode.setName(blockTag.getName().get());
                                }
                                paramNode.setDescription(blockTag.getContent().toText());
                                requestNode.addParamNode(paramNode);
                            } else if (blockTag.getTagName().equalsIgnoreCase("author")) {
                                requestNode.setAuthor(blockTag.getContent().toText());
                            } else if (blockTag.getTagName().equalsIgnoreCase("description")) {
                                requestNode.setSupplement(blockTag.getContent().toText());
                            }
                        }
                    });

                    m.getParameters().forEach(p -> {
                        String paraName = p.getName().asString();
                        ParamNode paramNode = requestNode.getParamNodeByName(paraName);

                        if (paramNode != null && ParseUtils.isExcludeParam(p)) {
                            requestNode.getParamNodes().remove(paramNode);
                            return;
                        }

                        if (paramNode != null) {
                            Type pType = p.getType();
                            boolean isList = false;
                            if (pType instanceof ArrayType) {
                                isList = true;
                                pType = ((ArrayType) pType).getComponentType();
                            } else if (ParseUtils.isCollectionType(pType.asString())) {
                                List<ClassOrInterfaceType> collectionTypes = pType.getChildNodesByType(ClassOrInterfaceType.class);
                                isList = true;
                                if (!collectionTypes.isEmpty()) {
                                    pType = collectionTypes.get(0);
                                } else {
                                    paramNode.setType("Object[]");
                                }
                            } else {
                                pType = p.getType();
                            }
                            if (paramNode.getType() == null) {
                                if (ParseUtils.isEnum(getControllerFile(), pType.asString())) {
                                    paramNode.setType(isList ? "enum[]" : "enum");
                                } else {
                                    final String pUnifyType = ParseUtils.unifyType(pType.asString());
                                    paramNode.setType(isList ? pUnifyType + "[]" : pUnifyType);
                                }
                            }
                        }
                    });

                    com.github.javaparser.ast.type.Type resultClassType = null;
                    String stringResult = null;
                    if (existsDocApi) {
                        AnnotationExpr an = m.getAnnotationByName(DocApi.class.getSimpleName()).get();
                        if (an instanceof SingleMemberAnnotationExpr) {
                            resultClassType = ((ClassExpr) ((SingleMemberAnnotationExpr) an).getMemberValue()).getType();
                        } else if (an instanceof NormalAnnotationExpr) {
                            for (MemberValuePair pair : ((NormalAnnotationExpr) an).getPairs()) {
                                final String pairName = pair.getNameAsString();
                                if ("result".equals(pairName) || "value".equals(pairName)) {
                                    resultClassType = ((ClassExpr) pair.getValue()).getType();
                                } else if (pairName.equals("url")) {
                                    requestNode.setUrl(((StringLiteralExpr) pair.getValue()).getValue());
                                } else if (pairName.equals("method")) {
                                    requestNode.addMethod(((StringLiteralExpr) pair.getValue()).getValue());
                                } else if ("stringResult".equals(pairName)) {
                                    stringResult = ((StringLiteralExpr) pair.getValue()).getValue();
                                }
                            }
                        }
                    }

                    afterHandleMethod(requestNode, m);

                    if (resultClassType == null) {
                        if (m.getType() == null) {
                            return;
                        }
                        resultClassType = m.getType();
                    }

                    ResponseNode responseNode = new ResponseNode();
                    responseNode.setRequestNode(requestNode);
                    if (stringResult != null) {
                        responseNode.setStringResult(stringResult);
                    } else {
                        handleResponseNode(responseNode, resultClassType.getElementType());
                    }
                    requestNode.setResponseNode(responseNode);
                    setRequestNodeChangeFlag(requestNode);
                    controllerNode.addRequestNode(requestNode);
                });
    }

    protected void beforeHandleController(ControllerNode controllerNode, ClassOrInterfaceDeclaration clazz) {
    }

    protected void afterHandleController(ControllerNode controllerNode, ClassOrInterfaceDeclaration clazz) {
    }


    protected boolean shouldIgnoreMethod(MethodDeclaration m) {
        // fixme 此处javaparser有bug,当指定注解不存在时,其并不能正常工作,会报错详情可自行断点查看,已通过下面代码临时解决
//        return m.getAnnotationByName(Ignore.class.getSimpleName()).isPresent();
        boolean ignore = false;
        try {
            ignore = m.getAnnotationByClass(DocIgnore.class).isPresent();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return ignore;
    }

    protected void handleResponseNode(ResponseNode responseNode, com.github.javaparser.ast.type.Type resultType) {
        parseClassNodeByType(responseNode, resultType);
    }

    void parseClassNodeByType(ClassNode classNode, com.github.javaparser.ast.type.Type classType) {
        // maybe void
        if (classType instanceof ClassOrInterfaceType) {
            // 解析方法返回类的泛型信息
            ((ClassOrInterfaceType) classType).getTypeArguments().ifPresent(typeList -> typeList.forEach(argType -> {
                GenericNode rootGenericNode = new GenericNode();
                rootGenericNode.setFromJavaFile(javaFile);
                rootGenericNode.setClassType(argType);
                classNode.addGenericNode(rootGenericNode);
            }));
            ParseUtils.parseClassNodeByType(javaFile, classNode, classType);
        }
    }

    protected void afterHandleMethod(RequestNode requestNode, MethodDeclaration md) {
    }


    // 设置接口的类型（新/修改/一样）
    private void setRequestNodeChangeFlag(RequestNode requestNode) {
        List<ControllerNode> lastControllerNodeList = DocContext.getLastVersionControllerNodes();
        if (lastControllerNodeList == null || lastControllerNodeList.isEmpty()) {
            return;
        }

        for (ControllerNode lastControllerNode : lastControllerNodeList) {
            for (RequestNode lastRequestNode : lastControllerNode.getRequestNodes()) {
                if (lastRequestNode.getUrl().equals(requestNode.getUrl())) {
                    requestNode.setLastRequestNode(lastRequestNode);
                    requestNode.setChangeFlag(isSameRequestNodes(requestNode, lastRequestNode) ? ChangeFlag.SAME : ChangeFlag.MODIFY);
                    return;
                }
            }
        }

        requestNode.setChangeFlag(ChangeFlag.NEW);
    }

    private boolean isSameRequestNodes(RequestNode requestNode, RequestNode lastRequestNode) {

        for (String lastMethod : lastRequestNode.getMethod()) {
            if (!requestNode.getMethod().contains(lastMethod)) {
                return false;
            }
        }

        return Utils.toJson(requestNode.getParamNodes()).equals(Utils.toJson(lastRequestNode.getParamNodes()))
                && Utils.toJson(requestNode.getHeader()).equals(Utils.toJson(lastRequestNode.getHeader()))
                && requestNode.getResponseNode().toJsonApi().equals(lastRequestNode.getResponseNode().toJsonApi());
    }
}
