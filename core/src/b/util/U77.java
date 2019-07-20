package b.util;

import java.util.*;

public class U77 {
  public static final double hhpi=Math.PI/4;
  public static final double hpi=Math.PI/2;
  public static final double pi34=Math.PI*3/2;
  public static final double dpi=Math.PI*2;

  private static Random rnd=new Random(77);

  public static final void dropRandom() {
    rnd=new Random(77);
  }

  /**       ^k
   *\       |       /
   * \      |      /
   *  \     |     /
   *   \____|____/
   *----------------> kk
   *0   |>center<|   1
   */
  public static final double k(double kk, double center) {
    double k=(kk <= 0.5) ? kk : 1-kk;
    center /= 2;
    return (k>0.5-center) ? 0 : (0.5-center-k)/(0.5-center);
  }

  /**            ^k
   *            _|1
   *          _/ |
   *        _/   |
   *      _/     |
   *-----/----------> kk
   *0   |>center<|   1
   */
  public static final double k2(double kk, double center) {
    kk -= (0.5-(center/2));
    return (kk<0 || kk>center) ? -1 : kk/center;
  }

  public static final double dist(double x, double y) {
    return Math.sqrt(x*x + (y*y));
  }

  /**
   * @return normailized angle
   */
  public static final double angle(double a) {
    return rem(a, dpi);
  }

  public static final double angle(double x, double y) {
    return Math.atan2(x, y);
  }

  public static final double aangle(double x, double y) {
    double a=Math.atan2(x, y);
    while (a<0) a+=dpi;
    while (a>=dpi) a-=dpi;
    return a;
  }

  /**
   * from a0 to a1
   */
  public static final double closest(double a0, double a1) {
    if (a0>=a1) {
      double res=a0-a1;
      return res>Math.PI?dpi-res:-res;
    } else {
      double res=a1-a0;
      return res<Math.PI?res:res-dpi;
    }
  }

  public static final double closestpos(double a0, double a1) {
    double res=Math.abs(a0-a1);
    return res>Math.PI ? dpi-res : res;
  }

  /**
   * y=k*x + b
   * k=1/tan(a)
   * b=y - k*x
   */
  public static final double[] kb(double x, double y, double a) {
    double k=1/Math.tan(a);
    return new double[] {k, -k*x+y};
  }

  /**
   * (-1.1, 5)=3.9
   * (1.1, 5)=1.1
   */
  public static final double rem(double a, double b) {
    double r=a%b;
    if (r<0) r += b;
    return (r==b)?0:r;
  }

  public static final boolean brem(double a, double b) {
    return rem(a, b)==0;
  }

  /**
   * (-1, 5)=4
   * (4, 5)=4
   * (5, 5)=0
   * (101, 5)=1
   */
  public static final int rem(int a, int b) {
    int r=a%b;
    if (r<0) r += b;
    return (r==b)?0:r;
  }

  public static final boolean brem(int a, int b) {
    return rem(a, b)==0;
  }

  public static final double rnd() {
    return rnd.nextDouble();
  }

  public static final boolean rndBool() {
    return rnd.nextBoolean();
  }

  public static final double rnd(double max) {
    return rnd.nextDouble()*max;
  }

  public static final double rnd(double min, double max) {
    return rnd.nextDouble()*(max-min)+min;
  }

  public static final String leadZeros(String s, int length) {
    if (s.startsWith("-")) return "-"+repeat("0", length-s.length())+
        s.substring(1);
    return repeat("0", length-s.length())+s;
  }

  public static final String sprecision(double d) {
    return "" + (d==(long)d?""+(long)d:d);
  }

  public static final String sprecision(double d, int afterDot) {
    int k=1;
    for (int i=0;i<afterDot;i++) k*=10;
    d*=k;
    d=((double)(int)d)/k;
    return d==(int)d ? ""+(int)d : ""+d;
  }

  /**
   * @param afterDot - obligatory digit count after dot
   */
  public static final String ssprecision(double d, int afterDot) {
    if (afterDot==0) return ""+(int)(d+0.5);
    String res=sprecision(d, afterDot);
    if (res.indexOf(".")==-1) {
      return res+"."+repeat("0", afterDot);
    } else {
      int aftDot=res.length()-1-res.indexOf(".");
      if (aftDot<afterDot) {
        return res+repeat("0", afterDot-aftDot);
      } else if (aftDot>afterDot) {
        return res.substring(0, res.length()+afterDot-aftDot);
      } else {
        return res;
      }
    }
  }

  public static final String spaces(int count) {
    return repeat(" ", count);
  }

  public static final String nbsps(int count) {
    return repeat("&nbsp;", count);
  }

  public static final String repeat(String s, int count) {
    String res="";
    for (int i=0; i<count; i++) res += s;
    return res;
  }

  public static final void throwException(Exception e) {
    if (e instanceof RuntimeException) throw (RuntimeException)e;
    throw new RuntimeException(toString(e));
  }

  public static final void throwException(Exception e, String msg) {
    throw new RuntimeException(msg+" "+toString(e));
  }

  public static final String toString(Exception e) {
    String res="" + e.getClass().getName() + " " + e.getMessage();
    StackTraceElement[] stack=e.getStackTrace();
    for (int i=0; i<stack.length; i++) {
      res +=  " | " + stack[i];
    }
    return res+"\n";
  }

  public static int maxIndex(int[] arr) {
    int max=0;
    for (int i=1; i<arr.length; i++) {
      if (arr[i] > arr[max]) {
        max=i;
      }
    }
    return max;
  }
}
