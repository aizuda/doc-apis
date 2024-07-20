package com.docapis.core.exception;

/**
 * java file not found exception
 * <p>
 * licence Apache 2.0, from japidoc
 **/
public class JavaFileNotFoundException extends RuntimeException {

    public JavaFileNotFoundException() {
    }

    public JavaFileNotFoundException(String message) {
        super(message);
    }
}
