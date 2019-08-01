package b.b.monsters;

import b.b.core.Config;
import b.b.core.Square;
import b.b.core.World;
import b.b.core.objs.Cannon;
import b.b.core.objs.Water;
import b.b.monsters.items.Item;
import b.gfx.Screen;

public class CannonBall extends Monster {
    private int dir;

    public CannonBall(double x, double y, int dir, World world,
                      Screen screen) {
        super(world, x, y, world.gfx.getSprite("cannon_ball"), 0.000001, ZLayer.TWO);
        this.dir = dir;
        double speed = Config.Monsters.Cannon.ballSpeed;
        mover = new Mover(this, speed, speed, speed);
        mover.setSpeed(speed, dir);
    }

    public void draw() {
        world.gfx.bufGfx.drawTransparentRangeCheck(sprite, xScreenStart(), yScreenStart());
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
        if (square instanceof Water || square instanceof Cannon) {
            return false;
        }
        damage(1, false);
        return false;
    }

    protected boolean onBullet(Bullet bullet) {
        damage(1, true);
        bullet.damage(1, true);
        return false;
    }

    protected boolean onMonster(Monster monster) {
        damage(1, true);
        monster.damage(Config.Damages.cannonBall, true);
        return false;
    }

    protected boolean onItem(Item i) {
        return false;
    }

    protected boolean checkScreenCollision() {
        if (outOfScreen()) {
            damage(1, false);
        }
        return false;
    }
}
