package b.b.monsters.items;

import b.b.core.*;
import b.b.monsters.*;
import b.gfx.*;

public abstract class Item extends Monster {
  public Item(World world, double x, double y,
      Sprite sprite) {
    super(world, x, y, sprite, 0.000001);
  }

  public void draw() {
    world.g.b.drawTranspRangeCheck(sprite, xScreenStart(), yScreenStart());
  }

  protected void move() {}

  protected void justDied() {
    world.removeFromMap(this);
    world.objsToRemove.add(this);
  }

  public boolean onMonster(Monster m) {
    return false;
  }

  protected boolean onSquare(Square o) {
    return false;
  }

  protected boolean onBullet(Bullet b) {
    return false;
  }

  protected boolean onItem(Item i) {
    return false;
  }
}
