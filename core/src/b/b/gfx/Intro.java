package b.b.gfx;

import java.util.*;
import b.gfx.*;
import b.gfx.effects.*;
import b.util.U77;

/**
 * Starts at time: 0
 */
public class Intro {
  public double duration;
  public double startTime;

  private final int waveHeight=10;
  private final int waveLength=40;
  private final int diagonalLength=120;
  
  /*
   * Including waves
   */
  private int xStart;
  private int yStart;
  private int width;
  private int height;

  private Sprite logo;
  private Gfx gfx;
  private Waves waves;
  private Appearing appearing;
  private RunningDiagonals diagonals;

  public Intro(Gfx gfx) {
    duration=7000;
    startTime=666666666;
    this.gfx=gfx;
    logo=gfx.getSprite("battery");
    xStart=gfx.b.w/2-(logo.w/2)-waveHeight-1;
    yStart=gfx.b.h/2-(logo.h/2)-waveHeight-1;
    width=waveHeight*2+logo.w+2;
    height=waveHeight*2+logo.h+2;
    waves=new Waves(gfx.b, xStart+waveHeight, yStart+waveHeight,
        logo.w+1, logo.h+1, waveLength, waveHeight, 4000);
    appearing=new Appearing(gfx.b, xStart, yStart, width, height, true, false);
    diagonals=new RunningDiagonals(gfx.b, xStart, yStart, xStart+width,
        yStart+height, diagonalLength);
  }

  public void draw(double time) {
    if (startTime==666666666) {
      startTime=time;
    }
    if (time<0) time=0;
    Arrays.fill(gfx.b.b, 0xff000000);
    gfx.b.drawAtCenter(logo);
    double timeK=(time-startTime)/duration;
    waves.draw(time, U77.k(timeK, 0.1));
    appearing.draw(timeK);
    diagonals.draw(U77.k2(timeK, 0.25));
    if (timeK>0.25 && timeK<0.75) {
      Sprite s=gfx.getSprite("copy");
      gfx.b.draw(s, gfx.w/2-(int)s.hw, gfx.h-50);
    }
  }
}
