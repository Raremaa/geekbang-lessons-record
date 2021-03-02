package com.masaiqi.geekbang.mvc.factory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 转发处理策略
 *
 * @author masaiqi
 * @date 2021/3/1 5:16 PM
 */
public class ForwardResponseHandler implements ResponseHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object result) throws Throwable {
        if (result instanceof String) {
            String viewPath = (String) result;

            // 页面请求 forward
            // request -> RequestDispatcher forward
            // RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewPath);
            // ServletContext -> RequestDispatcher forward
            // ServletContext -> RequestDispatcher 必须以 "/" 开头
            ServletContext servletContext = httpServletRequest.getServletContext();
            if (!viewPath.startsWith("/")) {
                viewPath = "/" + viewPath;
            }
            RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(viewPath);
            requestDispatcher.forward(httpServletRequest, httpServletResponse);
        } else {
            throw new RuntimeException("Forward Response must be String!");
        }
    }
}
