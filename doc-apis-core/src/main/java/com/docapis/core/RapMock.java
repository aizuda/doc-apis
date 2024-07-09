package com.docapis.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RapMock annotation
 * <p>
 * licence Apache 2.0, from japidoc
 **/
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD})
public @interface RapMock {

    String limit() default "";

    String value() default "";
}
