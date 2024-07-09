package com.docapis.core;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * i18n
 * <p>
 * licence Apache 2.0, from japidoc
 **/
public class I18n {

    private ResourceBundle resourceBundle;

    public I18n() {
        this.resourceBundle = ResourceBundle.getBundle("message", Locale.getDefault());
    }

    public I18n(Locale locale) {
        this.resourceBundle = ResourceBundle.getBundle("message", locale);
    }

    public String getMessage(String name) {
        return resourceBundle.getString(name);
    }
}
