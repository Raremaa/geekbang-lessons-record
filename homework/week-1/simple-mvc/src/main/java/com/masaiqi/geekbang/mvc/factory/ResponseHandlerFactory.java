package com.masaiqi.geekbang.mvc.factory;

import com.masaiqi.geekbang.mvc.annotation.ResponseType;

import java.util.HashMap;
import java.util.Map;

/**
 * 结果处理策略模式工厂
 *
 * @author masaiqi
 * @date 2021/3/1 5:14 PM
 */
public class ResponseHandlerFactory {

    /**
     * Thread-unsafe Container
     * Implementation Class should be singleton
     */
    private static Map<String, ResponseHandler> baseMap = new HashMap<>();

    static {
        baseMap.put(ResponseType.REST, new RestResponseHandler());
        baseMap.put(ResponseType.FORWARD, new ForwardResponseHandler());
        baseMap.put(ResponseType.REDIRECT, new RedirectResponseHandler());
    }

    /**
     * 获取Response处理策略
     *
     * @param type {@link ResponseType}
     * @return com.masaiqi.geekbang.mvc.factory.ResponseHandler
     * @author masaiqi
     * @date 2021/3/1 5:14 PM
     */
    public static ResponseHandler getResponseHandler(String type) {
        return baseMap.get(type);
    }

}
