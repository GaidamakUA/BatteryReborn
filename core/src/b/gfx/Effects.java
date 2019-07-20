package b.gfx;

import java.util.Random;
import b.b.core.*;

public class Effects {
  private BufGfx b;

  protected Effects(BufGfx buf) {
    b=buf;
  }

  public final void diagonal(double part, int length, int c) {
    int color = C.light(c, 0.5);
    int start = (int)(part*(length+length+b.w))-length;
    int border = start+length;
    for (int x=start; x<border; x++) {
      hline(x, c, color);
    }
  }

  public final void flipVertical() {
    int[] res = new int[b.h*b.w];
    for (int yy=0; yy<b.h; yy++) {
      System.arraycopy(b.b, b.w*yy, res, (b.h-1)*b.w-(yy*b.w), b.w);
    }
    System.arraycopy(res, 0, b.b, 0, b.w*b.h);
  }

  private final void hline(int x, int c, int color) {
    int[] p=b.b;
    if (x>=0 && x<b.w) {
      int index = x;
      for (int y=0; y<b.h; y++) {
        if (p[index] == c) {
          p[index] = color;
        }
        index += b.w;
      }
    }
  }

  public final void dark(int c, double dark) {
    int[] p=b.b;
    int cc=C.dark(c, dark);
    for (int i=p.length-1; i>=0; i--) {
      if (p[i]==c) p[i]=cc;
    }
  }

  public final void change(int c0, int c1) {
    int[] p = b.b;
    for (int i=p.length-1; i>=0; i--) {
      if (p[i] == c0) p[i] = c1;
    }
  }

  public final void dirt(int c, int randomSeed) {
    dirt(c, randomSeed, Config.Gfx.dirtK);
  }

  public final void dirt(int c, int randomSeed, double k) {
    Random r=new Random(randomSeed);
    int[] p=b.b;
    double rest=1-k;
    for (int i=p.length-1; i>=0; i--) {
      if (p[i]==c) {
        p[i]=C.dark(c, r.nextDouble()*k+rest);
      }
    }
  }

  public final void dirt(int randomSeed) {
    Random r=new Random(randomSeed);
    int[] p=b.b;
    for (int i=p.length-1; i>=0; i--) {
      p[i]=C.dark(p[i], r.nextDouble()*Config.Gfx.dirtK+Config.Gfx.restDirtK);
    }
  }

  public final void red(int c, double red) {//todo ispoljzovatj etot effekt onLandingGround
    int[] p=b.b;
    int[] rgb=C.rgb(c);
    int cc=C.c((int)(red*255), rgb[1], rgb[2]);
    for (int i=p.length-1; i>=0; i--) {
      if (p[i]==c) p[i]=cc;
    }
  }

  public final void inv() {
    int[] p=b.b;
    int c;
    for (int i=p.length-1; i>=0; i--) {
      c=p[i];
      if (c!=0xffffffff && c!=0xff000000) p[i]=C.inv(c);
    }
  }
}
