package b.b.monsters.bosses;

import b.b.Battery;
import b.b.core.Action;
import b.b.core.Config;
import b.b.core.World;
import b.b.gfx.Gfx;
import b.b.monsters.*;
import b.b.monsters.items.Coin;
import b.b.monsters.items.Explosion;
import b.gfx.BufGfx;
import b.gfx.Sprite;
import b.util.Pair;
import b.util.U77;

import java.util.Random;

public class Boss1AI extends InvisibleMonster implements ComplexAI {
    protected MonsterPart top;
    protected MonsterPart bottom;
    protected MonsterPart left;
    protected MonsterPart right;
    private Sprite topSprite;
    private Sprite bottomSprite;
    private Sprite leftSprite;
    private Sprite rightSprite;
    private double lastDraw;
    private Sprite core;
    private double timeWhenMoved = -1;
    private double angle1;
    private double angle2;
    private Gun topGun;
    private Gun rightGun;
    private Gun bottomGun;
    private Gun leftGun;

    public Boss1AI(double x, double y, World world) {
        super(world, x, y);
        initAnglesAndMover();
        lastDraw = -1;
        initSprites();
        initParts();
        initGuns();
    }

    private void initAnglesAndMover() {
        angle1 = U77.rnd(U77.dpi);
        angle2 = U77.rnd(U77.dpi);
        calcXY();
        mover = new Mover(this, Config.Monsters.Boss1.maxStrafeSpeed,
                Config.Monsters.Boss1.maxSpeed, Config.Monsters.Boss1.minSpeed);
        mover.zeroYSpeed = Config.cameraSpeed;
        mover.ySpeed = -Config.cameraSpeed;
        setWH(1, 1);
    }

    private void initSprites() {
        Gfx g = world.g;
        topSprite = g.getSprite("boss1_top");
        bottomSprite = g.getSprite("boss1_bottom");
        leftSprite = g.getSprite("boss1shield_left");
        rightSprite = g.getSprite("boss1shield_right");
        core = g.getSprite("boss1core");
    }

    private void initParts() {
        double life = Config.Monsters.Boss1.life;
        top = new MonsterPart(world, x, y - topSprite.h + 25, topSprite,
                life / 5 * 2, this, 4);
        bottom = new MonsterPart(world, x, y + bottomSprite.h - 5, bottomSprite,
                life / 5, this, 4);
        left = new MonsterPart(world, x - leftSprite.w + 1 - topSprite.hw, y, leftSprite,
                life / 5, this, 4);
        right = new MonsterPart(world, x + rightSprite.w - 1 + topSprite.hw, y, rightSprite,
                life / 5, this, 4);
        world.objsToAdd.add(bottom);
        world.addToMap(bottom);
        world.objsToAdd.add(top);
        world.addToMap(top);
        world.objsToAdd.add(left);
        world.addToMap(left);
        world.objsToAdd.add(right);
        world.addToMap(right);
    }

    private void initGuns() {
        topGun = new Gun(top, world, Config.Monsters.Boss1.shotInterval,
                Config.Monsters.Boss1.bulletSpeed, this);
        topGun.xShift = -10;
        topGun.yShift = 14;
        bottomGun = null;
        bottomGun = new Gun(bottom, world, Config.Monsters.Boss1.shotInterval,
                Config.Monsters.Boss1.bulletSpeed, this);
        bottomGun.xShift = 14;
        bottomGun.yShift = 14;
        leftGun = new Gun(left, world, Config.Monsters.Boss1.shotInterval,
                Config.Monsters.Boss1.bulletSpeed, this);
        leftGun.yShift = 11;
        rightGun = new Gun(right, world, Config.Monsters.Boss1.shotInterval,
                Config.Monsters.Boss1.bulletSpeed, this);
        leftGun.yShift = 11;
    }

    private void calcXY() {
        x += Math.sin(angle1) * 0.1 + (Math.sin(angle2) * 0.07);
        y += Math.cos(angle1) * 0.02 + (Math.cos(angle2) * 0.07);
    }

    protected void dmg(double dmg, double time, Object cause) {
    }

    public void draw() {
        if (lastDraw != time()) {
            lastDraw = time();
            BufGfx b = world.g.bufGfx;
            b.drawTranspRangeCheck(core, (int) (xScreenStart() - core.hw),
                    (int) (yScreenStart() - core.hh));
        }
    }

    public void move() {
        world.removeFromMap(this);
        if (timeWhenMoved != time()) {
            if (left.life <= 0 && top.life <= 0 && right.life <= 0 && bottom.life <= 0) {
                super.dmg(1, time(), world.btr.player);
                world.btr.timers.add(new Pair(new Double(time() +
                        Config.Intervals.afterBossPeriod), new Action() {
                    public void act(Battery btr) {
                        btr.timeWhenLevelCompleted = btr.time.time;
                    }
                }));
                world.objsToAdd.add(new Explosion(x, y, world, this, 4));
            } else {
                angle1 = U77.angle(angle1 + 0.01);
                angle2 = U77.angle(angle2 - 0.015);
                calcXY();
                if (angle1 > Math.PI && angle1 != 0) {
                    mover.left();
                } else {
                    mover.right();
                }
                if (yScreenStart() > 100) mover.up();
                if (yScreenStart() < 10) mover.down();
                if (x > world.btr.player.x && U77.rnd() < 0.2) mover.left();
                if (x < world.btr.player.x && U77.rnd() < 0.2) mover.right();
                mover.move();
                if (top.life > 0) topGun.shoot(2);
                if (bottom.life > 0) bottomGun.shoot(2);
                if (left.life > 0) leftGun.shoot(2);
                if (right.life > 0) rightGun.shoot(2);
            }
            timeWhenMoved = time();
        }
    }

    public void move(Monster m) {
        move();
        if (m.equals(top)) {
            top.x = x;
            top.y = y - topSprite.h + 25;
        } else if (m.equals(bottom)) {
            bottom.x = x;
            bottom.y = y + bottomSprite.h - 5;
        } else if (m.equals(left)) {
            left.x = x - leftSprite.w + 1 - topSprite.hw;
            left.y = y;
        } else {
            right.x = x + rightSprite.w - 1 + topSprite.hw;
            right.y = y;
        }
    }

    protected void justDied() {
        super.justDied();
        Random random = new Random(77);
        for (int i = 0; i < Config.Monsters.Boss1.rewardCoins; i++) {
            world.objsToAdd.add(new Coin(x - 50 + random.nextInt(100),
                    y - 50 + random.nextInt(100), world));
        }
    }
}
