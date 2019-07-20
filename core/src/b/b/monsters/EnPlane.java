package b.b.monsters;

import b.b.core.*;
import b.b.core.objs.*;
import b.util.*;

public class EnPlane extends Monster {
  private double angle;

  public EnPlane(double x, double y, World world) {
    super(world, x, y, world.g.getSprite("enplane"),
        Config.Monsters.EnPlane.life*Config.Damages.bullet);
    lvl=5;
    final double speed=Config.Monsters.EnPlane.speed;
    mover=new Mover(this, Config.Monsters.EnPlane.shiftSpeed, 0, speed);
    mover.setSpeed(speed, 2);
    angle=U77.rnd(U77.dpi);
  }

  protected void move() {
    angle=U77.angle(angle+Config.Monsters.EnPlane.turnSpeed);
    if (angle<Math.PI) mover.move(3); else mover.move(1);
    mover.move(2);
    mover.move();
  }

  public void draw() {
    super.draw();
  }

  protected boolean onMonster(Monster m) {
    if (m instanceof Tank) return false;
    if (m instanceof EnPlane) {
      removeFrom(m);
      return true;
    }
    return super.onMonster(m);
  }

  protected boolean onSquare(Square o) {
    if (o instanceof Water) return false;
    removeFrom(o);
    return true;
  }
}
