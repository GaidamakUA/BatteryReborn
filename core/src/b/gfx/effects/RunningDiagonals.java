package b.gfx.effects;

import b.gfx.*;

public class RunningDiagonals {
  private BufGfx b;
  private int[] p;
  private int xStart;
  private int yStart;
  private int xBorder;
  private int yBorder;
  private int width;
  private double speed;

  public RunningDiagonals(BufGfx buf, int xStart, int yStart, int xBorder,
      int yBorder, int length) {
    b=buf;
    p=buf.b;
    this.xStart=xStart;
    this.yStart=yStart;
    this.xBorder=xBorder;
    this.yBorder=yBorder;
    width=length;
    speed=((xBorder-xStart) + (yBorder-yStart) + width)*2;
  }

  public void draw(double time) {
    diagonal((int)(speed*time)+xStart);
    diagonal((int)(speed*time-(speed/2))+xStart);
  }

  private void diagonal(int xxBorder) {
    for (int y=yStart; y<yBorder; y++) {
      vlineRangeCheck(xxBorder-width, y, xxBorder);
      xxBorder--;
    }
  }

  private void vlineRangeCheck(int x, int y, int xxBorder) {
    if (x<xStart) {
      x=xStart;
    }
    if (xxBorder>xBorder) {
      xxBorder=xBorder;
    }
    int offset=y*b.w + x;
    for (;x<xxBorder; x++) {
      int c=p[offset];
      if (c==0xff800000 || c==0xff954000 || c==0xffd982) p[offset]=0xffffff99;
      offset++;
    }
  }
}
