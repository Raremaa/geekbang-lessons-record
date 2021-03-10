package com.masaiqi.geekbang.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 控制器方法注解，标志接口回调处理类型
 *
 * @author masaiqi
 * @date 2021/3/1 3:35 PM
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseType {

    /**
     * 转发
     */
    String FORWARD = "FORWARD";

    /**
     * 重定向
     */
    String REDIRECT = "REDIRECT";

    /**
     * REST
     */
    String REST = "REST";

    String value() default REST;

}
