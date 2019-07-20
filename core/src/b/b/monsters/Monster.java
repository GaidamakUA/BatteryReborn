package b.b.monsters;

import java.util.*;
import b.gfx.*;
import b.b.core.*;
import b.b.core.objs.*;
import b.b.monsters.items.*;

public abstract class Monster extends Drawable implements Changeable {
  public double life;
  public double maxLife;

  protected double lastDmgTime;
  protected double wrongDmgTime;
  protected Sprite sprite;
  protected double dist;
  public Mover mover;

  public Monster(World world, double x, double y,
      Sprite sprite, double maxLife) {
    this(x, y, sprite.w, sprite.h, true, 4, maxLife,
        maxLife, world);
    this.sprite=sprite;
    mover=null;
  }

  private Monster(double x, double y, int width, int height,
      boolean isShape, int lvl, double life, double maxLife, World world) {
    super(x, y, width, height, isShape, lvl, world);
    this.life=life;
    this.maxLife=maxLife;
    lastDmgTime=-1000;
    wrongDmgTime=-1000;
    dist=0;
  }

  public final void act() {
    if (life > 0) {
      world.removeFromMap(this);
      move();
      int collisionCountdown=Config.Monsters.maxCollisionCount;
      while (checkCollision() && collisionCountdown>0) {
        collisionCountdown--;
      }
      if (collisionCountdown==0 && life!=0) {
        life=0;
        justDied();
      } else if (life>0) {
        world.addToMap(this);
      } else {
        world.objsToRemove.add(this);
      }
    }
  }

  protected void justDied() {
    world.removeFromMap(this);
    world.objsToRemove.add(this);
    world.objsToAdd.add(new Coin(x, y, world));
    if (!outOfScreen()) {
      world.objsToAdd.add(new Explosion(x, y, world, this));
    }
  }

  /**
   * Do not care about collisions and removing or adding to map.
   */
  protected abstract void move();

  public void dmg(double dmg, Object cause) {
    dmg(dmg, time(), cause);
  }

  protected void dmg(double dmg, double time, Object cause) {
    if (!afterDmg() && life>0) {
      life -= dmg;
      if (!(this instanceof Player) && world.btr.player.equals(cause)) {
        world.btr.player.incScores();
      }
      if (life<=0) {
        life=0;
        justDied();
      }
      lastDmgTime=time;
    } else if (cause instanceof Monster) {
      wrongDmgTime=time;
    }
  }

  /**
   * Checks if Monster ready for the next dmg.
   */
  public boolean afterDmg() {
    return time()-lastDmgTime<Config.Monsters.afterDmgTime;
  }

  public boolean afterWrongDmg() {
    return time()-wrongDmgTime<Config.Monsters.wrongDmgTime;
  }

  public void draw() {
    BufGfx b=world.g.b;
    if (afterDmg() || life==0) {
      if (afterWrongDmg()) {
        b.drawTranspBlackRangeCheck(sprite, xScreenStart(), yScreenStart());
      } else {
        b.drawTranspWhiteRangeCheck(sprite, xScreenStart(), yScreenStart());
      }
    } else {
      b.drawTranspRangeCheck(sprite, xScreenStart(), yScreenStart());
    }
  }

  public void draw(int xScreen, int yScreen, int xScreenBorder,
      int yScreenBorder) {
    throw new RuntimeException("Should not be");
  }

  /**
   * Object should be removed from map before the method call.
   * The method should not add it to map.
   * @return true if something moved
   */
  private boolean checkCollision() {
    if (checkScreenCollision()) {
      return true;
    }
    List<Drawable> objs=world.get(this);
    for (Drawable o: objs) {
      if (o.isShape) {
        if (!(o instanceof Monster) ||
            ((o instanceof Monster) && (((Monster)o).life>0))) {
          if (collision(o)) return true;
        }
      }
    }
    return false;
  }

  /**
   * Object should be removed from map before
   */
  protected boolean checkScreenCollision() {
    if (yStart()>screen.yEnd()) {
      dmg(maxLife, null);
      return true;
    }
    return false;
  }

  protected final boolean outOfScreen() {
    boolean res= (yStart()>screen.yEnd() || yEnd()<screen.camY() ||
        xStart()>screen.xEnd() || xEnd()<0);
    return res;
  }

  /**
   * @param o is not this. Be sure.
   * @return true if something removed
   */
  protected boolean collision(Drawable o) {
    if (((xStart()>=o.xStart() && xStart()<o.xEnd()) ||
        (o.xStart()>=xStart() && o.xStart()<xEnd())) &&
        ((yStart()>=o.yStart() && yStart()<o.yEnd()) ||
        (o.yStart()>=yStart() && o.yStart()<yEnd()))) {
      if (o instanceof Square) {
        return onSquare((Square)o);
      } else if (o instanceof Item) {
        return onItem((Item)o);
      } else if (o instanceof Bullet) {
        return onBullet((Bullet)o);
      } else if (o instanceof Monster) {
        return onMonster((Monster)o);
      }
      throw new RuntimeException("Unknown collision: "+getClass().getName());
    }
    return false;
  }

  protected boolean onSquare(Square o) {
    if (o instanceof Water) {
      return onWater((Water)o);
    }
    dmg(Config.Damages.square, null);
    removeFrom(o);
    return true;
  }

  protected boolean onWater(Water w) {
    return false;
  }

  protected boolean onBullet(Bullet b) {
    if ((b.owner != this) && (b != this)) {
      dmg(Config.Damages.bullet, b.owner);
      b.dmg(1, this);
    }
    return false;
  }

  protected boolean onMonster(Monster m) {
    if (m instanceof InvisibleMonster) return false;
    dmg(Config.Damages.monster, m);
    m.dmg(Config.Damages.monster, this);
    if (!afterDmg() && !m.afterDmg()) {
      return true;
    } else {
      return false;
    }
  }

  protected boolean onItem(Item i) {
    return i.onMonster(this);
  }

  /**
   * Rectangles should be overlapped
   */
  protected void removeFrom(Drawable o) {
    double xCenterDist=x - o.x;
    double yCenterDist=y - o.y;
    double xDist=hw+o.hw;
    double yDist=hh+o.hh;
    if (xDist-Math.abs(xCenterDist) < yDist-Math.abs(yCenterDist)) {
      if (xCenterDist<0) {
        x=o.x - xDist;
      } else {
        x=o.x + xDist;
      }
    } else {
      if (yCenterDist<0) {
        y=o.y - yDist;
      } else {
        y=o.y + yDist;
      }
    }
  }

  protected double time() {
    return world.g.btr.time.time;
  }
}
