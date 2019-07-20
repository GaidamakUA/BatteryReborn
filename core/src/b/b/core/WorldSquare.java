package b.b.core;

import b.b.*;
import b.gfx.*;
import java.util.*;

public class WorldSquare {
  private Battery btr;
  public int x;
  public int y;
  public List<Drawable> objs;
  private BufGfx buf;

  public WorldSquare(int x, int y, Battery b) {
    btr = b;
    this.x = x;
    this.y = y;
    objs = new ArrayList<Drawable>();
    buf = null;
  }

  public void draw() {
    boolean drawn = false;
    if (buf == null || (buf != null && buf.w != 0)) {
      buf = new BufGfx(30, 30);
      Collections.sort(objs);
      for (Drawable d: objs) {
        if (d instanceof Square) {
          drawn = true;
          ((Square)d).draw2(buf.b);
        }
      }
    }
    if (drawn) {
      btr.gfx.b.drawRangeCheck(buf, x*30, (y*30)-btr.screen.camY());
    } else {
      buf = new BufGfx(0, 0);
    }
  }

  public void drop() {
    buf = null;
  }
}
