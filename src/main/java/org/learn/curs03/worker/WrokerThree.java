package org.learn.curs03.worker;

import org.learn.curs03.cache.annotations.Cache;

/**
 * Формула.
 * (A + корень(A * B) + корень(B * C) + C) / A*C - B^2)
 * Если значение под корнем меньше нуля, то будет исключение, которое должно лежать в кеше
 * также, как и при нулевом знаменателе.
 *
 * Если Вы считаете, что это извращение, то Вы не видел задач "Упростите выражение"
 * на вступительных экзаменах в МАИ и МИФИ в середине 80-х.
 */
public class WrokerThree extends WorkClass{
  @Override
  @Cache
  public double cal() throws InterruptedException {
    Thread.sleep(waiters);
    double divisible = getStay().getA();
    Thread.sleep(waiters);
    double tmp = getStay().getA();
    Thread.sleep(waiters);
    tmp *= getStay().getB();
    Thread.sleep(waiters);
    divisible += tmp;
    Thread.sleep(waiters);
    tmp = getStay().getB();
    Thread.sleep(waiters);
    tmp *= getStay().getC();
    Thread.sleep(waiters);
    divisible += tmp;
    Thread.sleep(waiters);
    divisible += getStay().getC();
    Thread.sleep(waiters);
    double divider = getStay().getA();
    Thread.sleep(waiters);
    divider *= getStay().getC();
    Thread.sleep(waiters);
    tmp = getStay().getB() * getStay().getB();
    Thread.sleep(waiters);
    divider -= tmp;
    Thread.sleep(waiters);
    double res = divisible / divider;
    Thread.sleep(waiters * 2);
    return res;
  }
}
