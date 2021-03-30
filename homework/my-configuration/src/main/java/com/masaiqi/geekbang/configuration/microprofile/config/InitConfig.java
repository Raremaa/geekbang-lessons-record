package com.masaiqi.geekbang.configuration.microprofile.config;

import com.masaiqi.geekbang.configuration.microprofile.config.source.servlet.ServletContextConfigSource;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

/**
 * @author masaiqi
 * @date 2021/3/30 9:20 AM
 */
public class InitConfig {

    public static void doInitConfig(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        ClassLoader classLoader = servletContext.getClassLoader();

        ServletContextConfigSource servletContextConfigSource = new ServletContextConfigSource(servletContext);

        // 初始化ConfigProviderResolver，底层源码基于SPI完成实例化
        ConfigProviderResolver configProviderResolver = ConfigProviderResolver.instance();
        ConfigBuilder configBuilder = configProviderResolver.getBuilder();
        // 配置 ClassLoader
        configBuilder.forClassLoader(classLoader);
        // 默认配置源（内建的，静态的）
        configBuilder.addDefaultSources();
        // 通过发现配置源（动态的，自己实现，这里基于SPI加载Convert的实现类）
        configBuilder.addDiscoveredConverters();
        // 增加扩展配置源（基于 Servlet 引擎）
        configBuilder.withSources(servletContextConfigSource);
        // 获取 Config
        Config config = configBuilder.build();
        // 注册 Config 关联到当前 ClassLoader
        configProviderResolver.registerConfig(config, classLoader);
    }
}
