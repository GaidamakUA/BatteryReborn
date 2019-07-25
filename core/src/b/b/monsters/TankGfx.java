package b.b.monsters;

import b.b.core.Config;
import b.b.core.World;
import b.gfx.BufGfx;
import b.gfx.Sprite;
import b.util.U77;

public class TankGfx {
    private Tank tankModel;
    private Sprite caterpillar;

    public TankGfx(Tank tank, Sprite caterpillar) {
        tankModel = tank;
        this.caterpillar = caterpillar;
    }

    protected final void draw(World w) {
        BufGfx b = w.gfx.bufGfx;
        drawCatters(b);
        tankModel.sprite = w.gfx.getSprite("tank_base");
        int xShift = 0;
        int yShift = 0;
        double time = tankModel.time() - tankModel.gun.lastShotTime;
        if (time < Config.Monsters.Tank.turretTime) {
            time = time / Config.Monsters.Tank.turretTime;
            if (tankModel.ai.turretDir == 0) {
                yShift = (int) (8 - (time * 8));
            } else if (tankModel.ai.turretDir == 1) {
                xShift = (int) (time * 8 - 8);
            } else if (tankModel.ai.turretDir == 2) {
                yShift = (int) (time * 8 - 8);
            } else {
                xShift = (int) (8 - (time * 8));
            }
        }
        Sprite turret = w.gfx.getSprite("tank_turret" + tankModel.ai.turretDir);
        if (tankModel.afterDmg()) {
            if (tankModel.afterWrongDmg()) {
                b.drawTranspBlackRangeCheck(tankModel.sprite, tankModel.xScreenStart(), tankModel.yScreenStart());
                b.drawTranspBlackRangeCheck(turret, tankModel.xScreenStart() + xShift,
                        tankModel.yScreenStart() + yShift);
            } else {
                b.drawTranspWhiteRangeCheck(tankModel.sprite, tankModel.xScreenStart(), tankModel.yScreenStart());
                b.drawTranspWhiteRangeCheck(turret, tankModel.xScreenStart() + xShift,
                        tankModel.yScreenStart() + yShift);
            }
        } else {
            b.drawTranspRangeCheck(tankModel.sprite, tankModel.xScreenStart(), tankModel.yScreenStart());
            b.drawTranspRangeCheck(turret, tankModel.xScreenStart() + xShift,
                    tankModel.yScreenStart() + yShift);
        }
    }

    private final void drawCatters(BufGfx b) {
        BufGfx cater = new BufGfx(caterpillar, true);
        int start = 3 - U77.rem((int) tankModel.dist, 4);
        int width = caterpillar.width - 2;
        for (int y = start; y < caterpillar.height; y += 4) {
            cater.hline(1, y, width, 0xff000000);
        }
        int xStart1 = tankModel.xScreenStart();
        int yStart1 = tankModel.yScreenStart();
        int xStart2 = tankModel.xScreenStart() + tankModel.sprite.width - cater.w;
        int yStart2 = tankModel.yScreenStart();
        if (tankModel.ai.direction == 1) {
            cater.rot90NotSquare(null);
            xStart2 = tankModel.xScreenStart();
            yStart2 = tankModel.yScreenStart() + tankModel.sprite.height - cater.h;
        } else if (tankModel.ai.direction == 2) {
            cater.flipVertical();
        } else if (tankModel.ai.direction == 3) {
            cater.flipVertical();
            cater.rot90NotSquare(null);
            xStart2 = tankModel.xScreenStart();
            yStart2 = tankModel.yScreenStart() + tankModel.sprite.height - cater.h;
        }
        if (tankModel.afterDmg()) {
            if (tankModel.afterWrongDmg()) {
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
