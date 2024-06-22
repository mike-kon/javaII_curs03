package org.learn.curs03.worker;

import org.learn.curs03.cache.annotations.Cache;

/**
 * Формула.
 * (A*B*C) / (A + B - C)
 */
public class WrokerTwo extends WorkClass{
  @Override
  @Cache
  public double cal() throws InterruptedException {
    Thread.sleep(waiters);
    double divisible = getStay().getA();
    Thread.sleep(waiters);
    divisible *= getStay().getB();
    Thread.sleep(waiters);
    divisible *= getStay().getC();
    Thread.sleep(waiters);
    double divider = getStay().getA();
    Thread.sleep(waiters);
    divider += getStay().getB();
    Thread.sleep(waiters);
    divider += getStay().getC();
    Thread.sleep(waiters);
    double res = divisible / divider;
    Thread.sleep(waiters * 2);
    return res;
  }
}
