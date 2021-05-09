package com.goblincwl.cwlweb.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解方式认证拦截器 注解
 *
 * @author ☪wl
 * @date 2021-05-09 0:11
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenCheck {

    /*校验认证*/
    boolean value() default true;

}
