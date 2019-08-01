package b.b.monsters.items;

import b.b.core.Square;
import b.b.core.World;
import b.b.monsters.Bullet;
import b.b.monsters.Monster;
import b.gfx.Sprite;

public abstract class Item extends Monster {
    public Item(World world, double x, double y, Sprite sprite, ZLayer zLayer) {
        super(world, x, y, sprite, 0.000001, zLayer);
    }

    public void draw() {
        world.gfx.bufGfx.drawTransparentRangeCheck(sprite, xScreenStart(), yScreenStart());
    }

    protected void move() {
    }

    protected void justDied() {
        world.removeFromMap(this);
        world.objectsToRemove.add(this);
    }

    public boolean onMonster(Monster monster) {
        return false;
    }

    protected boolean onSquare(Square square) {
        return false;
    }

    protected boolean onBullet(Bullet bullet) {
        return false;
    }

    protected boolean onItem(Item i) {
        return false;
    }
}
