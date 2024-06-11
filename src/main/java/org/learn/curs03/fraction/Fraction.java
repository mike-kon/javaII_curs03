package org.learn.curs03.fraction;

import org.learn.curs03.cache.Cache;
import org.learn.curs03.cache.Mutator;

public class Fraction implements Fractionable{
  private int num;
  private int denum;

  public Fraction(int num, int denum) {
    this.num = num;
    this.denum = denum;
  }

  @Cache(1000)
  @Override
  public double doubleValue() {
    System.out.println("invoke double value");
    return (double) num/denum;
  }

  @Mutator
  @Override
  public void setNum(int num) {
    this.num = num;
  }

  @Mutator
  @Override
  public void setDenum(int denum) {
    this.denum = denum;
  }

}
