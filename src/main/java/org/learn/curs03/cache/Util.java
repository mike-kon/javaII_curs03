package org.learn.curs03.cache;

import java.lang.reflect.Proxy;

public class Util {
  public static <T> T cache(T object){
    return (T) Proxy.newProxyInstance(
            object.getClass().getClassLoader(),
            object.getClass().getInterfaces(),
            new HandlerCache<>(object));
  }

}
