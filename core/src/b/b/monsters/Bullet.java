package b.b.monsters;

import b.b.core.Config;
import b.b.core.Square;
import b.b.core.World;
import b.b.core.objs.Water;
import b.b.monsters.items.Item;
import b.gfx.Screen;

public class Bullet extends Monster {
    private int dir;
    public Object owner;

    public Bullet(double speed, double x, double y, int dir, World world,
                  Screen screen, Object owner) {
        super(world, x, y, world.gfx.getSprite("bullet" + dir), 0.000001, 5);
        this.owner = owner;
        this.dir = dir;
        mover = new Mover(this, speed, speed, speed);
        mover.setSpeed(speed, dir);
    }

    public void draw() {
        world.gfx.bufGfx.drawRangeCheck(sprite, xScreenStart(), yScreenStart());
    }

    protected void move() {
        mover.move(dir);
        mover.move();
    }

    protected void justDied() {
        world.removeFromMap(this);
        world.objectsToRemove.add(this);
    }

    protected boolean onSquare(Square o) {
        if (o instanceof Water) return false;
        dmg(1, null);
        return false;
    }

    protected boolean onBullet(Bullet b) {
        dmg(1, b.owner);
        b.dmg(1, owner);
        return false;
    }

    protected boolean onMonster(Monster m) {
        if (m instanceof MonsterPart) {
            return m.onBullet(this);
        } else if (owner != m) {
            dmg(1, m);
            m.dmg(Config.Damages.bullet, owner);
        }
        return false;
    }

    protected boolean onItem(Item i) {
        return false;
    }

    protected boolean checkScreenCollision() {
        if (outOfScreen()) dmg(1, null);
        return false;
    }
}
