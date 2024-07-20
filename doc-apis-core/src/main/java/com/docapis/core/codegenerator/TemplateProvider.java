package com.docapis.core.codegenerator;


import com.docapis.core.Resources;
import com.docapis.core.Utils;

import java.io.IOException;

/**
 * template provider
 * <p>
 * licence Apache 2.0, from japidoc
 **/
public class TemplateProvider {
    public static String provideTemplateForName(String templateName) throws IOException {
        return Utils.streamToString(Resources.getCodeTemplateFile(templateName));
    }
}
