package com.huangmaojie.read.mybatis;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author huangmaojie
 * @date 2021/11/30
 */
public class TestInvokerHandler implements InvocationHandler {
    // 真正的业务对象，也就是RealSubject对象
    private Object target;

    public TestInvokerHandler(Object target) {
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 在执行业务方法之前的预处理
        Object result = method.invoke(target, args);
        // 在执行业务方法之后的后置处理
        return result;
    }

    public Object getProxy() {
        //创建代理对象
        return com.questions.huangmaojie.mybatis.Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                target.getClass().getInterfaces(), this);
    }

    public static void main(String[] args) {
        com.questions.huangmaojie.mybatis.Subject subject = new RealSubject();
        TestInvokerHandler invokerHandler = new TestInvokerHandler(subject);
        //获取代理对象
        com.questions.huangmaojie.mybatis.Subject proxy = (com.questions.huangmaojie.mybatis.Subject) invokerHandler.getProxy();
        //调用代理对象的方法，它会调用 TestInvokerHandler.invoke()方法
        proxy.operation();
    }

}
