package com.docapis.core.codegenerator.provider;


import com.docapis.core.codegenerator.IFieldProvider;

/**
 * provider factory
 * <p>
 * licence Apache 2.0, from japidoc
 **/
public class ProviderFactory {

    public static IFieldProvider createProvider() {
        return new DocFieldProvider();
    }
}
