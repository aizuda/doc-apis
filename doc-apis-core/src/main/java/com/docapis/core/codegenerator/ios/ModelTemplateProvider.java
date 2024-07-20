package com.docapis.core.codegenerator.ios;


import com.docapis.core.codegenerator.TemplateProvider;

import java.io.IOException;

/**
 * 模型目标构造
 * <p>
 * licence Apache 2.0, from japidoc
 **/
public class ModelTemplateProvider {

    public String provideTemplateForName(String templateName) throws IOException {
        return TemplateProvider.provideTemplateForName(templateName);
    }

}
