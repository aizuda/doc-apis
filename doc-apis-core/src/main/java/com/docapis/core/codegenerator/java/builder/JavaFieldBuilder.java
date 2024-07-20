package com.docapis.core.codegenerator.java.builder;


import com.docapis.core.codegenerator.ICodeBuilder;
import com.docapis.core.codegenerator.model.FieldModel;

/**
 * java field code builder
 * <p>
 * licence Apache 2.0, from japidoc
 **/
public class JavaFieldBuilder implements ICodeBuilder {

    private String fieldTemplate;
    private FieldModel entryFieldModel;

    public JavaFieldBuilder(String fieldTemplate, FieldModel entryFieldModel) {
        this.fieldTemplate = fieldTemplate;
        this.entryFieldModel = entryFieldModel;
    }

    @Override
    public String build() {
        fieldTemplate = fieldTemplate.replace("${FIELD_TYPE}", entryFieldModel.getFieldType());
        fieldTemplate = fieldTemplate.replace("${FIELD_NAME}", entryFieldModel.getFieldName());
        fieldTemplate = fieldTemplate.replace("${COMMENT}", entryFieldModel.getComment());
        return fieldTemplate + "\n";
    }
}
