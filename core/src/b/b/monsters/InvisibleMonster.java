package b.b.monsters;

import b.b.core.Square;
import b.b.core.World;
import b.b.monsters.items.Item;

public abstract class InvisibleMonster extends Monster {
    public InvisibleMonster(World world, double x, double y) {
        super(world, x, y, world.gfx.getSprite("pix"), 0.000001, 4);
    }

    public void draw() {
    }

    public void move() {
    }

    abstract public void move(Monster m);

    protected void justDied() {
        world.removeFromMap(this);
        world.objectsToRemove.add(this);
    }

    protected boolean onSquare(Square o) {
        return false;
    }

    protected boolean onBullet(Bullet b) {
        return false;
    }

    protected boolean onMonster(Monster m) {
        return false;
    }

    protected boolean onItem(Item i) {
        return false;
    }

    protected boolean checkScreenCollision() {
        return false;
    }
}
