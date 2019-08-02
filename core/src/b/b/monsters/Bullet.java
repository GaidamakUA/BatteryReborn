package b.b.monsters;

import b.b.core.Config;
import b.b.core.Square;
import b.b.core.World;
import b.b.core.objs.Water;
import b.b.monsters.items.Item;

public class Bullet extends Monster {
    private int dir;
    public Object owner;

    public Bullet(double speed, double x, double y, int dir, World world, Object owner) {
        super(world, x, y, world.gfx.getSprite("bullet" + dir), 0.000001, ZLayer.FIVE);
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

    protected boolean onSquare(Square square) {
        if (square instanceof Water) {
            return false;
        }
        damage(1, null);
        return false;
    }

    protected boolean onBullet(Bullet bullet) {
        damage(1, bullet.owner);
        bullet.damage(1, owner);
        return false;
    }

    protected boolean onMonster(Monster monster) {
        if (monster instanceof MonsterPart) {
            return monster.onBullet(this);
        } else if (owner != monster) {
            damage(1, monster);
            monster.damage(Config.Damages.bullet, owner);
        }
        return false;
    }

    protected boolean onItem(Item i) {
        return false;
    }

    protected boolean checkScreenCollision() {
        if (outOfScreen()) {
            damage(1, null);
        }
        return false;
    }
}
