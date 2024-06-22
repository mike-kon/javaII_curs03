package org.learn.curs03.cache;

import java.lang.reflect.Proxy;

public class Util {
  static ICache cache = null;
  public static <T> T getCache(T object) {
    if (cache == null){
      cache = new CacheImpl<T>(10, 0.7, 5);
    }
    return (T) Proxy.newProxyInstance(
        object.getClass().getClassLoader(),
        object.getClass().getInterfaces(),
        new HandlerCache<>(cache, object)
    );
  }

  public static void shutdown(){
    cache.shutdown();
  }
}
