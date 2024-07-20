package com.docapis.core.codegenerator.java;


import com.docapis.core.codegenerator.TemplateProvider;

import java.io.IOException;

/**
 * java template provider
 * <p>
 * licence Apache 2.0, from japidoc
 **/
public class JavaTemplateProvider {

    public String provideTemplateForName(String templateName) throws IOException {
        return TemplateProvider.provideTemplateForName(templateName);
    }

}
