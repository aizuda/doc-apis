package com.docapis.core.codegenerator.java.builder;


import com.docapis.core.codegenerator.ICodeBuilder;

/**
 * java class builder
 * <p>
 * licence Apache 2.0, from japidoc
 **/
public class JavaClassBuilder implements ICodeBuilder {

    private String className;
    private String mFieldCode;
    private String mMethodCode;
    private String entryClassTemplate;

    public JavaClassBuilder(String entryClassTemplate, String className, String mFieldCode, String mMethodCode) {
        this.className = className;
        this.mFieldCode = mFieldCode;
        this.mMethodCode = mMethodCode;
        this.entryClassTemplate = entryClassTemplate;
    }

    @Override
    public String build() {
        entryClassTemplate = entryClassTemplate.replace("${CLASS_NAME}", className);
        entryClassTemplate = entryClassTemplate.replace("${FIELDS}", mFieldCode);
        entryClassTemplate = entryClassTemplate.replace("${METHODS}", mMethodCode);
        return entryClassTemplate;
    }
}
