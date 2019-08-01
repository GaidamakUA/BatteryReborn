package b.b.monsters;

import b.b.core.*;
import b.b.core.objs.Water;
import b.b.monsters.items.Coin;
import b.b.monsters.items.Explosion;
import b.b.monsters.items.Item;
import b.gfx.BufGfx;
import b.gfx.Sprite;
import com.badlogic.gdx.math.Vector2;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class Monster extends Drawable implements Changeable {
    public double life;
    public double maxLife;

    protected double lastDmgTime;
    protected double wrongDmgTime;
    protected Sprite sprite;
    protected double dist;
    @Nullable
    protected Mover mover;

    public Monster(World world, double x, double y, Sprite sprite, double maxLife, ZLayer zLayer) {
        this(x, y, sprite.width, sprite.height, true, zLayer, maxLife,
                maxLife, world);
        this.sprite = sprite;
        mover = null;
    }

    private Monster(double x, double y, int width, int height,
                    boolean isShape, ZLayer lvl, double life, double maxLife, World world) {
        super(x, y, width, height, isShape, lvl, world);
        this.life = life;
        this.maxLife = maxLife;
        lastDmgTime = -1000;
        wrongDmgTime = -1000;
        dist = 0;
    }

    public final void act() {
        if (life > 0) {
            world.removeFromMap(this);
            move();
            int collisionCountdown = Config.Monsters.maxCollisionCount;
            while (checkCollision() && collisionCountdown > 0) {
                collisionCountdown--;
            }
            if (collisionCountdown == 0 && life != 0) {
                life = 0;
                justDied();
            } else if (life > 0) {
                world.addToMap(this);
            } else {
                world.objectsToRemove.add(this);
            }
        }
    }

    protected void justDied() {
        world.removeFromMap(this);
        world.objectsToRemove.add(this);
        world.objectsToAdd.add(new Coin(x, y, world));
        if (!outOfScreen()) {
            world.level7.add(new Explosion(new Vector2((float) x, (float) y), world, this));
        }
    }

    /**
     * Do not care about collisions and removing or adding to map.
     */
    protected abstract void move();

    public void damage(double dmg, Object cause) {
        damage(dmg, time(), cause);
    }

    protected void damage(double damage, double time, Object cause) {
        if (!afterDmg() && life > 0) {
            life -= damage;
            if (!(this instanceof Player) && world.btr.player.equals(cause)) {
                world.btr.player.incScores();
            }
            if (life <= 0) {
                life = 0;
                justDied();
            }
            lastDmgTime = time;
        } else if (cause instanceof Monster) {
            wrongDmgTime = time;
        }
    }

    /**
     * Checks if Monster ready for the next dmg.
     */
    public boolean afterDmg() {
        return time() - lastDmgTime < Config.Monsters.afterDmgTime;
    }

    protected boolean afterWrongDmg() {
        return time() - wrongDmgTime < Config.Monsters.wrongDmgTime;
    }

    public void draw() {
        BufGfx b = world.gfx.bufGfx;
        if (afterDmg() || life == 0) {
            if (afterWrongDmg()) {
                b.drawTransparentBlackRangeCheck(sprite, xScreenStart(), yScreenStart());
            } else {
                b.drawTransparentWhiteRangeCheck(sprite, xScreenStart(), yScreenStart());
            }
        } else {
            b.drawTransparentRangeCheck(sprite, xScreenStart(), yScreenStart());
        }
    }

    /**
     * Object should be removed from map before the method call.
     * The method should not add it to map.
     *
     * @return true if something moved
     */
    private boolean checkCollision() {
        if (checkScreenCollision()) {
            return true;
        }
        List<Drawable> objs = world.get(this);
        for (Drawable o : objs) {
            if (o.isShape) {
                if (!(o instanceof Monster) || ((Monster) o).life > 0) {
                    if (collision(o)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Object should be removed from map before
     */
    protected boolean checkScreenCollision() {
        if (yStart() > screen.yEnd()) {
            damage(maxLife, null);
            return true;
        }
        return false;
    }

    protected final boolean outOfScreen() {
        boolean res = (yStart() > screen.yEnd() || yEnd() < screen.camY() ||
                xStart() > screen.xEnd() || xEnd() < 0);
        return res;
    }

    /**
     * @param drawable is not this. Be sure.
     * @return true if something removed
     */
    protected boolean collision(Drawable drawable) {
        if (((xStart() >= drawable.xStart() && xStart() < drawable.xEnd()) ||
                (drawable.xStart() >= xStart() && drawable.xStart() < xEnd())) &&
                ((yStart() >= drawable.yStart() && yStart() < drawable.yEnd()) ||
                        (drawable.yStart() >= yStart() && drawable.yStart() < yEnd()))) {
            if (drawable instanceof Square) {
                return onSquare((Square) drawable);
            } else if (drawable instanceof Item) {
                return onItem((Item) drawable);
            } else if (drawable instanceof Bullet) {
                return onBullet((Bullet) drawable);
            } else if (drawable instanceof Monster) {
                return onMonster((Monster) drawable);
            }
            throw new RuntimeException("Unknown collision: " + getClass().getName());
        }
        return false;
    }

    protected boolean onSquare(Square square) {
        if (square instanceof Water) {
            return false;
        }
        damage(Config.Damages.square, null);
        removeFrom(square);
        return true;
    }

    protected boolean onBullet(Bullet bullet) {
        if ((bullet.owner != this) && (bullet != this)) {
            damage(Config.Damages.bullet, bullet.owner);
            bullet.damage(1, this);
        }
        return false;
    }

    protected boolean onMonster(Monster monster) {
        if (monster instanceof InvisibleMonster) {
            return false;
        }
        damage(Config.Damages.monster, monster);
        monster.damage(Config.Damages.monster, this);
        return !afterDmg() && !monster.afterDmg();
    }

    protected boolean onItem(Item i) {
        return i.onMonster(this);
    }

    /**
     * Rectangles should be overlapped
     */
    protected void removeFrom(Drawable drawable) {
        double xCenterDist = x - drawable.x;
        double yCenterDist = y - drawable.y;
        double xDist = halfWidth + drawable.halfWidth;
        double yDist = halfHeight + drawable.halfHeight;
        if (xDist - Math.abs(xCenterDist) < yDist - Math.abs(yCenterDist)) {
            if (xCenterDist < 0) {
                x = drawable.x - xDist;
            } else {
                x = drawable.x + xDist;
            }
        } else {
            if (yCenterDist < 0) {
                y = drawable.y - yDist;
            } else {
                y = drawable.y + yDist;
            }
        }
    }

    protected double time() {
        return world.gfx.battery.time.time;
    }

    public Vector2 getSpeed() {
        if (mover != null) {
            return new Vector2((float) mover.xSpeed, (float) mover.ySpeed).scl(1000);
        } else {
            return new Vector2();
        }
    }
}
