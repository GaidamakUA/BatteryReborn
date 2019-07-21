package b.b.monsters.items;

import b.b.core.Config;
import b.b.core.World;
import b.b.monsters.Monster;
import b.b.monsters.Player;
import b.gfx.BufGfx;
import b.gfx.Sprite;

public class Coin extends Item {
    private double prevFrameTime;

    public Coin(double x, double y, World world) {
        super(world, x, y, world.g.getSprite("coin"));
        lvl = 3;
        prevFrameTime = -999;
    }

    public void draw() {
        double time = world.btr.time.time;
        Sprite sprite;
        if (prevFrameTime + Config.Monsters.Coin.lineDuration > time) {
            sprite = new Sprite("coin", world.g.getSprite("coin"), true);
            new BufGfx(sprite).effects().diagonal((time - prevFrameTime) /
                    Config.Monsters.Coin.lineDuration, 10, -5138687);
        } else {
            sprite = world.g.getSprite("coin");
            if (prevFrameTime + Config.Monsters.Coin.duration < time) {
                prevFrameTime = time;
            }
        }
        world.g.bufGfx.drawTranspRangeCheck(sprite, xScreenStart(), yScreenStart());
    }

    public boolean onMonster(Monster m) {
        if (life > 0 && m.life > 0 && (m instanceof Player)) {
            dmg(1, null);
            ((Player) m).incCoins();
            return true;
        }
        return false;
    }
}
