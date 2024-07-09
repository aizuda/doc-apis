package com.docapis.core.codegenerator;


import com.docapis.core.DocContext;
import com.docapis.core.Utils;
import com.docapis.core.parser.ClassNode;
import com.docapis.core.parser.FieldNode;
import com.docapis.core.parser.ResponseNode;

import java.io.File;
import java.io.IOException;

/**
 * code generator
 * <p>
 * licence Apache 2.0, from japidoc
 **/
public abstract class CodeGenerator {

    private ResponseNode responseNode;
    private File codePath;
    private String codeRelativePath;

    public CodeGenerator(ResponseNode responseNode) {
        this.responseNode = responseNode;
        this.codeRelativePath = getRelativeCodeDir();
        this.codePath = new File(DocContext.getDocPath(), codeRelativePath);
        if (!this.codePath.exists()) {
            this.codePath.mkdirs();
        }
    }

    public String generateCode() throws IOException {
        if (responseNode.getChildNodes() == null || responseNode.getChildNodes().isEmpty()) {
            return "";
        }
        StringBuilder codeBodyBuilder = new StringBuilder();
        generateCodeForBuilder(responseNode, codeBodyBuilder);
        final String sCodeTemplate = getCodeTemplate();
        CodeFileBuilder codeBuilder = new CodeFileBuilder(responseNode.getClassName(), codeBodyBuilder.toString(), sCodeTemplate);
        final String javaFileName = String.format("%s_%s_%s_%s.html",
                responseNode.getRequestNode().getControllerNode().getPackageName().replace(".", "_"),
                responseNode.getRequestNode().getControllerNode().getClassName(),
                responseNode.getRequestNode().getMethodName(), responseNode.getClassName());
        Utils.writeToDisk(new File(codePath, javaFileName), codeBuilder.build());
        return String.format("%s/%s", codeRelativePath, javaFileName);
    }

    private void generateCodeForBuilder(ClassNode rootNode, StringBuilder codeBodyBuilder) throws IOException {
        codeBodyBuilder.append(generateNodeCode(rootNode));
        codeBodyBuilder.append('\n');
        for (FieldNode recordNode : rootNode.getChildNodes()) {
            if (recordNode.getChildNode() != null) {
                generateCodeForBuilder(recordNode.getChildNode(), codeBodyBuilder);
            }
        }
    }

    public abstract String generateNodeCode(ClassNode classNode) throws IOException;

    public abstract String getRelativeCodeDir();

    public abstract String getCodeTemplate();
}
