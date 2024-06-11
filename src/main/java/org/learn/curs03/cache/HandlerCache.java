package org.learn.curs03.cache;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HandlerCache<T> implements InvocationHandler {
  private final Map<Method, Object> cacheData;
  private final T obj;

  public HandlerCache(T obj) {
    this.obj = obj;
    cacheData = new HashMap<>();
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
    Method actualMethod = obj.getClass().getMethod(method.getName(), method.getParameterTypes());
    if (actualMethod.isAnnotationPresent(Mutator.class)) {
      return mutator(actualMethod, args);
    }
    if (actualMethod.isAnnotationPresent(Cache.class)) {
      return getCash(actualMethod, args);
    }
    return actualMethod.invoke(obj, args);
  }

  private Object mutator(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
    cacheData.clear();
    return method.invoke(obj, args);
  }

  private Object getCash(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
    if (cacheData.containsKey(method)) {
      return cacheData.get(method);
    }
    Object result = method.invoke(obj, args);
    cacheData.put(method, result);
    return result;
  }

}


