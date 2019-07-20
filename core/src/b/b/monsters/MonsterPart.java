package b.b.monsters;

import b.b.core.*;
import b.b.monsters.items.*;
import b.gfx.*;

public class MonsterPart extends Monster {
  protected ComplexAI ai;

  public MonsterPart(World world, double x, double y, Sprite s, double life,
      ComplexAI ai, int plvl) {
    super(world, x, y, s, life*Config.Damages.bullet);
    this.ai=ai;
    lvl = plvl;
  }

  public void move() {
    ai.move(this);
  }

  public void draw() {
    ai.draw();
    super.draw();
  }

  protected void dmg(double dmg, double time, Object cause) {
    if (!afterDmg() && life>0 && !ai.equals(cause)) {
      life -= dmg;
      if (world.btr.player.equals(cause)) {
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

  protected void justDied() {
    world.removeFromMap(this);
    world.objsToRemove.add(this);
    if (!outOfScreen()) {
      world.objsToAdd.add(new Explosion(x, y, world, this));
    }
  }

  protected boolean onMonster(Monster m) {
    if (m == world.btr.player) {
      return super.onMonster(m);
    } else {
      return false;
    }
  }

  protected boolean onBullet(Bullet b) {
    if (b.owner == ai) {
      return false;
    } else {
      return super.onBullet(b);
    }
  }
}
