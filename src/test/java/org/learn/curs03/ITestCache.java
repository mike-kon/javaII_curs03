package org.learn.curs03;

public interface ITestCache {
  public void setValue(int value);
  public void setException();
  public int getValue() throws Exception;
  public int getCount();
}
