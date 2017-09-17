package com.alexanderageychenko.ecometer.Tools.Navigator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by alexanderageychenko on 16.05.17.
 */

@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DefaultScreenType {
}
