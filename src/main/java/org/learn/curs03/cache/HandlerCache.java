package org.learn.curs03.cache;

import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.temporal.ChronoUnit.MILLIS;

public class HandlerCache<T> implements InvocationHandler {
  private final Map<Method, CacheData> cacheDataMap;
  private final T obj;
  private boolean work = true;

  public HandlerCache(T obj) {
    this.obj = obj;
    cacheDataMap = new HashMap<>();
    Thread liveControl = new Thread(this::cacheLiveControl);
    liveControl.start();
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
    Method actualMethod = obj.getClass().getMethod(method.getName(), method.getParameterTypes());
    if (actualMethod.isAnnotationPresent(Mutator.class)) {
      return mutator(actualMethod, args);
    }
    if (actualMethod.isAnnotationPresent(Cache.class)) {
      long live = actualMethod.getAnnotation(Cache.class).value();
      return getCash(actualMethod, args, live);
    }
    return actualMethod.invoke(obj, args);
  }

  private Object mutator(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
    cacheDataMap.clear();
    return method.invoke(obj, args);
  }

  private Object getCash(Method method, Object[] args, long cacheLive) throws InvocationTargetException, IllegalAccessException {
    if (cacheDataMap.containsKey(method)) {
      CacheData cacheData = cacheDataMap.get(method);
      cacheData.deathTime = cacheData.deathTime.plus(cacheLive, MILLIS);
      return cacheData.data;
    }
    Object result = method.invoke(obj, args);
    LocalDateTime death = LocalDateTime.now().plus(cacheLive, MILLIS);
    cacheDataMap.put(method, new CacheData(result, death));
    return result;
  }

  private void cacheLiveControl() {
    while (work) {
      cacheLiveControlItem();
      try {
        long waitTime = 50;
        Thread.sleep(waitTime);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private void cacheLiveControlItem() {
    List<Method> removes = cacheDataMap.entrySet().stream()
            .filter(x -> x.getValue().deathTime.isBefore(LocalDateTime.now()))
            .map(Map.Entry::getKey).toList();
    if (!removes.isEmpty()) {
      synchronized (cacheDataMap) {
        removes.forEach(cacheDataMap::remove);
      }
    }
  }

  public void shutdown(){
    work = false;
  }

  @AllArgsConstructor
  class CacheData {
    Object data;
    LocalDateTime deathTime;
  }
}


