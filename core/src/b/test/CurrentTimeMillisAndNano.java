package b.test;

import b.util.*;

public class CurrentTimeMillisAndNano {
  public static void main(String[] args) {
    long current = System.currentTimeMillis();
    long nano = System.nanoTime();
    for (int i=0; i<100; i++) {
      while (true) {
        long c = System.currentTimeMillis();
        long n = System.nanoTime();
        if (c > current) {
          current = c;
          nano = n;
          P.p("c:"+current+" n/1000+:"+(nano/1000+1235206099170L)+" dif:"+
              (current-(nano/1000+1235206099170L)));
          break;
        } else if (n>nano) {
          current = c;
          nano = n;
          P.p("c:"+current+" n/1000+:"+(nano/1000+1235206099170L)+" dif:"+
              (current-(nano/1000+1235206099170L)));
          break;
        }
      }
    }
  }
}
