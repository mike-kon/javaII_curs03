package org.learn.curs03.cache;

import org.learn.curs03.cache.annotations.Cache;
import org.learn.curs03.cache.annotations.Mutator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerCache<T> implements InvocationHandler {

  private final ICache<T> cache;
  private final T obj;

  public HandlerCache(ICache<T> cache, T obj) {
    this.cache = cache;
    this.obj = obj;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    Method actualMethod = obj.getClass().getMethod(method.getName(), method.getParameterTypes());
    if (actualMethod.isAnnotationPresent(Mutator.class)) {
      return mutator(actualMethod, args);
    }
    if (actualMethod.isAnnotationPresent(Cache.class)) {
      return getCache(actualMethod, args);
    }
    return actualMethod.invoke(obj, args);
  }

  private Object mutator(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
    cache.invalidateCache(getKeyObject());
    return method.invoke(obj, args);
  }

  private Object getCache(Method method, Object[] args) throws Throwable {
    try {
      return cache.getCache(getKeyObject(), method);
    } catch (CacheKeyNotFoundException e) {
      try {
        T res = (T) method.invoke(obj, args);
        cache.putCache(getKeyObject(), method, res);
        return res;
      } catch (InvocationTargetException ex) {
        Throwable workException = ex.getTargetException();
        cache.putCache(getKeyObject(), method, workException);
        throw workException;
      }
    }
  }

  /**
   * Для идентификации объекта нам нужно значение, которое не будет изменяться при изменении состояния объекта
   * Этому требованию отвечает только метод Object::hash(). obj.hash() не подходит, т.к. он может быть переопределен
   * и его результат будет зависеть от значений полей.
   * @return
   */
  private int getKeyObject() {
    return System.identityHashCode(obj);
  }
}


