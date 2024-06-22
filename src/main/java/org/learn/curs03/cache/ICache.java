package org.learn.curs03.cache;

import java.lang.reflect.Method;

/**
 * Методы для работы с кешем.
 * @param <T> - тип кешируемого значения
 */
public interface ICache<T> {

  /**
   * Извлекает данные из кэша. Возвращает объект типа T, null или вызывает исключение, лежащее в кеше.
   * В случае отсутствия значения вызывается исключение CacheKeyNotFoundException.
   * Счетчик обращений сбрасывается.
   * @param keyObject идентификатор объекта
   * @param keyMethod идентификатор метода
   * @return данные из кеша
   * @throws CacheKeyNotFoundException в случае отсутствия данных в кеше
   */
  T getCache(int keyObject, Method keyMethod) throws Throwable;

  /**
   * Проверяет наличие значения в кеше
   * @param keyObject идентификатор объекта
   * @param keyMethod идентификатор метода
   * @return присутствия значения в кеше
   */
  boolean IsExistCache(int keyObject, Method keyMethod);

  /**
   * Сохраняет значение в Кеше.
   * @param keyObject идентификатор объекта
   * @param keyMethod идентификатор метода
   * @param value объект, помещаемый в кеш
   */
  void putCache(int keyObject, Method keyMethod, T value);


  /**
   * Сохраняет Exception в кеше
   * @param keyObject идентификатор объекта
   * @param keyMethod идентификатор метода
   * @param exception исключение, помещаемое в кеш
   */
  void putCache(int keyObject, Method keyMethod, Throwable exception);

  /**
   * Делает значение кеша невалидным
   * @param keyObject идентификатор объекта
   */
  void invalidateCache(int keyObject);

  /**
   * Вызывается для остановки фоновых процессов кеша.
   */
  void shutdown();

}
