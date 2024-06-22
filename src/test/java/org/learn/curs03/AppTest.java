package org.learn.curs03;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.learn.curs03.cache.Util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AppTest {

  @AfterAll
  public static void stop() {
    Util.shutdown();
  }

  @Test
  public void PutCacheOneValue() throws Exception {
    ITestCache test = Util.getCache(new TestCache());
    test.setValue(100);
    int value = test.getValue();
    int count = test.getCount();
    Assertions.assertEquals(100, value);
    Assertions.assertEquals(1, count);
    value = test.getValue();
    count = test.getCount();
    Assertions.assertEquals(100, value);
    Assertions.assertEquals(1, count);
  }

  @Test
  public void PutCacheManyValue() throws Exception {
    ITestCache test = Util.getCache(new TestCache());
    test.setValue(100);
    Assertions.assertEquals(100, test.getValue());
    Assertions.assertEquals(1, test.getCount());
    Assertions.assertEquals(100, test.getValue());
    Assertions.assertEquals(1, test.getCount());
    test.setValue(200);
    Assertions.assertEquals(200, test.getValue());
    Assertions.assertEquals(2, test.getCount());
    Assertions.assertEquals(200, test.getValue());
    Assertions.assertEquals(2, test.getCount());
    test.setValue(100);
    Assertions.assertEquals(100, test.getValue());
    Assertions.assertEquals(3, test.getCount());
    Assertions.assertEquals(100, test.getValue());
    Assertions.assertEquals(3, test.getCount());
  }

  @Test
  public void PutCacheException() throws Exception {
    ITestCache test = Util.getCache(new TestCache());
    test.setException();
    assertThrows(Exception.class, test::getValue, TestCache.ExceptionMessage);
    assertEquals(1, test.getCount());
    assertThrows(Exception.class, test::getValue, TestCache.ExceptionMessage);
    assertEquals(1, test.getCount());
    test.setValue(100);
    Assertions.assertEquals(100, test.getValue());
    Assertions.assertEquals(2, test.getCount());
    test.setException();
    assertThrows(Exception.class, test::getValue, TestCache.ExceptionMessage);
    assertEquals(3, test.getCount());
  }
}
