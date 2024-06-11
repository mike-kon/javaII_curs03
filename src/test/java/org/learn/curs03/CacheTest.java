package org.learn.curs03;

import lombok.Getter;
import org.learn.curs03.cache.Cache;
import org.learn.curs03.cache.Mutator;

@Getter
public class CacheTest implements ICacheTest {
  private int cachedCount = 0;
  private int nonCachedCount = 0;

  @Override
  @Cache(100)
  public void cachedMethod() {
    cachedCount++;
  }

  @Override
  @Cache(100)
  public int cachedMethod(int i) {
    cachedCount++;
    return i;
  }

  @Override
  @Cache(100)
  public double cachedMethod(double d) {
    cachedCount++;
    return d;
  }

  @Override
  @Mutator
  public void mutatorMethod() {
  }

  @Override
  public void nonCachedMethod() {
    nonCachedCount++;
  }

}
