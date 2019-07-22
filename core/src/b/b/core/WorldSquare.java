package b.b.core;

import b.b.Battery;
import b.gfx.BufGfx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorldSquare {
    private Battery btr;
    private final int x;
    private final int y;
    public final List<Drawable> objects = new ArrayList<Drawable>();
    private BufGfx buf;

    public WorldSquare(int x, int y, Battery b) {
        btr = b;
        this.x = x;
        this.y = y;
        buf = null;
    }

    public void draw() {
        boolean drawn = false;
        if (buf == null || (buf != null && buf.w != 0)) {
            buf = new BufGfx(30, 30);
            Collections.sort(objects);
            for (Drawable d : objects) {
                if (d instanceof Square) {
                    drawn = true;
                    ((Square) d).draw2(buf.pixels);
                }
            }
        }
        if (drawn) {
            btr.gfx.bufGfx.drawRangeCheck(buf, x * 30, (y * 30) - btr.screen.camY());
        } else {
            buf = new BufGfx(0, 0);
        }
    }

    public void drop() {
        buf = null;
    }
}
