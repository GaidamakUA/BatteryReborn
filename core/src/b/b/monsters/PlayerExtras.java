package b.b.monsters;

import b.b.core.*;

public class PlayerExtras {
  public int immortalities = 0;
  public double immortalityStart = -99999999;

  public boolean immortal(double time) {
    return immortalityStart + Config.Intervals.immortality > time;
  }
}
