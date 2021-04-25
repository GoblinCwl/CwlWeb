package com.goblincwl.cwlweb.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring Bean 静态注入
 *
 * @author 朱维
 * @date 2018年11月2日
 */
public class BeanUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (BeanUtil.applicationContext == null) {
            BeanUtil.applicationContext = applicationContext;
        }
    }

    /**
     * 获取applicationContext
     *
     * @return ApplicationContext
     * @date 2020-12-20 23:21:24
     * @author ☪wl
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过name获取 Bean.
     *
     * @param name Bean Name
     * @return Bean Object
     * @date 2020-12-20 23:21:38
     * @author ☪wl
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean.
     *
     * @param clazz Bean Class
     * @param <T>   Bean Class TypeOf
     * @return Bean Object
     * @date 2020-12-20 23:22:05
     * @author ☪wl
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);

    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     *
     * @param name  Bean Name
     * @param clazz Bean Class
     * @param <T>   Bean Class TypeOf
     * @return Bean Object
     * @date 2020-12-20 23:22:22
     * @author ☪wl
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

}