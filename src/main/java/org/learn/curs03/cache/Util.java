package org.learn.curs03.cache;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class Util {
  static List<HandlerCache> handlers = new ArrayList<>();
  public static <T> T cache(T object) {
    ClassLoader classLoader = object.getClass().getClassLoader();
    Class<?>[] interfaces = object.getClass().getInterfaces();
    HandlerCache<T> handle = new HandlerCache<>(object);
    handlers.add(handle);
    return (T) Proxy.newProxyInstance(classLoader, interfaces, handle);
  }

  public static void shutdown(){
    handlers.forEach(HandlerCache::shutdown);
  }
}
