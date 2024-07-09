package com.docapis.core.codegenerator.ios.builder;


import com.docapis.core.codegenerator.ICodeBuilder;

/**
 * 模型构造
 * <p>
 * licence Apache 2.0, from japidoc
 **/
public class ModelBuilder implements ICodeBuilder {

    private String modelTemplate;
    private String objectName;
    private String properties;


    public ModelBuilder(String modelTemplate, String objectName, String properties) {
        super();
        this.modelTemplate = modelTemplate;
        this.objectName = objectName;
        this.properties = properties;
    }

    @Override
    public String build() {
        modelTemplate = modelTemplate.replace("${CLASS_NAME}", objectName);
        modelTemplate = modelTemplate.replace("${FIELDS}", properties);
        return modelTemplate;
    }
}
