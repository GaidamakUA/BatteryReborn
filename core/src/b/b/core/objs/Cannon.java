package b.b.core.objs;

import b.b.core.Config;
import b.b.core.World;
import b.b.monsters.InvisibleCannon;

public class Cannon extends ChanSquare {

    public Cannon(int dir, int xMap, int yMap, World w) {
        super(w.gfx.getSprite("cannon" + dir), xMap, yMap, w, true, 3);
        new InvisibleCannon(w, xMap * Config.squareSize, yMap * Config.squareSize, dir);
    }

    public void draw() {
        System.out.println("draw");
        b.drawRangeCheck(sprite, (int) (x - hw),
                (int) (y - hh - screen.cameraY()) + chanSquareBonus());
    }

    public void changeSprite() {
    }
}
