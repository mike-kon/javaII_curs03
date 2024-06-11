package org.learn.curs03.cache;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Util {
  public static <T> T cache(T object) {
    ClassLoader classLoader = object.getClass().getClassLoader();
    Class<?>[] interfaces = object.getClass().getInterfaces();
    InvocationHandler handle = new HandlerCache<>(object);
    return (T) Proxy.newProxyInstance(classLoader, interfaces, handle);
  }

}
