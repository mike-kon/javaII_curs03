package org.learn.curs03;

import org.learn.curs03.cache.Util;
import org.learn.curs03.fraction.Fractionable;
import org.learn.curs03.fraction.Fraction;

public class App
{
    public static void main( String[] args ) throws InterruptedException {
        Fraction fr = new Fraction(2,3);
        Fractionable num = Util.cache(fr);
        double d;
        d = num.doubleValue();
        System.out.printf("1: %1.2f%n", d);
        d = num.doubleValue();
        System.out.printf("2: %1.2f%n", d);
        Thread.sleep(1000);
        d = num.doubleValue();
        System.out.printf("3: %1.2f%n", d);
        num.setNum(5);
        num.setDenum(17);
        d = num.doubleValue();
        System.out.printf("4: %1.2f%n", d);
        d = num.doubleValue();
        System.out.printf("5: %1.2f%n", d);
        Thread.sleep(1100);
        d = num.doubleValue();
        System.out.printf("6: %1.2f%n", d);
        Util.shutdown();
    }
}
