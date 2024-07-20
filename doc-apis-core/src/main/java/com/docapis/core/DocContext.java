package com.docapis.core;

import com.docapis.core.exception.ConfigException;
import com.docapis.core.parser.*;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.io.File;
import java.util.*;

import static com.docapis.core.constant.CoreConstants.COMMA_SIGN;

/**
 * doc context
 * <p>
 * licence Apache 2.0,AGPL-3.0, from japidoc and doc-apis originated
 **/
public class DocContext {

    private static String projectPath;
    private static String docPath;
    @Deprecated
    private static List<String> javaSrcPaths = new ArrayList<>();
    private static AbsControllerParser controllerParser;
    private static List<File> controllerFiles;
    private static IResponseWrapper responseWrapper;
    private static DocsConfig config;
    private static I18n i18n;

    private static String currentApiVersion;
    private static List<String> apiVersionList = new ArrayList<>();
    private static List<ControllerNode> lastVersionControllerNodes;
    private static List<ControllerNode> controllerNodeList;

    public static void init(DocsConfig config) {
        if (config.projectPath == null || !new File(config.projectPath).exists()) {
            throw new ConfigException(String.format("projectDir doesn't exists. %s", projectPath));
        }

        if (config.getApiVersion() == null) {
            throw new ConfigException("api version cannot be null");
        }

        if (config.getProjectName() == null) {
            config.setProjectName("doc-apis");
        }

        DocContext.config = config;
        i18n = new I18n(config.getLocale());
        DocContext.currentApiVersion = config.getApiVersion();
        setProjectPath(config.projectPath);
        setDocPath(config);
        initApiVersions();

        File logFile = getLogFile();
        if (logFile.exists()) {
            logFile.delete();
        }

        if (config.getJavaSrcPaths().isEmpty()) {
            findOutJavaSrcPathsByProjectPaths();
        } else {
            javaSrcPaths.addAll(config.getJavaSrcPaths());
        }

        LogUtils.info("find java src paths:  %s", javaSrcPaths);

        ProjectType projectType = findOutProjectType();
        findOutControllers(projectType);
        initLastVersionControllerNodes();
    }

    private static void initLastVersionControllerNodes() {
        File docDir = new File(docPath).getParentFile();
        File[] childDirs = docDir.listFiles();
        if (childDirs != null && childDirs.length != 0) {
            File lastVerDocDir = null;
            for (File childDir : childDirs) {
                if (childDir.isDirectory() && !currentApiVersion.equals(childDir.getName())
                        && (lastVerDocDir == null || childDir.lastModified() > lastVerDocDir.lastModified())) {
                    lastVerDocDir = childDir;
                }
            }
            if (lastVerDocDir != null) {
                lastVersionControllerNodes = CacheUtils.getControllerNodes(lastVerDocDir.getName());
            }
        }
    }

    private static void findOutJavaSrcPathsByProjectPaths() {
        if (projectPath != null) {
            String[] split = projectPath.split(COMMA_SIGN);
            Arrays.stream(split).forEach(DocContext::findOutJavaSrcPaths);
        }
    }

    private static void findOutJavaSrcPaths(String path) {
        //try to find javaSrcPaths
        File projectDir = new File(path);

        //module name maybe:
        //include 'auth:auth-redis'
        List<String> moduleNames = Utils.getModuleNames(projectDir);
        if (!moduleNames.isEmpty()) {
            for (String moduleName : moduleNames) {
                final String moduleRelativePath = moduleName.replace(":", "/");
                String javaSrcPath = findModuleSrcPath(new File(projectDir, moduleRelativePath));
                Optional.ofNullable(javaSrcPath).ifPresent(javaSrcPaths::add);
            }
        }

        // is it a simple java project?
        if (javaSrcPaths.isEmpty()) {
            String javaSrcPath = findModuleSrcPath(projectDir);
            javaSrcPaths.add(javaSrcPath);
        }
    }

    private static void initApiVersions() {
        File docDir = new File(docPath).getParentFile();
        String[] diffVersionApiDirs = docDir.list((dir, name) -> dir.isDirectory() && !name.startsWith("."));
        if (diffVersionApiDirs != null) {
            Collections.addAll(DocContext.apiVersionList, diffVersionApiDirs);
        }
    }

    private static ProjectType findOutProjectType() {

        //which mvc framework
        ProjectType projectType = null;

        if (config.isSpringMvcProject()) {
            projectType = ProjectType.SPRING;
        } else if (config.isJfinalProject()) {
            projectType = ProjectType.JFINAL;
        } else if (config.isPlayProject()) {
            projectType = ProjectType.PLAY;
        } else if (config.isGeneric()) {
            projectType = ProjectType.GENERIC;
        }

        if (projectType == null) {
            LogUtils.info("project type not set, try to figure out...");
            for (String javaSrcPath : javaSrcPaths) {
                File javaSrcDir = new File(javaSrcPath);
                if (Utils.isSpringFramework(javaSrcDir)) {
                    projectType = ProjectType.SPRING;
                } else if (Utils.isPlayFramework(new File(getProjectPath()))) {
                    projectType = ProjectType.PLAY;
                } else if (Utils.isJFinalFramework(javaSrcDir)) {
                    projectType = ProjectType.JFINAL;
                }

                if (projectType != null) {
                    return projectType;
                }
            }
        }

        projectType = projectType != null ? projectType : ProjectType.GENERIC;

        LogUtils.info("found it a %s project, tell us if we are wrong.", projectType);

        return projectType;
    }

    private static void findOutControllers(ProjectType projectType) {
        controllerFiles = new ArrayList<>();
        Set<String> controllerFileNames;

        for (String javaSrcPath : getJavaSrcPaths()) {
            LogUtils.info("start find controllers in path : %s", javaSrcPath);
            File javaSrcDir = new File(javaSrcPath);
            List<File> result = new ArrayList<>();
            switch (projectType) {
                case PLAY:
                    controllerParser = new PlayControllerParser();
                    controllerFileNames = new LinkedHashSet<>();
                    List<PlayRoutesParser.RouteNode> routeNodeList = PlayRoutesParser.INSTANCE.getRouteNodeList();

                    for (PlayRoutesParser.RouteNode node : routeNodeList) {
                        controllerFileNames.add(node.controllerFile);
                    }

                    for (String controllerFileName : controllerFileNames) {
                        controllerFiles.add(new File(controllerFileName));
                    }

                    break;
                case JFINAL:
                    controllerParser = new JFinalControllerParser();
                    controllerFileNames = new LinkedHashSet<>();
                    List<JFinalRoutesParser.RouteNode> jFinalRouteNodeList = JFinalRoutesParser.INSTANCE.getRouteNodeList();

                    for (JFinalRoutesParser.RouteNode node : jFinalRouteNodeList) {
                        controllerFileNames.add(node.controllerFile);
                    }

                    for (String controllerFileName : controllerFileNames) {
                        controllerFiles.add(new File(controllerFileName));
                    }
                    break;
                case SPRING:
                    controllerParser = new SpringControllerParser();
                    Utils.wideSearchFile(javaSrcDir, (f, name) -> f.getName().endsWith(".java") && ParseUtils.compilationUnit(f)
                                    .getChildNodesByType(ClassOrInterfaceDeclaration.class)
                                    .stream()
                                    .anyMatch(cd -> (cd.getAnnotationByName("Controller").isPresent()
                                            || cd.getAnnotationByName("RestController").isPresent())
                                            || cd.getAnnotationByName("RequestMapping").isPresent()
                                            || cd.getName().asString().endsWith("Controller")
                                            || cd.getName().asString().endsWith("Service")
                                            && !cd.getAnnotationByName(DocIgnore.class.getSimpleName()).isPresent())
                            , result, false);
                    controllerFiles.addAll(result);
                    break;
                default:
                    controllerParser = new GenericControllerParser();
                    Utils.wideSearchFile(javaSrcDir, (f, name) -> f.getName().endsWith(".java") && ParseUtils.compilationUnit(f)
                            .getChildNodesByType(ClassOrInterfaceDeclaration.class)
                            .stream()
                            .anyMatch(cd ->
                                    cd.getChildNodesByType(MethodDeclaration.class)
                                            .stream()
                                            .anyMatch(md -> md.getAnnotationByName(DocApi.class.getSimpleName()).isPresent())
                            ), result, false);
                    controllerFiles.addAll(result);
                    break;
            }
            for (File controllerFile : result) {
                LogUtils.info("find controller file : %s", controllerFile.getName());
            }
        }
    }

    private static String findModuleSrcPath(File moduleDir) {

        List<File> result = new ArrayList<>();
        Utils.wideSearchFile(moduleDir, (file, name) -> {
            if (name.endsWith(".java") && file.getAbsolutePath().contains("src")) {
                Optional<PackageDeclaration> opPackageDeclaration = ParseUtils.compilationUnit(file).getPackageDeclaration();
                if (opPackageDeclaration.isPresent()) {
                    String packageName = opPackageDeclaration.get().getNameAsString();
                    if (Utils.hasDirInFile(file, moduleDir, "test") && !packageName.contains("test")) {
                        return false;
                    } else {
                        return true;
                    }
                }
                return !Utils.hasDirInFile(file, moduleDir, "test");
            }
            return false;
        }, result, true);

        if (result.isEmpty()) {
            LogUtils.warn("cannot find any java file, skip this module : " + moduleDir.getName());
            return null;
        }

        File oneJavaFile = result.get(0);
        Optional<PackageDeclaration> opPackageDeclaration = ParseUtils.compilationUnit(oneJavaFile).getPackageDeclaration();
        String parentPath = oneJavaFile.getParentFile().getAbsolutePath();
        if (opPackageDeclaration.isPresent()) {
            return parentPath.substring(0, parentPath.length() - opPackageDeclaration.get().getNameAsString().length());
        } else {
            return parentPath + "/";
        }
    }

    public static File getLogFile() {
        return new File(DocContext.getDocPath(), "docapis.log");
    }

    public static String getProjectPath() {
        return projectPath;
    }

    private static void setProjectPath(String projectPath) {
        if (projectPath != null) {
            DocContext.projectPath = new File(projectPath).getAbsolutePath() + "/";
        }
    }

    public static String getDocPath() {
        return docPath;
    }

    private static void setDocPath(DocsConfig config) {
        if (config.docsPath == null || config.docsPath.isEmpty()) {
            config.docsPath = projectPath + "docapis";
        }

        File docDir = new File(config.docsPath, config.apiVersion);
        if (!docDir.exists()) {
            docDir.mkdirs();
        }
        DocContext.docPath = docDir.getAbsolutePath();
    }

    public static List<String> getJavaSrcPaths() {
        return javaSrcPaths;
    }

    public static File[] getControllerFiles() {
        return controllerFiles.toArray(new File[controllerFiles.size()]);
    }

    public static AbsControllerParser controllerParser() {
        return controllerParser;
    }

    public static IResponseWrapper getResponseWrapper() {
        if (responseWrapper == null) {
            responseWrapper = new IResponseWrapper() {
                @Override
                public Map<String, Object> wrapResponse(ResponseNode responseNode) {
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("code", 0);
                    resultMap.put("data", responseNode);
                    resultMap.put("msg", "success");
                    return resultMap;
                }
            };
        }
        return responseWrapper;
    }

    public static List<ControllerNode> getControllerNodeList() {
        return controllerNodeList;
    }

    static void setControllerNodeList(List<ControllerNode> controllerNodeList) {
        DocContext.controllerNodeList = controllerNodeList;
    }

    public static DocsConfig getDocsConfig() {
        return DocContext.config;
    }

    public static String getCurrentApiVersion() {
        return currentApiVersion;
    }

    public static List<String> getApiVersionList() {
        return apiVersionList;
    }

    public static List<ControllerNode> getLastVersionControllerNodes() {
        return lastVersionControllerNodes;
    }

    public static I18n getI18n() {
        return i18n;
    }

    static void setResponseWrapper(IResponseWrapper responseWrapper) {
        DocContext.responseWrapper = responseWrapper;
    }
}
