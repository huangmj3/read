package com.huangmaojie.read.mybatis.resource.singleton;

/**
 * @author huangmaojie
 * @date 2021/11/30
 */
public class Singleton {

    //饿汉模式
    public static class Singleton1 {
        //饿汉模式是最简单的实现方式，在类加载的时候就创建了单例类的对象
        private static final Singleton instance = new Singleton();

        //单例类的构造方法都是私有的，防止外部创建单例类的对象
        private Singleton1() {
        }

        public static Singleton newInstance() {
            return instance; // 返回唯一的羊例对象
        }
    }

    //懒汉模式
    public static class Singleton2 {
        //饿汉模式是最简单的实现方式，在类加载的时候就创建了单例类的对象
        private static Singleton2 instance = null;

        //单例类的构造方法都是私有的，防止外部创建单例类的对象
        private Singleton2() {
        }

        public static Singleton2 newInstance() {
            // 在需要的时候才去创建的单例对象，如果单例对象已经创建，
            // 再次调用newInstance()方法时将不会重新创建新的单例对象，而是直接返回之前创建的单例对象
            if (null == instance) {
                instance = new Singleton2();
                return instance;
            }
            return instance;
        }
    }

    //加锁
    public static class Singleton3 {
        private static Singleton instance = null;

        private Singleton3() {
        }

        //使用synchronized为newInstance()方法加锁
        public static synchronized Singleton newInstance() {
            if (null == instance) {
                instance = new Singleton();
                return instance;
            }
            return instance;
        }
    }

    // 双检查锁
    public static class Singleton4 {
        private static Singleton4 instance = null;

        private Singleton4() {
        }

        public static Singleton4 getInstance() {
            // 第一次检测
            if (instance == null) {
                // 第二次检测
                synchronized (Singleton4.class) {
                    instance = new Singleton4();
                    return instance;

                }
            }
            return instance;
        }
    }


    // 安全的双检查锁
    public static class Singleton5 {
        // 使用volatile关键字修饰instance字段
        private static volatile Singleton5 instance = null;

        private Singleton5() {
        }

        public static Singleton5 getInstance() {
            if (instance == null) {
                //第一次检测
                synchronized (Singleton5.class) {
                    // 第二次检测
                    instance = new Singleton5();
                    return instance;

                }
            }
            return instance;
        }
    }

    public static class Singleton6 {
        // 私有的静态内部类，该静态内部类只会在newInstance()方法中被使用
        private static class SingletonHolder {
            //静态字段
            public static Singleton6 instance = new Singleton6();
        }

        private Singleton6() {
        }

        public static Singleton6 newInstance() {
            // 访问静态内部类中的静态字段
            return SingletonHolder.instance;
        }
    }
}
