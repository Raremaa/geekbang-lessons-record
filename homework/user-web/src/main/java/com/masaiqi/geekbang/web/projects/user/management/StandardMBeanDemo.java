package com.masaiqi.geekbang.web.projects.user.management;

import com.masaiqi.geekbang.web.projects.user.domain.User;

import javax.management.MBeanInfo;
import javax.management.StandardMBean;

public class StandardMBeanDemo {

    public static void main(String[] args) throws Exception {
        // 将静态的 MBean 接口转化成 DynamicMBean
        StandardMBean standardMBean = new StandardMBean(new UserManager(new User()), UserManagerMBean.class);

        MBeanInfo mBeanInfo = standardMBean.getMBeanInfo();

        System.out.println(mBeanInfo);
    }
}
