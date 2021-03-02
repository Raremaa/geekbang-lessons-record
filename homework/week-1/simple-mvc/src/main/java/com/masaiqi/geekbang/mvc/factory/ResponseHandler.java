package com.masaiqi.geekbang.mvc.factory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 结果处理策略接口
 *
 * @author masaiqi
 * @date 2021/3/1 5:12 PM
 */
public interface ResponseHandler {

    /**
     * 处理接口
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @return void
     * @author masaiqi
     * @date 2021/3/1 5:35 PM
     */
    void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object result) throws Throwable;
}
