package b.b.monsters;

import b.b.*;
import b.b.core.*;
import b.util.*;

public class InvisibleCannon extends InvisibleMonster {
  private int dir;
  private double lastShot;
  private Battery btr;

  public InvisibleCannon(World world, double x, double y, int dir) {
    super(world, x, y);
    this.dir=dir;
    btr=world.btr;
    lastShot=U77.rnd(Config.Monsters.Cannon.shotInterval)+btr.time.time;
    world.objsToAddInTime.add(this);
  }

  public void move() {
    if (lastShot+Config.Monsters.Cannon.shotInterval<btr.time.time) {
      btr.world.objsToAdd.add(new CannonBall(x+Config.hSquareSize,
          y+Config.hSquareSize, dir, btr.world, btr.screen));
      lastShot=btr.time.time;
    }
  }

  public void move(Monster m) {
    move();
  }
}
