package com.huangmaojie.read.mybatis.resource.resolverutil;

import java.util.Collection;

/**
 * @author huangmaojie
 * @date 2021/11/30
 */
public class ResolverUtilTest {

    private static final String pkg1 = "com/huangmaojie/read/mybatis/resource/resolverutil";
    private static final String pkg2 = "com/huangmaojie/read/mybatis/resource/classloader";

    public static void main(String[] args) {
        ResolverUtil<ActionBean> resolver = new ResolverUtil<ActionBean>();
        // 在pkg1和pkg2这两个包下查找实现了ActionBean这个类
        resolver.findImplementations(ActionBean.class, pkg1, pkg2);
        // 在pkg1包下查找符合CustomTest条件的类
        resolver.find(new CustomTest(), pkg1);
        // 在pkg2包下查找符合CustomTest条件的类
        resolver.find(new CustomTest(), pkg2);

        //获取上面三次查找的结果
        Collection<Class<? extends ActionBean>> classes = resolver.getClasses();
    }
}
