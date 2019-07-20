package b.util;

public class Time77 {
  public static final int step=3;

  private static final int maxFPS=2500;

  public double time;
  public int fps;

  private long lastTime;
  private int frame;
  private double frameCounterStartTime;

  public Time77() {
    time=0;
    lastTime=System.currentTimeMillis();
    frame=0;
    frameCounterStartTime=time;
    fps=0;
  }

  public boolean step() {
    if (System.currentTimeMillis()-lastTime > step) {
      time += step;
      lastTime += step;
      return true;
    }
    return false;
  }

  public void nextFrame() {
    frame++;
    if (frame==maxFPS) {
      frame=0;
      fps=(int)((double)maxFPS*1000/(time-frameCounterStartTime));
      frameCounterStartTime=time;
    } else if (frame>49 && frame/50*50==frame) {
      fps=(int)((double)frame*1000/(time-frameCounterStartTime));
    }
  }
}
