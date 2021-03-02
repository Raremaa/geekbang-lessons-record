package com.masaiqi.geekbang.web.projects.user.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @author masaiqi
 * @date 2021/3/2 10:57 AM
 */
@WebListener
public class DBConnectionInitializerListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.err.println(1);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
