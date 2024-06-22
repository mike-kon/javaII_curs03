package org.learn.curs03.cache;

import lombok.AllArgsConstructor;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheImpl<T> implements ICache<T>{
  private final int size;
  private final double porog;
  private final long cacheLiveSec;
  private final HashMap<Integer,HashMap<Method,CacheObject<T>>> cacheData;
  private final Thread clearTh = new Thread(this::Clear);
  private volatile boolean work = true;

  public CacheImpl(int size, double porog, long cacheLiveSec) {
    this.size = size;
    this.porog = porog;
    this.cacheLiveSec = cacheLiveSec;
    cacheData = new HashMap<>();
    clearTh.start();
  }

  private double capacity() {
    Double res = Double.valueOf (cacheData.values().stream().map(HashMap::size).reduce(0, Integer::sum));
    return res / size;
  }
  @Override
  public T getCache(int keyObject, Method keyMethod) throws Throwable {
    // проверяем, есть-ли такая группа?
    HashMap<Method,CacheObject<T>> object = getMethodCacheObjectHashMap(keyObject);
    // группа есть - отпускаем keys и дальше работаем с группой
    synchronized (object) {
      if (object.containsKey(keyMethod)){
        CacheObject<T> cacheItem = object.get(keyMethod);
        cacheItem.lastAccess = LocalDateTime.now();
        if (cacheItem.exception == null){
          return cacheItem.object;
        } else {
          throw cacheItem.exception;
        }
      } else {
        throw new CacheKeyNotFoundException();
      }
    }
  }

  @Override
  public boolean IsExistCache(int keyObject, Method keyMethod) {
    throw new RuntimeException("under construction");
  }

  @Override
  public void putCache(int keyObject, Method keyMethod, T value) {
    checkClearCache();
    HashMap<Method,CacheObject<T>> object = getMethodCacheObjectHashMap(keyObject);
    synchronized (object) {
      object.put(keyMethod, new CacheObject<>(value, LocalDateTime.now(), null));
    }
  }

  private void checkClearCache() {
    if (capacity() > porog){
      clearTh.notify();
    }
  }

  @Override
  public void putCache(int keyObject, Method keyMethod, Throwable exception) {
    HashMap<Method,CacheObject<T>> object = getMethodCacheObjectHashMap(keyObject);
    synchronized (object) {
      object.put(keyMethod,new CacheObject<>(null,LocalDateTime.now(), exception));
    }
  }

  @Override
  public void invalidateCache(int keyObject) {
    synchronized (cacheData) {
      cacheData.remove(keyObject);
    }
  }

  @Override
  public void shutdown() {
    work = false;
    clearTh.notify();
  }

  private HashMap<Method, CacheObject<T>> getMethodCacheObjectHashMap(int keyObject) {
    HashMap<Method, CacheObject<T>> object;
    synchronized (cacheData) {
      if (!cacheData.containsKey(keyObject)) {
        object = new HashMap<>();
        cacheData.put(keyObject, object);
      } else {
        object = cacheData.get(keyObject);
      }
    }
    return object;
  }

  /**
   * Метод выполняется постоянно в отдельном потоке.
   * При старте он сразу засыпает и ждет, когда его разбудят.
   * Разбудить могут или для очистки, или для окончания работы.
   */
  private void Clear() {
    try {
      while (true) {
        // Сразу засыпаем и ждем, когда нас разбудят
        wait();
        if (!work) {
          // если разбудили для окончания, то выходим.
          return;
        }
        // иначе проводим очистку
        // вычисляем время, ранее которого все будет удаляться.
        LocalDateTime die = LocalDateTime.now().minusSeconds(cacheLiveSec);
        for (Map.Entry<Integer, HashMap<Method, CacheObject<T>>> objects : cacheData.entrySet()) {
          // Для каждого объекта в кеше отбираем ключи для удаления
          // делаем это без синхронизации. Если за это время добавились новые значения, то их не учитываем
          List<Method> removeKeys =
              objects.getValue().entrySet().stream()
                  .filter(x -> x.getValue().lastAccess.isBefore(die))
                  .map(Map.Entry::getKey).toList();
          if (!removeKeys.isEmpty()) {
            var obj = objects.getValue();
            // теперь удаляем то, что нашли.
            synchronized (obj){
              for (Method rkey : removeKeys) {
                if (obj.containsKey(rkey)) {
                  // Еще раз проверяем перед удалением.
                  // Пока собирали ключи для удаления, кого-то могли "дернуть" и продлить жизнь,
                  // а кого-то удалить из-за вызова мутатора.
                  if (obj.get(rkey).lastAccess.isBefore(die)) {
                    obj.remove(rkey);
                  }
                }
              }
            }
          }
        }
        // теперь удалим те группы, у которых не осталось значений.
        synchronized (cacheData) {
          for (Map.Entry<Integer, HashMap<Method, CacheObject<T>>> objects : cacheData.entrySet()){
            if (objects.getValue().isEmpty()){
              cacheData.remove(objects.getKey());
            }
          }
        }
      }
    } catch (Throwable ex) {
      throw new RuntimeException(ex);
    }
  }

  @AllArgsConstructor
  static class CacheObject<O> {
    O object;
    LocalDateTime lastAccess;
    Throwable exception;
  }
}
