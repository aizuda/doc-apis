package com.docapis.core.plugin.rap;

/**
 * action type
 * <p>
 * licence Apache 2.0, from japidoc
 **/
enum ActionTypeEnum {

    GET("1"),
    POST("2"),
    PUT("3"),
    DELETE("4");

    public final String type;

    ActionTypeEnum(String type) {
        this.type = type;
    }
}
