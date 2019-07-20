package b.b.monsters.bosses;

import java.util.*;
import b.b.*;
import b.b.core.*;
import b.b.gfx.*;
import b.b.monsters.*;
import b.b.monsters.items.*;
import b.gfx.*;
import b.util.*;

public class Boss2AI extends InvisibleMonster implements ComplexAI {
  public static Boss2Well wellLeft = null;
  public static Boss2Well wellRight = null;
  public static boolean alreadyDrawn = false;

  private MonsterPart headLeft;
  private MonsterPart headRight;
  private Boss2Pane paneLeft;
  private Boss2Pane paneRight;
  private List<GunMonster> gunMonsters;
  private Sprite headLeftSprite;
  private Sprite headRightSprite;
  private Sprite paneLeftSprite;
  private Sprite paneRightSprite;
  private double timeWhenMoved=-1;
  private boolean left = true;
  private boolean headUp = true;
  private double horizYShift;
  private double headShift;
  private boolean headShiftOn = true;
  private boolean leftHeadDied = false;
  private boolean rightHeadDied = false;
  private boolean leftPaneDied = false;
  private boolean rightPaneDied = false;
  private boolean died = false;

  public Boss2AI(double x, double y, World world) {
    super(world, x, y);
    initMover();
    initSprites();
    initParts();
    initGuns();
  }

  private void initMover() {
    mover=new Mover(this, Config.Monsters.Boss2.maxStrafeSpeed,
        Config.Monsters.Boss2.maxSpeed, Config.Monsters.Boss2.minSpeed);
    mover.zeroYSpeed=Config.cameraSpeed;
    mover.ySpeed=-Config.cameraSpeed;
    setWH(1, 1);
    horizYShift = 0;
    headShift = 0;
  }

  private void initSprites() {
    Gfx g=world.g;
    headLeftSprite = g.getSprite("boss2_head_left");
    headRightSprite = g.getSprite("boss2_head_right");
    paneLeftSprite = g.getSprite("boss2_pane_left");
    paneRightSprite = g.getSprite("boss2_pane_right");
  }

  private void initParts() {
    int life = Config.Monsters.Boss2.paneLife;
    headLeft = new MonsterPart(world, x-headLeftSprite.w, y-(headLeftSprite.h/2),
        headLeftSprite, life, this, 5);
    headRight = new MonsterPart(world, x, y-(headRightSprite.h/2), headRightSprite,
        life, this, 5);
    paneLeft = new Boss2Pane(world, x-150-paneLeftSprite.w,
        y-(paneLeftSprite.h/2), paneLeftSprite, this);
    paneRight = new Boss2Pane(world, x+150, y-(paneRightSprite.h/2),
        paneRightSprite, this);
    wellLeft = new Boss2Well(world, this, null, true);
    wellRight = new Boss2Well(world, this, null, false);
    add(wellLeft, 12);
    add(wellRight, 12);
    addToWorld(headLeft);
    addToWorld(headRight);
    addToWorld(paneLeft);
    addToWorld(paneRight);
  }

  private final void add(Boss2Well well, int toAdd) {
    addToWorld(well);
    if (toAdd > 0) {
      Boss2Well w = new Boss2Well(world, this, well, well.left);
      well.child = w;
      add(w, toAdd-1);
    }
  }

  private final void addToWorld(MonsterPart part) {
    world.objsToAdd.add(part);
    world.addToMap(part);
  }

  private final void initGuns() {
    gunMonsters = new ArrayList<GunMonster>();
    initGun(-49);
    initGun(-49+9);
    initGun(-49+(9*2));
    initGun(-49+(9*3));
    initGun(-49+(9*4));
    initGun(11);
    initGun(11+9);
    initGun(11+(9*2));
    initGun(11+(9*3));
    initGun(11+(9*4));
  }

  private void initGun(int shift) {
    GunMonster gun = new GunMonster(world, this, shift);
    gunMonsters.add(gun);
    world.objsToAdd.add(gun);
    world.addToMap(gun);
  }

  protected void dmg(double dmg, double time, Object cause) {}

  public void draw() {
    if (!alreadyDrawn) {
      alreadyDrawn = true;
      BufGfx b=world.g.b;
      Sprite mainCarcasSprite = world.g.getSprite("boss2_main_carcas");
      b.drawTranspRangeCheck(mainCarcasSprite, (int)(xScreenStart() -
          mainCarcasSprite.hw), (int)(yScreenStart() - mainCarcasSprite.hh));
      verticals();
      horizontal();
      if (paneLeft.life > 0) {
        paneLeft.drawFromBoss();
      }
      if (paneRight.life > 0) {
        paneRight.drawFromBoss();
      }
      if (wellLeft != null) {
        wellLeft.drawFromBoss();
      }
      if (wellRight != null) {
        wellRight.drawFromBoss();
      }
    }
  }

  private void verticals() {
    BufGfx b = world.g.b;
    Sprite mainCarcasSprite = world.g.getSprite("boss2_main_carcas");
      Sprite vertCarcasSprite = world.g.getSprite("boss2_carcas_vert");
      b.drawRangeCheck(vertCarcasSprite, (int)(xScreenStart() - 25 - headShift),
          (int)(yScreenStart() - mainCarcasSprite.hh + 6));
      b.drawRangeCheck(vertCarcasSprite, (int)(xScreenStart() + 18 + headShift),
          (int)(yScreenStart() - mainCarcasSprite.hh + 6));
      Sprite cross = world.g.getSprite("boss2_carcas_cross");
      b.drawTranspRangeCheck(cross, (int)(xScreenStart() - 27 - headShift),
          (int)(yScreenStart() - mainCarcasSprite.hh - 2));
      b.drawTranspRangeCheck(cross, (int)(xScreenStart() + 16 + headShift),
          (int)(yScreenStart() - mainCarcasSprite.hh - 2));
  }

  private void horizontal() {
    BufGfx b = world.g.b;
    Sprite mainCarcasSprite = world.g.getSprite("boss2_main_carcas");
    b.drawRangeCheck(world.g.getSprite("boss2_carcas_horiz"),
        (int)(xScreenStart() - mainCarcasSprite.hw + 6),
        (int)(yScreenStart() - mainCarcasSprite.hh + 85 + horizYShift));
    Sprite cross = world.g.getSprite("boss2_carcas_cross");
    b.drawTranspRangeCheck(cross, (int)(xScreenStart() - mainCarcasSprite.hw - 2),
        (int)(yScreenStart() - mainCarcasSprite.hh + 83 + horizYShift));
    b.drawTranspRangeCheck(cross, (int)(xScreenStart() + mainCarcasSprite.hw - 8),
        (int)(yScreenStart() - mainCarcasSprite.hh + 83 + horizYShift));
    b.drawTranspRangeCheck(cross, (int)(xScreenStart() - 27 - headShift),
        (int)(yScreenStart() - mainCarcasSprite.hh + 83 + horizYShift));
    b.drawTranspRangeCheck(cross, (int)(xScreenStart() + 16 + headShift),
        (int)(yScreenStart() - mainCarcasSprite.hh + 83 + horizYShift));
  }

  public void move() {
    if (timeWhenMoved!=time() && !died) {
      world.removeFromMap(this);
      if (!leftHeadDied && headLeft.life <= 0) {
        leftHeadDied = true;
        for (int i=0; i<5; i++) {
          GunMonster gun = gunMonsters.get(i);
          if (gun.life > 0) {
            gun.dmg(100, world.btr.player);
          }
        }
      }
      if (!rightHeadDied && headRight.life <= 0) {
        rightHeadDied = true;
        for (int i=5; i<10; i++) {
          GunMonster gun = gunMonsters.get(i);
          if (gun.life > 0) {
            gun.dmg(100, world.btr.player);
            world.objsToRemove.add(gun);
            world.removeFromMap(gun);
          }
        }
      }
      if (!leftPaneDied && paneLeft.life <= 0) {
        leftPaneDied = true;
        if ((wellLeft != null) && (wellLeft.life > 0)) {
          dmg(wellLeft);
        }
        wellLeft = null;
      }
      if (!rightPaneDied && paneRight.life <= 0) {
        rightPaneDied = true;
        if ((wellRight != null) && (wellRight.life > 0)) {
          dmg(wellRight);
        }
        wellRight = null;
      }
      if (headRight.life<=0 && headRight.life<=0 &&
          paneLeft.life<=0 && paneRight.life<=0) {
        died = true;
        world.objsToRemove.add(this);
        world.objsToRemove.add(headLeft);
        world.objsToRemove.add(headRight);
        world.objsToRemove.add(paneLeft);
        world.objsToRemove.add(paneRight);
        if (wellLeft != null) {
          removeFromWorld(wellLeft);
        }
        if (wellRight != null) {
          removeFromWorld(wellRight);
        }
        for (GunMonster gun: gunMonsters) {
          world.objsToRemove.add(gun);
        }
        super.dmg(1, time(), world.btr.player);
        world.btr.timers.add(new Pair(new Double(time()+
            Config.Intervals.afterBossPeriod), new Action() {
          public void act(Battery btr) {
            btr.timeWhenLevelCompleted=btr.time.time;
          }
        }));
        world.objsToAdd.add(new Explosion(x, y, world, this, 4));
      } else {
        if (left) {
          mover.left();
        } else {
          mover.right();
        }
        headUpDown();
        headLeftRight();
        if (yScreenStart()>100) {
          mover.up();
        } else if (yScreenStart()<10) {
          mover.down();
        }
        if (x<50) {
          left = false;
          x=50;
        } else if (x>world.g.w-50) {
          left = true;
          x = world.g.w-50;
        }
        mover.move();
        for (GunMonster gun: gunMonsters) {
          if (gun.life > 0) {
            gun.shoot();
          }
        }
      }
      timeWhenMoved=time();
    }
  }

  private final void dmg(Boss2Well well) {
    well.dmg(100, world.btr.player);
    removeFromWorld(well);
    if (well.child != null) {
      dmg(well.child);
    }
  }

  private final void removeFromWorld(Boss2Well well) {
    world.objsToRemove.add(well);
    world.removeFromMap(well);
    if (well.child != null) {
      removeFromWorld(well.child);
    }
  }

  private final void headUpDown() {
    if (headUp) {
      horizYShift -= Config.Monsters.Boss2.headYSpeed;
      if (horizYShift < -35) {
        horizYShift = -35;
        headUp = false;
      }
    } else {
      horizYShift += Config.Monsters.Boss2.headYSpeed;
      if (horizYShift > 20) {
        horizYShift = 20;
        headUp = true;
      }
    }
  }

  private final void headLeftRight() {
    if (headShiftOn) {
      headShift += Config.Monsters.Boss2.headXSpeed;
      if (headShift > 15) {
        headShift = 15;
        headShiftOn = false;
      }
    } else {
      headShift -= Config.Monsters.Boss2.headXSpeed;
      if (headShift < 0) {
        headShift = 0;
        headShiftOn = true;
      }
    }
  }

  public void move(Monster m) {
    move();
    if (m.equals(headLeft)) {
      headLeft.x = (int)(x - 66 + headLeftSprite.hw -
          headShift);
      headLeft.y = y + 40 + horizYShift;
    } else if (m.equals(headRight)) {
      headRight.x = (int)(x + 2 + headRightSprite.hw +
          headShift);
      headRight.y = y + 40 + horizYShift;;
    } else if (m.equals(paneLeft)) {
      paneLeft.x = x - 167 + paneLeftSprite.hw;
      paneLeft.y=y+40;
    } else if (m.equals(paneRight)){
      paneRight.x= x + 31 + paneRightSprite.hw;
      paneRight.y=y+40;
    } else if (m instanceof Boss2Well) {
      Boss2Well well = (Boss2Well)m;
      well.setXY();
    } else {
      GunMonster gun = (GunMonster)m;
      if (inFirstFive(gun)) {
        gun.x = x + gun.shift - headShift;
      } else {
        gun.x = x + gun.shift + headShift;
      }
      gun.y = y + 86 + horizYShift;
    }
  }

  private final boolean inFirstFive(GunMonster gun) {
    for (int i=0; i<5; i++) {
      if (gunMonsters.get(i) == gun) return true;
    }
    return false;
  }

  protected void justDied() {
    super.justDied();
    Random random = new Random(77);
    for (int i=0; i<Config.Monsters.Boss2.rewardCoins; i++) {
      world.objsToAdd.add(new Coin(x-50+random.nextInt(100),
          y-50+random.nextInt(100), world));
    }
  }
}
