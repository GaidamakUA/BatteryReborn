package b.util;

import java.io.*;

public class Pair implements Serializable {
  public Object o1;
  public Object o2;

  public Pair(int o1, int o2) {
    this.o1=new Integer(o1);
    this.o2=new Integer(o2);
  }

  public Pair(Object o1, Object o2) {
    this.o1=o1;
    this.o2=o2;
  }
}
