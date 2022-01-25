package com.cnl.mybot.annotation;

import com.cnl.mybot.system.Permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandMethod {
    Permission value() default Permission.NOBODY;
}