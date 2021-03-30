package com.masaiqi.geekbang.web.projects.user.web.listener;

import com.masaiqi.geekbang.configuration.microprofile.config.InitConfig;
import com.masaiqi.geekbang.context.ClassicComponentContext;
import com.masaiqi.geekbang.web.projects.user.domain.User;
import com.masaiqi.geekbang.web.projects.user.management.UserManager;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.lang.management.ManagementFactory;

/**
 *
 * @author masaiqi
 * @date 2021/3/2 10:57 AM
 */
public class ComponentContextInitializerListener implements ServletContextListener {

    private ServletContext servletContext;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        this.servletContext = sce.getServletContext();
        ClassicComponentContext classicComponentContext = new ClassicComponentContext();
        classicComponentContext.init(servletContext);

        // 初始化JMX MBean
        initMBean();

        // 初始化Config
        InitConfig.doInitConfig(sce);



        ConfigProviderResolver configProviderResolver = ConfigProviderResolver.instance();
        Config config = configProviderResolver.getConfig(servletContext.getClassLoader());
    }

    private void initMBean() {
        // 获取平台 MBean Server
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        // 为 UserMXBean 定义 ObjectName

        // TODO 交给自定义IOC容器

        try {
            ObjectName objectName = null;
            objectName = new ObjectName("org.geektimes.projects.user.management:type=User");
            // TODO hard code作为一个DEMO，创建 UserMBean 实例
            User user = new User();
            mBeanServer.registerMBean(new UserManager(new User()), objectName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
