package com.masaiqi.geekbang.mvc.factory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 重定向处理策略
 *
 * @author masaiqi
 * @date 2021/3/1 5:16 PM
 */
public class RedirectResponseHandler implements ResponseHandler{
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object result) throws Throwable {
        // 暂时省略......
    }
}
