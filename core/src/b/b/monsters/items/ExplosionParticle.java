package b.b.monsters.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import b.b.Battery;
import b.b.core.World;
import b.util.Time77;
import b.util.Utils;

public class ExplosionParticle extends Item implements DrawableLibGDX {
    private final static float MAX_SPEED = 0.27f;
    private Vector2 speed;
    private Explosion explosion;

    public ExplosionParticle(double x, double y, World world, float xsp, float ysp,
                             Explosion explosion) {
        super(world, x, y, world.gfx.getSprite("expl"), ZLayer.FIVE);
        this.explosion = explosion;
        float speedValue = Utils.rnd(MAX_SPEED * 0.4) + (MAX_SPEED * 0.6f);
        speed = new Vector2(speedValue, 0);
        speed.rotate(360 * Utils.rnd());
        speed.add(xsp * 0.6f, ysp * 0.6f);
        speed.scl(Time77.STEP);
        move();
        move();
        move();
    }

    protected void move() {
        x += speed.x;
        y += speed.y;
        if (explosion.secondaryExplosions > 0 && Utils.rnd() > 0.999) {
            int k = explosion.secondaryExplosions / 2;
            explosion.secondaryExplosions -= k;
            world.objectsToAdd.add(new Explosion(x, y, world, this, k));
        }
    }

    public void draw() {
    }

    @Override
    public void draw(SpriteBatch batch) {
        Battery battery = world.btr;
        batch.draw(battery.explosionParticleTexture, xScreenStart(), Battery.VIEWPORT_HEIGHT - yScreenStart());
    }
}
