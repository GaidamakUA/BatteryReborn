package b.b.core.objs;

import b.b.Battery;
import b.b.core.Config;
import b.b.core.World;
import b.gfx.BufGfx;
import b.gfx.Sprite;
import b.gfx.effects.Appearing;
import b.util.U77;

public class Water extends ChanSquare {
    public Water(int x, int y, World w) {
        super(w.g.getSprite("water"), x, y, w, true, 0);
    }

    public void changeSprite() {
        sprite = world.g.getSprite("watcur");
    }

    public static void prepareWatCur(Battery btr) {
        Sprite watcur = btr.gfx.getSprite("watcur");
        Sprite water = btr.gfx.getSprite("water");
        int b[] = watcur.b;
        final int width = water.w;
        final int height = water.h;
        final double time = btr.time.time;
        System.arraycopy(water.b, 0, b, 0, width * height);

        /* x waves */
        int[] line = new int[width];
        for (int y = watcur.h - 1; y >= 0; y--) {
            int xShift = (int) (Math.sin(time / Config.Bcks.waterXWaveTimeK +
                    (((double) y / Config.Bcks.waterWave) / width) * U77.dpi) * 4);
            int offset = y * width + width - 1;
            for (int x = width - 1; x >= 0; x--) {
                line[U77.rem(x + xShift, width)] = b[offset--];
            }
            System.arraycopy(line, 0, b, y * width, width);
        }
    }

    public static void init(Battery btr) {
        Sprite watcur = btr.gfx.getSprite("watcur");
        BufGfx buf = new BufGfx(watcur);
        Appearing a = new Appearing(buf, 0, 0, watcur.w, watcur.h, false, true);
        btr.things.put("watcurAppearing", a);
    }
}
