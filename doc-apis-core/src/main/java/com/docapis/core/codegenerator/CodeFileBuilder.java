package com.docapis.core.codegenerator;

/**
 * code file builder
 * <p>
 * licence Apache 2.0, from japidoc
 **/
public class CodeFileBuilder implements ICodeBuilder {

    private String fileName;
    private String codeBody;
    private String codeTemplate;

    public CodeFileBuilder(String fileName, String codeBody, String codeTemplate) {
        super();
        this.fileName = fileName;
        this.codeBody = codeBody;
        this.codeTemplate = codeTemplate;
    }

    public String build() {
        codeTemplate = codeTemplate.replace("${FILE_NAME}", fileName);
        codeTemplate = codeTemplate.replace("${MODEL_LIST}", codeBody);
        return codeTemplate;
    }
}
