package com.jwcomptech.commons.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface FeatureComplete {
    String since() default "1.0.0";
    String note() default "Do not modify unless critical.";
}
