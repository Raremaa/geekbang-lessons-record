package com.masaiqi.geekbang.web.context;

import com.masaiqi.geekbang.web.function.ThrowableAction;
import com.masaiqi.geekbang.web.function.ThrowableFunction;
import com.masaiqi.geekbang.web.projects.user.domain.User;
import com.masaiqi.geekbang.web.projects.user.management.UserManager;
import com.masaiqi.geekbang.web.projects.user.web.listener.ComponentContextInitializerListener;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.management.*;
import javax.naming.*;
import javax.servlet.ServletContext;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * 组件上下文
 * 由{@link ComponentContextInitializerListener} 进行实例化
 * 本质上是对servlet的上下文ServletContext进行了封装，透明化底层的实现，开放部分基于底层的能力给上层服务调用
 *
 * @author masaiqi
 * @date 2021/3/9 2:25 PM
 */
@Deprecated
public class ComponentContext {

    public static final String CONTEXT_NAME = ComponentContext.class.getName();

    private static final String COMPONENT_ENV_CONTEXT_NAME = "java:comp/env";

    private static final Logger logger = Logger.getLogger(CONTEXT_NAME);

    private static ServletContext servletContext; // 请注意
    // 假设一个 Tomcat JVM 进程，三个 Web Apps，会不会相互冲突？（不会冲突）
    // static 字段是 JVM 缓存吗？（是 ClassLoader 缓存）

//    private static ApplicationContext applicationContext;

//    public void setApplicationContext(ApplicationContext applicationContext){
//        ComponentContext.applicationContext = applicationContext;
//        WebApplicationContextUtils.getRootWebApplicationContext()
//    }

    /**
     * Component Env Context
     * For Instance, /Tomcat/localhost
     */
    private Context envContext;

    private ClassLoader classLoader;

    private Map<String, Object> componentsMap = new LinkedHashMap<>();

    public static ComponentContext getInstance() {
        return (ComponentContext) servletContext.getAttribute(CONTEXT_NAME);
    }

    /**
     * 通过名称进行依赖查找
     *
     * @param name
     * @param <C>
     * @return
     */
    public <C> C getComponent(String name) {
        return (C) componentsMap.get(name);
    }

    public void destroy() throws RuntimeException {
        close(this.envContext);
    }

    public void init(ServletContext servletContext) {
        ComponentContext.servletContext = servletContext;
        servletContext.setAttribute(CONTEXT_NAME, this);
        // 获取当前 ServletContext（WebApp）ClassLoader -> WebappClassLoader
        this.classLoader = servletContext.getClassLoader();
        initEnvContext();
        instantiateComponents();
        initializeComponents();

        // 初始化JMX MBean
        initMBean();

        // 初始化 microprofile
        initMicroprofile();
    }

    private void initMicroprofile() {
        // #instance method generate instant based on SPI, This is a singleton implementation with double-check for thread-safe
        ConfigProviderResolver configProviderResolvers = ConfigProviderResolver.instance();
        if (configProviderResolvers == null) {
            return;
        }

        // TODO 交给自定义IOC容器

        // 寻找配置名字并输出
        String key = "property.name";
        String value = configProviderResolvers.getConfig().getValue(key, String.class);
        logger.info("microprofile read: " + key + " | " + value);
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

    private void initializeComponents() {
        componentsMap.values().forEach(component -> {
            Class<?> componentClass = component.getClass();
            // 注入阶段 - {@link Resource}
            injectComponents(component, componentClass);
            // 初始阶段 - {@link PostConstruct}
            processPostConstruct(component, componentClass);
            // TODO 实现销毁阶段 - {@link PreDestroy}
            processPreDestroy();
        });
    }

    private void processPreDestroy() {
        // TODO
    }

    private void processPostConstruct(Object component, Class<?> componentClass) {
        Stream.of(componentClass.getMethods())
                .filter(method ->
                        !Modifier.isStatic(method.getModifiers()) &&      // 非 static
                                method.getParameterCount() == 0 &&        // 没有参数
                                method.isAnnotationPresent(PostConstruct.class) // 标注 @PostConstruct
                ).forEach(method -> {
            // 执行目标方法
            try {
                method.invoke(component);
            } catch (Exception e) {
                throw new RuntimeException(e.getCause());
            }
        });
    }

    private void injectComponents(Object component, Class<?> componentClass) {
        Stream.of(componentClass.getDeclaredFields())
                .filter(field -> {
                    int mods = field.getModifiers();
                    return !Modifier.isStatic(mods) &&
                            field.isAnnotationPresent(Resource.class);
                }).forEach(field -> {
            Resource resource = field.getAnnotation(Resource.class);
            String resourceName = resource.name();
            Object injectedObject = lookupComponent(resourceName);
            field.setAccessible(true);
            try {
                // 注入目标对象
                field.set(component, injectedObject);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e.getCause());
            }
        });
    }

    private void instantiateComponents() {
        // 遍历获取所有的组件名称
        List<String> componentNames = listAllComponentNames();
        // 通过依赖查找，实例化对象（ Tomcat BeanFactory setter 方法的执行，仅支持简单类型）
        componentNames.forEach(name -> componentsMap.put(name, lookupComponent(name)));
    }

    private <R> R executeInContext(Context context, ThrowableFunction<Context, R> function,
                                   boolean ignoredException) {
        R result = null;
        try {
            result = ThrowableFunction.execute(context, function);
        } catch (Throwable e) {
            if (ignoredException) {
                logger.warning(e.getMessage());
            } else {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    /**
     * 在 Context 中执行，通过指定 ThrowableFunction 返回计算结果
     *
     * @param function         ThrowableFunction
     * @param ignoredException 是否忽略异常
     * @param <R>              返回结果类型
     * @return 返回
     * @see ThrowableFunction#apply(Object)
     */
    protected <R> R executeInContext(ThrowableFunction<Context, R> function, boolean ignoredException) {
        return executeInContext(this.envContext, function, ignoredException);
    }

    /**
     * 在 Context 中执行，通过指定 ThrowableFunction 返回计算结果
     *
     * @param function ThrowableFunction
     * @param <R>      返回结果类型
     * @return 返回
     * @see ThrowableFunction#apply(Object)
     */
    protected <R> R executeInContext(ThrowableFunction<Context, R> function) {
        return executeInContext(function, false);
    }

    protected <C> C lookupComponent(String name) {
        return executeInContext(context -> (C) context.lookup(name));
    }

    protected List<String> listComponentNames(String name) {
        return executeInContext(context -> {
            NamingEnumeration<NameClassPair> e = executeInContext(context, ctx -> ctx.list(name), true);

            // 目录 - Context
            // 节点 -
            if (e == null) { // 当前 JNDI 名称下没有子节点
                return Collections.emptyList();
            }

            List<String> fullNames = new LinkedList<>();
            while (e.hasMoreElements()) {
                NameClassPair element = e.nextElement();
                String className = element.getClassName();
                Class<?> targetClass = classLoader.loadClass(className);
                if (Context.class.isAssignableFrom(targetClass)) {
                    // 如果当前名称是目录（Context 实现类）的话，递归查找
                    fullNames.addAll(listComponentNames(element.getName()));
                } else {
                    // 否则，当前名称绑定目标类型的话话，添加该名称到集合中
                    String fullName = name.startsWith("/") ?
                            element.getName() : name + "/" + element.getName();
                    fullNames.add(fullName);
                }
            }
            return fullNames;
        });
    }

    private List<String> listAllComponentNames() {
        return listComponentNames("/");
    }

    private void initEnvContext() throws RuntimeException {
        if (this.envContext != null) {
            return;
        }
        Context context = null;
        try {
            context = new InitialContext();
            this.envContext = (Context) context.lookup(COMPONENT_ENV_CONTEXT_NAME);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } finally {
            close(context);
        }
    }

    private static void close(Context context) throws RuntimeException {
        if (context != null) {
            ThrowableAction.execute(context::close);
        }
    }
}
