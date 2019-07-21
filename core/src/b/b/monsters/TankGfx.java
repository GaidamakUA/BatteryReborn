package b.b.monsters;

import b.b.core.Config;
import b.b.core.World;
import b.gfx.BufGfx;
import b.gfx.Sprite;
import b.util.U77;

public class TankGfx {
    private Tank t;
    private Sprite caterpillar;

    public TankGfx(Tank tank, Sprite caterpillar) {
        t = tank;
        this.caterpillar = caterpillar;
    }

    protected final void draw(World w) {
        BufGfx b = w.g.bufGfx;
        drawCatters(b);
        t.sprite = w.g.getSprite("tank_base");
        int xShift = 0;
        int yShift = 0;
        double time = t.time() - t.gun.lastShotTime;
        if (time < Config.Monsters.Tank.turretTime) {
            time = time / Config.Monsters.Tank.turretTime;
            if (t.ai.turretDir == 0) {
                yShift = (int) (8 - (time * 8));
            } else if (t.ai.turretDir == 1) {
                xShift = (int) (time * 8 - 8);
            } else if (t.ai.turretDir == 2) {
                yShift = (int) (time * 8 - 8);
            } else {
                xShift = (int) (8 - (time * 8));
            }
        }
        Sprite turret = w.g.getSprite("tank_turret" + t.ai.turretDir);
        if (t.afterDmg()) {
            if (t.afterWrongDmg()) {
                b.drawTranspBlackRangeCheck(t.sprite, t.xScreenStart(), t.yScreenStart());
                b.drawTranspBlackRangeCheck(turret, t.xScreenStart() + xShift,
                        t.yScreenStart() + yShift);
            } else {
                b.drawTranspWhiteRangeCheck(t.sprite, t.xScreenStart(), t.yScreenStart());
                b.drawTranspWhiteRangeCheck(turret, t.xScreenStart() + xShift,
                        t.yScreenStart() + yShift);
            }
        } else {
            b.drawTranspRangeCheck(t.sprite, t.xScreenStart(), t.yScreenStart());
            b.drawTranspRangeCheck(turret, t.xScreenStart() + xShift,
                    t.yScreenStart() + yShift);
        }
    }

    private final void drawCatters(BufGfx b) {
        BufGfx cater = new BufGfx(caterpillar, true);
        int start = 3 - U77.rem((int) t.dist, 4);
        int width = caterpillar.w - 2;
        for (int y = start; y < caterpillar.h; y += 4) {
            cater.hline(1, y, width, 0xff000000);
        }
        int xStart1 = t.xScreenStart();
        int yStart1 = t.yScreenStart();
        int xStart2 = t.xScreenStart() + t.sprite.w - cater.w;
        int yStart2 = t.yScreenStart();
        if (t.ai.dir == 1) {
            cater.rot90NotSquare(null);
            xStart2 = t.xScreenStart();
            yStart2 = t.yScreenStart() + t.sprite.h - cater.h;
        } else if (t.ai.dir == 2) {
            cater.flipVertical();
        } else if (t.ai.dir == 3) {
            cater.flipVertical();
            cater.rot90NotSquare(null);
            xStart2 = t.xScreenStart();
            yStart2 = t.yScreenStart() + t.sprite.h - cater.h;
        }
        if (t.afterDmg()) {
            if (t.afterWrongDmg()) {
                b.drawTranspBlackRangeCheck(cater.pixels, cater.w, cater.h, xStart1, yStart1);
                b.drawTranspBlackRangeCheck(cater.pixels, cater.w, cater.h, xStart2, yStart2);
            } else {
                b.drawTranspWhiteRangeCheck(cater, xStart1, yStart1);
                b.drawTranspWhiteRangeCheck(cater, xStart2, yStart2);
            }
        } else {
            b.drawRangeCheck(cater, xStart1, yStart1);
            b.drawRangeCheck(cater, xStart2, yStart2);
        }
    }
}
