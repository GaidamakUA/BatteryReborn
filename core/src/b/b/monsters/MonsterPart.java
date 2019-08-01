package b.b.monsters;

import b.b.core.Config;
import b.b.core.World;
import b.b.monsters.items.Explosion;
import b.gfx.Sprite;
import com.badlogic.gdx.math.Vector2;

public class MonsterPart extends Monster {
    protected ComplexAI ai;

    public MonsterPart(World world, double x, double y, Sprite s, double life,
                       ComplexAI ai, ZLayer plvl) {
        super(world, x, y, s, life * Config.Damages.bullet, plvl);
        this.ai = ai;
    }

    public void move() {
        ai.move(this);
    }

    public void draw() {
        ai.draw();
        super.draw();
    }

    protected void damage(double damage, double time, Object cause) {
        if (!afterDmg() && life > 0 && !ai.equals(cause)) {
            life -= damage;
            if (world.btr.player.equals(cause)) {
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

    protected void justDied() {
        world.removeFromMap(this);
        world.objectsToRemove.add(this);
        if (!outOfScreen()) {
            world.level7.add(new Explosion(new Vector2((float) x, (float) y), world, this));
        }
    }

    protected boolean onMonster(Monster monster) {
        if (monster == world.btr.player) {
            return super.onMonster(monster);
        } else {
            return false;
        }
    }

    protected boolean onBullet(Bullet bullet) {
        if (bullet.owner == ai) {
            return false;
        } else {
            return super.onBullet(bullet);
        }
    }
}
