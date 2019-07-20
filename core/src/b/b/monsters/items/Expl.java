package b.b.monsters.items;

import b.b.core.*;
import b.util.*;

public class Expl extends Item {
  public final static double maxSpeed=0.27;
  private double xSpeed;
  private double ySpeed;
  private Explosion expl;

  public Expl(double x, double y, World world, double xsp, double ysp,
      Explosion expl) {
    super(world, x, y, world.g.getSprite("expl"));
    this.expl=expl;
    lvl=5;
    double speed=U77.rnd(maxSpeed*0.4)+(maxSpeed*0.6);
    double angle=U77.rnd(U77.dpi);
    xSpeed=(Math.sin(angle)*speed+(xsp*0.6))*Time77.step;
    ySpeed=(Math.cos(angle)*speed+(ysp*0.6))*Time77.step;
    move();
    move();
    move();
  }

  protected void move() {
    x+=xSpeed;
    y+=ySpeed;
    if (expl.k>0 && U77.rnd()>0.999) {
      int k=expl.k/2;
      expl.k-=k;
      world.objsToAdd.add(new Explosion(x, y, world, this, k));
    }
  }

  public void draw(double perc) {
    world.g.b.drawTranspRangeCheck(sprite, xScreenStart(), yScreenStart());
  }
}
