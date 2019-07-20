package b.gfx.effects;

import b.gfx.*;

public class Waves {
  private final static double dpi=Math.PI*2;

  private BufGfx b;
  private int xStart;
  private int yStart;
  private int width;
  private int height;
  private double waveLength;
  private int waveHeight;
  private double period;
  private double time;

  public Waves(BufGfx buf, int xStart, int yStart, int width, int height,
      double waveLength, int waveHeight, double period) {
    b=buf;
    this.xStart=xStart;
    this.yStart=yStart;
    this.width=width;
    this.height=height;
    this.waveLength=waveLength;
    this.waveHeight=waveHeight;
    this.period=period;
  }

  public void draw(double time, double k) {
    this.time=time;
    double p=period/Math.PI/8;
    xWave(p*3, k*3/4, waveLength, false, 5);
    yWave(p*3, k*3/4, waveLength, false, 3);
    xWave(p, k/4, waveLength/2, true, 10);
    yWave(p, k/4, waveLength/2, true, 4);
  }

  private void xWave(double period, double k, double waveLength, boolean left,
        double shft) {
    int[] buf=b.b;
    for (int x=xStart-waveHeight-1; x<xStart+width+waveHeight; x++) {
      int shift;
      if (left) {
        double s1=Math.sin(((double)-x+shft+(time/period*waveLength))/waveLength*dpi);
        double s2=Math.sin((time+shft)/period);
        shift=(int)((s1+s2)*waveHeight*k/2);
      } else {
        double s1=Math.sin(((double)+x+shft+(time/period*waveLength))/waveLength*dpi);
        double s2=Math.sin((time+shft)/period);
        shift=(int)((s1+s2)*waveHeight*k/2);
      }
      if (shift < 0) {
        int yyStart=yStart-waveHeight-shift-1;
        int offset=yyStart*b.w+x;
        int offsetShift=shift*b.w;
        int yBorder=yStart+height+waveHeight;
        for (int y=yyStart; y<yBorder; y++) {
          try{
            buf[offset+offsetShift]=buf[offset];
          } catch(Exception ignored){}
          offset += b.w;
        }
      } else if (shift > 0) {
        int yyStart=yStart+height+waveHeight-shift;
        int offset=yyStart*b.w+x;
        int offsetShift=shift*b.w;
        int yBorder=yStart-waveHeight-shift;
        for (int y=yyStart; y>yBorder; y--) {
          try {
            buf[offset+offsetShift]=buf[offset];
          } catch(Exception ignored){}
          offset -= b.w;
        }
      }
    }
  }

  private void yWave(double period, double k, double waveLength, boolean left,
      double shft) {
    int[] buf=b.b;
    for (int y=yStart-waveHeight-1; y<yStart+height+waveHeight; y++) {
      int shift;
      if (left) {
        double s1=Math.sin(((double)-y+shft+(time/period*waveLength))/waveLength*dpi);
        double s2=Math.sin((time+shft)/period);
        shift=(int)((s1+s2)*waveHeight*k/2);
      } else {
        double s1=Math.sin(((double)+y+shft+(time/period*waveLength))/waveLength*dpi);
        double s2=Math.sin((time+shft)/period);
        shift=(int)((s1+s2)*waveHeight*k/2);
      }
      if (shift < 0) {
        int xxStart=xStart-waveHeight-shift-1;
        int offset=y*b.w+xxStart;
        int xBorder=xStart+width+waveHeight;
        for (int x=xxStart; x<xBorder; x++) {
          try {
            buf[offset+shift]=buf[offset++];
          } catch(Exception ignored){}
        }
      } else if (shift > 0) {
        int xxStart=xStart+width+waveHeight-shift;
        int offset=y*b.w+xxStart;
        int xBorder=xStart-waveHeight-shift;
        for (int x=xxStart; x>xBorder; x--) {
          try {
            buf[offset+shift]=buf[offset--];
          } catch(Exception ignored){}
        }
      }
    }
  }
}
