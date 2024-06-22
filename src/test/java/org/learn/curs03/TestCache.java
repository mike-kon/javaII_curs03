package org.learn.curs03;

import org.learn.curs03.cache.annotations.Cache;
import org.learn.curs03.cache.annotations.Mutator;

public class TestCache implements ITestCache{
  public static final String ExceptionMessage = "generate exception";
  private Integer value;
  private int count;
  private boolean wrong;

  public TestCache() {
    value = null;
    count = 0;
    wrong = false;
  }

  @Override
  @Mutator
  public void setValue(int value) {
    wrong = false;
    this.value = value;
  }

  @Override
  @Mutator
  public void setException(){
    wrong = true;
  }

  @Override
  @Cache
  public int getValue() throws Exception{
    count++;
    if (wrong) {
      throw new Exception (ExceptionMessage);
    } else {
      return value;
    }
  }

  @Override
  public int getCount() {
    return count;
  }
}
