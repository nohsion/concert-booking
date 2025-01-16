package com.sion.concertbooking.intefaces.presentation.accesslog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogMapping {

    LogGroup logGroup() default LogGroup.DEFAULT;
}
