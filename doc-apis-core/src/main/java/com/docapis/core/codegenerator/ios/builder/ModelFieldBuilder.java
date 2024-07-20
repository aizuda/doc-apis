package com.docapis.core.codegenerator.ios.builder;


import com.docapis.core.codegenerator.ICodeBuilder;
import com.docapis.core.codegenerator.model.FieldModel;

/**
 * 模型字段构造
 * <p>
 * licence Apache 2.0, from japidoc
 **/
public class ModelFieldBuilder implements ICodeBuilder {

    private String modelFieldTemplate;
    private FieldModel entryFieldModel;

    public ModelFieldBuilder(String modelFieldTemplate, FieldModel entryFieldModel) {
        super();
        this.modelFieldTemplate = modelFieldTemplate;
        this.entryFieldModel = entryFieldModel;
    }

    @Override
    public String build() {
        modelFieldTemplate = modelFieldTemplate.replace("${FIELD_TYPE}", entryFieldModel.getIFieldType());
        modelFieldTemplate = modelFieldTemplate.replace("${FIELD_NAME}", entryFieldModel.getFieldName());
        modelFieldTemplate = modelFieldTemplate.replace("${COMMENT}", entryFieldModel.getComment());
        modelFieldTemplate = modelFieldTemplate.replace("${ASSIGN}", entryFieldModel.getAssign());
        return modelFieldTemplate + "\n";
    }
}
