package com.masaiqi.geekbang.mvc;

import com.masaiqi.geekbang.mvc.annotation.ResponseType;
import com.masaiqi.geekbang.mvc.controller.Controller;
import com.masaiqi.geekbang.mvc.factory.ResponseHandlerFactory;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import static java.util.Arrays.asList;
import static org.apache.commons.lang.StringUtils.substringAfter;

/**
 * 自定义MVC框架 Servlet
 *
 * @author masaiqi
 * @date 2021/3/1 3:13 PM
 */
public class FrontControllerServlet extends HttpServlet {

    /**
     * key - 请求路径 | value - 请求方法元数据(可能有同路径的多种请求类型，比如Post+Get，因此是一个集合)
     */
    private Map<String, List<HandlerMethodMetadata>> handlerMethodMetadataMap = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        initHandlerMethodMetadata(config);
    }

    /**
     * 初始化HandlerMethodMetadata
     *
     * @param config
     * @return void
     * @author masaiqi
     * @date 2021/3/1 3:43 PM
     */
    private void initHandlerMethodMetadata(ServletConfig config) {
        // 基于Java SPI发现控制器实现类
        for (Controller controller : ServiceLoader.load(Controller.class)) {
            Class<? extends Controller> controllerClass = controller.getClass();
            Path path = controllerClass.getAnnotation(Path.class);
            if (path == null) {
                continue;
            }

            // 执行初始化钩子函数
            controller.init();

            String requestPath = path.value();
            Method[] publicMethods = controllerClass.getMethods();
            // 处理方法支持的 HTTP 方法集合
            for (Method method : publicMethods) {
                tryParseHandlerMethodMetadata(requestPath, method, controller)
                        .ifPresent(handlerMethodMetadata -> {
                            String specialRequestPath = handlerMethodMetadata.get(0).getRequestPath();
                            List<HandlerMethodMetadata> handlerMethodMetadataList = handlerMethodMetadataMap.get(specialRequestPath);
                            if (handlerMethodMetadataList == null) {
                                handlerMethodMetadataList = new ArrayList<>(1);
                                handlerMethodMetadataMap.put(specialRequestPath, handlerMethodMetadataList);
                            }
                            // 省略查重验证...
                            handlerMethodMetadataList.addAll(handlerMethodMetadata);
                        });
            }
        }
    }

    /**
     * 转化为 {@link HandlerMethodMetadata} 对象
     *
     * @param rootRequestPath
     * @param method
     * @param controller
     * @return java.util.Optional<com.masaiqi.geekbang.mvc.HandlerMethodMetadata>
     * @author masaiqi
     * @date 2021/3/1 4:06 PM
     */
    private Optional<List<HandlerMethodMetadata>> tryParseHandlerMethodMetadata(String rootRequestPath, Method method, Controller controller) {
        Path path = method.getAnnotation(Path.class);
        if (path == null) {
            return Optional.empty();
        }
        // 省略path.value为Blank的处理...
        // 省略path.value判断是否有"/"字符开头的验证...
        String requestPath = rootRequestPath + path.value().trim();

        // 获取ResponseType
        String responseType;
        ResponseType responseTypeAnnotation = method.getAnnotation(ResponseType.class);
        if (responseTypeAnnotation != null) {
            responseType = responseTypeAnnotation.value();
        } else {
            // 默认REST类型
            responseType = ResponseType.REST;
        }

        Set<String> supportHttpMethods = new HashSet<>();
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            HttpMethod httpMethod = annotation.annotationType().getAnnotation(HttpMethod.class);
            if (httpMethod != null) {
                supportHttpMethods.add(httpMethod.value());
            }
        }
        if (supportHttpMethods.size() == 0) {
            // 默认支持所有请求
            supportHttpMethods.addAll(asList(HttpMethod.GET, HttpMethod.POST,
                    HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.HEAD, HttpMethod.OPTIONS));
        }
        List<HandlerMethodMetadata> result = new ArrayList<>(supportHttpMethods.size());
        for (String supportHttpMethod : supportHttpMethods) {
            HandlerMethodMetadata addHandlerMethod = new HandlerMethodMetadata(requestPath, method, controller, supportHttpMethod, responseType);
            result.add(addHandlerMethod);
        }
        return Optional.of(result);
    }


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 建立映射关系
        // requestURI = /a/hello/world
        String requestURI = request.getRequestURI();
        // contextPath  = /a or "/" or ""
        String servletContextPath = request.getContextPath();
        String prefixPath = servletContextPath;
        // 映射路径（子路径）
        String requestMappingPath = substringAfter(requestURI,
                StringUtils.replace(prefixPath, "//", "/"));
        // 映射到具体方法
        List<HandlerMethodMetadata> handlerMethodMetadataList = handlerMethodMetadataMap.get(requestMappingPath);
        if (handlerMethodMetadataList == null) {
            // 省略跳转404页面...
            return;
        }
        String httpMethod = request.getMethod();
        for (int i = 0; i < handlerMethodMetadataList.size(); i++) {
            HandlerMethodMetadata handlerMethodMetadata = handlerMethodMetadataList.get(i);
            String supportedHttpMethod = handlerMethodMetadata.getSupportedHttpMethod();
            if (httpMethod.equals(supportedHttpMethod)) {
                try {
                    Object result = handlerMethodMetadata.getHandlerMethod().invoke(handlerMethodMetadata.getHandlerClass(), request, response);
                    // 根据回调类型不同调用不一样的策略
                    ResponseHandlerFactory.getResponseHandler(handlerMethodMetadata.getResponseType())
                            .handle(request, response, result);
                    return;
                } catch (Throwable throwable) {
                    if (throwable.getCause() instanceof IOException) {
                        throw (IOException) throwable.getCause();
                    } else {
                        throw new ServletException(throwable.getCause());
                    }
                }
            }
        }
        // 省略跳转到404页面...
    }
}
