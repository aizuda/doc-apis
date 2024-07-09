package com.docapis.core.codegenerator.ios;


import com.docapis.core.codegenerator.CodeGenerator;
import com.docapis.core.codegenerator.IFieldProvider;
import com.docapis.core.codegenerator.TemplateProvider;
import com.docapis.core.codegenerator.ios.builder.ModelBuilder;
import com.docapis.core.codegenerator.ios.builder.ModelFieldBuilder;
import com.docapis.core.codegenerator.model.FieldModel;
import com.docapis.core.codegenerator.provider.ProviderFactory;
import com.docapis.core.parser.ClassNode;
import com.docapis.core.parser.ResponseNode;

import java.io.IOException;
import java.util.List;

/**
 * 模型构造
 * <p>
 * licence Apache 2.0, from japidoc
 **/
public class ModelCodeGenerator extends CodeGenerator {

    private static final String FILE_FIELD_TEMPLATE = "IOS_Model_Field.tpl";
    private static final String FILE_MODEL_TEMPLATE = "IOS_Model.tpl";
    private static final String FILE_CODE_TEMPLATE = "Code_File.html.tpl";
    private static final String IOS_CODE_DIR = "iosCodes";

    private static String sFieldTemplate;
    private static String sModelTemplate;
    private static String sCodeTemplate;

    static {
        ModelTemplateProvider resourceTemplateProvider = new ModelTemplateProvider();
        try {
            sFieldTemplate = resourceTemplateProvider.provideTemplateForName(FILE_FIELD_TEMPLATE);
            sModelTemplate = resourceTemplateProvider.provideTemplateForName(FILE_MODEL_TEMPLATE);
            sCodeTemplate = TemplateProvider.provideTemplateForName(FILE_CODE_TEMPLATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ModelCodeGenerator(ResponseNode responseNode) {
        super(responseNode);
    }

    @Override
    public String generateNodeCode(ClassNode respNode) throws IOException {
        String className = respNode.getClassName();
        IFieldProvider entryProvider = ProviderFactory.createProvider();
        List<FieldModel> entryFields = entryProvider.provideFields(respNode);
        if (entryFields == null || entryFields.isEmpty()) {
            return "";
        }
        StringBuilder fieldStrings = new StringBuilder();
        for (FieldModel entryFieldModel : entryFields) {
            ModelFieldBuilder fieldBuilder = new ModelFieldBuilder(sFieldTemplate, entryFieldModel);
            fieldStrings.append(fieldBuilder.build());
        }
        ModelBuilder modelBuilder = new ModelBuilder(sModelTemplate, className, fieldStrings.toString());
        return modelBuilder.build();
    }

    @Override
    public String getRelativeCodeDir() {
        return IOS_CODE_DIR;
    }

    @Override
    public String getCodeTemplate() {
        return sCodeTemplate;
    }
}
