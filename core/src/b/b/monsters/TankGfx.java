package b.b.monsters;

import b.b.core.Config;
import b.b.core.World;
import b.gfx.BufGfx;
import b.gfx.Sprite;
import b.util.Utils;

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
                b.drawTransparentBlackRangeCheck(tankModel.sprite, tankModel.xScreenStart(), tankModel.yScreenStart());
                b.drawTransparentBlackRangeCheck(turret, tankModel.xScreenStart() + xShift,
                        tankModel.yScreenStart() + yShift);
            } else {
                b.drawTransparentWhiteRangeCheck(tankModel.sprite, tankModel.xScreenStart(), tankModel.yScreenStart());
                b.drawTransparentWhiteRangeCheck(turret, tankModel.xScreenStart() + xShift,
                        tankModel.yScreenStart() + yShift);
            }
        } else {
            b.drawTransparentRangeCheck(tankModel.sprite, tankModel.xScreenStart(), tankModel.yScreenStart());
            b.drawTransparentRangeCheck(turret, tankModel.xScreenStart() + xShift,
                    tankModel.yScreenStart() + yShift);
        }
    }

    private final void drawCatters(BufGfx b) {
        Sprite caterpillarCopy = caterpillar.copy();
        int start = 3 - Utils.rem((int) tankModel.dist, 4);
        int width = caterpillar.width - 2;
        for (int y = start; y < caterpillar.height; y += 4) {
            caterpillarCopy.horizontalLine(1, y, width, 0xff000000);
        }
        int xStart1 = tankModel.xScreenStart();
        int yStart1 = tankModel.yScreenStart();
        int xStart2 = tankModel.xScreenStart() + tankModel.sprite.width - caterpillarCopy.width;
        int yStart2 = tankModel.yScreenStart();
        if (tankModel.ai.direction == 1) {
            caterpillarCopy.rot90NotSquare();
            xStart2 = tankModel.xScreenStart();
            yStart2 = tankModel.yScreenStart() + tankModel.sprite.height - caterpillarCopy.height;
        } else if (tankModel.ai.direction == 2) {
            caterpillarCopy.flipVertical();
        } else if (tankModel.ai.direction == 3) {
            caterpillarCopy.flipVertical();
            caterpillarCopy.rot90NotSquare();
            xStart2 = tankModel.xScreenStart();
            yStart2 = tankModel.yScreenStart() + tankModel.sprite.height - caterpillarCopy.height;
        }
        if (tankModel.afterDmg()) {
            if (tankModel.afterWrongDmg()) {
                b.drawTransparentBlackRangeCheck(caterpillarCopy.pixels, caterpillarCopy.width, caterpillarCopy.height, xStart1, yStart1);
                b.drawTransparentBlackRangeCheck(caterpillarCopy.pixels, caterpillarCopy.width, caterpillarCopy.height, xStart2, yStart2);
            } else {
                b.drawTransparentWhiteRangeCheck(caterpillarCopy, xStart1, yStart1);
                b.drawTransparentWhiteRangeCheck(caterpillarCopy, xStart2, yStart2);
            }
        } else {
            b.drawRangeCheck(caterpillarCopy, xStart1, yStart1);
            b.drawRangeCheck(caterpillarCopy, xStart2, yStart2);
        }
    }
}
