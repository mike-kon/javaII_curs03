package org.learn.curs03.worker;

import lombok.Getter;
import org.learn.curs03.cache.annotations.Mutator;

import java.util.Objects;

/**
 * Этот класс долго рассчитывает некоторый показатель.
 * Показатель рассчитывается из трех величин.

 * В знаменателе может быть 0 и это должно нормально обрабатываться (исключение).
 * В метод расчета специально вставлены задержки.
 * Предполагается, что метод рассчета кешируется.
 * Ошибка DivideByZero должно кешироваться.
 */
public abstract class WorkClass {
  protected  final long waiters = 500;

  @Getter
  private final WorkerStay stay;

  public WorkClass() {
    this.stay = new WorkerStay();
  }

  @Mutator
  public void setA(double value){
    stay.setA(value);
  }

  @Mutator
  public void setB(double value){
    stay.setB(value);
  }

  @Mutator
  public void setC(double value){
    stay.setC(value);
  }

  public abstract double cal() throws InterruptedException;

}
