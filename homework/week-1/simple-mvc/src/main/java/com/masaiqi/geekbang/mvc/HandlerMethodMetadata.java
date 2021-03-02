package com.masaiqi.geekbang.mvc;

import com.masaiqi.geekbang.mvc.controller.Controller;

import java.lang.reflect.Method;

/**
 * 处理方法数据元数据信息
 *
 * @author masaiqi
 * @date 2021/3/1 3:39 PM
 */
public class HandlerMethodMetadata {


    private final String requestPath;

    private final Method handlerMethod;

    private final Controller handlerClass;

    private final String supportedHttpMethod;

    private final String responseType;


    public HandlerMethodMetadata(String requestPath, Method handlerMethod, Controller handlerClass, String supportedHttpMethod, String responseType) {
        this.requestPath = requestPath;
        this.handlerMethod = handlerMethod;
        this.handlerClass = handlerClass;
        this.supportedHttpMethod = supportedHttpMethod;
        this.responseType = responseType;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public Method getHandlerMethod() {
        return handlerMethod;
    }

    public Controller getHandlerClass() {
        return handlerClass;
    }

    public String getSupportedHttpMethod() {
        return supportedHttpMethod;
    }

    public String getResponseType() {
        return responseType;
    }
}
