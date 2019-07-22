package b.b.monsters;

import b.b.Battery;
import b.b.core.Config;
import b.b.core.Drawable;
import b.b.core.Keyboard77;
import b.b.core.World;
import b.gfx.BufGfx;
import b.util.U77;

public class Player extends Monster {
    public int lifes;
    private int scores;
    public double bullets;
    public PlayerExtras extras;
    private Keyboard77 kbd;
    private double lastShotTime;
    private int coins;

    public Player(Keyboard77 keyboard, World world, int pscores, int plifes,
                  int pcoins, PlayerExtras pextras) {
        super(world, (double) world.width / 2 * Config.squareSize,
                (world.height - 3) * Config.squareSize, world.gfx.getSprite("plane"),
                Config.Monsters.Player.life * Config.Damages.bullet);
        scores = pscores;
        lifes = plifes;
        coins = pcoins;
        extras = pextras;
        screen = world.btr.screen;
        screen.setCameraY(world.height * Config.squareSize - screen.h, world);
        kbd = keyboard;
        lvl = 5;
        lastShotTime = 0;
        mover = new Mover(this, Config.Monsters.Player.maxStrafeSpeed,
                Config.Monsters.Player.maxSpeed, Config.Monsters.Player.minSpeed);
        mover.zeroYSpeed = Config.cameraSpeed;
        mover.ySpeed = -Config.cameraSpeed;
        bullets = Config.Monsters.Player.bullets;
    }

    public int getCoins() {
        return coins;
    }

    public void incCoins() {
        coins++;
    }

    public int getScores() {
        return scores;
    }

    public void minusCoins(int pcoins) {
        coins -= pcoins;
    }

    public void incScores() {
        scores++;
        world.btr.logger.log("scores " + scores + " " + U77.sprecision(time()));
    }

    public void initXY() {
        x = (double) world.width / 2 * Config.squareSize;
        y = (world.height - 3) * Config.squareSize;
    }

    protected void move() {
        if (kbd.left()) mover.left();
        if (kbd.right()) mover.right();
        if (kbd.up()) mover.up();
        if (kbd.down()) mover.down();
        if (kbd.f1() && extras.immortalities > 0) {
            extras.immortalityStart = world.btr.time.time;
            extras.immortalities--;
        }
        mover.move();
        if (kbd.ctrl() || kbd.space()) shoot();
    }

    protected void justDied() {
        super.justDied();
        lifes--;
        Battery btr = world.gfx.battery;
        btr.timeWhenLevelCompleted = time();
        btr.logger.log("gameover " + U77.sprecision(time()));
        if (lifes == 0) {
            btr.logger.log("GAMEOVER");
        }
    }

    public boolean afterDmg() {
        return time() - lastDmgTime < Config.Monsters.Player.afterDmgTime;
    }

    public void draw() {
        BufGfx b = world.gfx.bufGfx;
        if (extras.immortal(world.btr.time.time)) {
            b.drawTransp(world.gfx.getSprite("immune"), xScreenStart(), yScreenStart());
        } else super.draw();
    }

    protected void dmg(double dmg, double time, Object cause) {
        if (!extras.immortal(world.btr.time.time)) {
            super.dmg(dmg, time, cause);
        }
    }

    protected boolean collision(Drawable o) {
        if (extras.immortal(world.btr.time.time)) {
            return false;
        } else {
            return super.collision(o);
        }
    }

    protected boolean checkScreenCollision() {
        boolean moved = false;
        if (xStart() < 0) {
            x = hw;
            moved = true;
        } else if (xEnd() > screen.xEnd()) {
            x = screen.xEnd() - hw;
            moved = true;
        }
        if (yStart() < screen.camY()) {
            y = screen.camY() + hh;
            moved = true;
        } else if (yEnd() > screen.yEnd()) {
            y = screen.yEnd() - hh;
            moved = true;
        }
        return moved;
    }

    private void shoot() {
        if ((time() - lastShotTime > Config.Intervals.afterShotTime) &&
                (bullets > 0)) {
            bullets--;
            Bullet bullet = new Bullet(Config.Monsters.Bullet.speed, x, yStart() -
                    Config.Monsters.Bullet.startShift, 0, world, screen, this);
            bullet.y -= bullet.hh;
            world.objsToAdd.add(bullet);
            lastShotTime = time();
        }
    }
}
