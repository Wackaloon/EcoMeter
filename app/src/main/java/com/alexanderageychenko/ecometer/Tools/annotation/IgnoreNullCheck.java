package com.alexanderageychenko.ecometer.Tools.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * Created by Alexander on 01.05.2017.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD, PARAMETER, FIELD, ANNOTATION_TYPE, PACKAGE})
public @interface IgnoreNullCheck {
}
