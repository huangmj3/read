package com.huangmaojie.read.mybatis.logger.agency;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MyJdkProxyTest {
    public interface Subject {
        void sayHello();
    }

    static class RealSubject implements Subject {
        @Override
        public void sayHello() {
            System.out.println("Hello World");
        }
    }

    static class TestInvokerHandler implements InvocationHandler {
        // 真正的业务对象，也就是RealSubject对象
        private Object target;

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
            return Proxy.newProxyInstance(Thread.currentThread()
                    .getContextClassLoader(), target.getClass().getInterfaces(), this);
        }
    }

    public static void main(String[] args) {
        RealSubject subject = new RealSubject();
        TestInvokerHandler testInvokerHandler = new TestInvokerHandler(subject);
        Subject proxy = (Subject) testInvokerHandler.getProxy();
        proxy.sayHello();
    }
}
