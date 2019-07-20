package b.b.monsters.items;

import java.util.*;
import b.b.core.*;
import b.b.monsters.*;

public class Explosion extends Item {
  private static final double duration=370;
  private static final int count = 30;

  private List<Expl> list;
  private double time;
  protected int k;

  public Explosion(double x, double y, World world, Monster m) {
    this(x, y, world, m, 1);
  }

  public Explosion(double x, double y, World world, Monster m, int k) {
    super(world, x, y, world.g.getSprite("expl"));
    lvl=7;
    this.k=k-1;
    list=new ArrayList<Expl>();
    double xSpeed=0;
    double ySpeed=0;
    if (m.mover!=null) {
      xSpeed=m.mover.xSpeed;
      ySpeed=m.mover.ySpeed;
    }
    for (int i=0; i<count; i++) {
      list.add(new Expl(x, y, world, xSpeed, ySpeed, this));
    }
    time=time();
    setWH(1, 1);
  }

  public void draw() {
    double perc=1-(duration/(time()-time));
    for (Expl e: list) e.draw(perc);
  }

  protected void move() {
    y=world.btr.screen.camY()+(world.btr.screen.h/2);
    for (Expl e: list) e.move();
    if (time+duration<time()) {
      dmg(1, null);
    }
  }
}
