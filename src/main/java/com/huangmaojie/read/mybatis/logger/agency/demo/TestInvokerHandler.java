package com.huangmaojie.read.mybatis.logger.agency.demo;

import com.huangmaojie.read.mybatis.logger.agency.Proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author huangmaojie
 * @date 2021/12/7
 */
public class TestInvokerHandler implements InvocationHandler {

    // 真正的业务对象，也就是RealSubject对象
    Object target;

    public TestInvokerHandler(Object target) {
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 在执行业务方法之前的预处理
        System.out.println("before");
        Object result = method.invoke(target, args);
        // 在执行业务方法之后的后置处理
        System.out.println("after");
        return result;
    }

    public Object getProxy() {
        // 创建代理对象
//            return Proxy.newProxyInstance(Subject.class.getClassLoader(),
//                    new Class[]{Subject.class},
//                    new TestInvokerHandler(new RealSubject()));
        return Proxy.newProxyInstance(Subject.class.getClassLoader(), target.getClass().getInterfaces(), this);
    }

}
