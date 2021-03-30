package com.masaiqi.geekbang.configuration.microprofile.config.source.servlet;


import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Set;

/**
 * 这个类交给SPI加载，servlet规范会保证被加载
 */
public class ServletConfigInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext servletContext) throws ServletException {
    }
}
