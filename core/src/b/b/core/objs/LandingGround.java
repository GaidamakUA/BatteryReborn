/* refactoring0 */
package b.b.core.objs;

import b.b.Battery;
import b.b.core.Config;
import b.b.core.World;
import b.gfx.BufGfx;
import b.gfx.Effects;
import b.gfx.Sprite;

public class LandingGround extends ChanSquare {
    private Battery btr;

    public LandingGround(int x, int y, World world) {
        super(world.gfx.getSprite("landingground"), (x + 1) * Config.squareSize,
                (y + 1) * Config.squareSize, world.gfx.getSprite("landingground").w,
                world.gfx.getSprite("landingground").h, world, false, 0);
        btr = world.gfx.battery;
    }

    protected void changeSprite() {
        sprite = new Sprite("lg", sprite, true);
        BufGfx b = new BufGfx(sprite);
        Effects eff = b.effects();
        if (!onPlayer()) {
            double k = Math.sin(btr.time.time / 500) / 2 + 0.5;
            /* antiflick */
            if (k < 0.04) k = 0.04;
            if (k > 0.96) k = 0.96;
            eff.dark(0xffdcdcdc, k);
            k = Math.cos(btr.time.time / 500) / 2 + 0.5;
            if (k < 0.04) k = 0.04;
            if (k > 0.96) k = 0.96;
            eff.dark(0xff808080, k);
        } else {
            eff.change(0xff808080, 0xffdcdcdc);
        }
    }

    public void act() {
        if (onPlayer()) {
            btr.player.bullets += Config.Monsters.Bullet.growth;
        }
    }
}
