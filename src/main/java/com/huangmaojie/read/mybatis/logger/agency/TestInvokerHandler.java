package com.huangmaojie.read.mybatis.logger.agency;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.apache.catalina.startup.Bootstrap;

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
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                target.getClass().getInterfaces(), this);
    }

    public static void main(String[] args) {
        Subject subject = new RealSubject();
        TestInvokerHandler invokerHandler = new TestInvokerHandler(subject);
        //获取代理对象
        Subject proxy = (Subject) invokerHandler.getProxy();
        //调用代理对象的方法，它会调用 TestInvokerHandler.invoke()方法
        proxy.operation();
    }

}
