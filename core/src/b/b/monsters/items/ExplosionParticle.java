package b.b.monsters.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import b.b.Battery;
import b.b.core.World;
import b.util.Time77;
import b.util.Utils;

public class ExplosionParticle implements DrawableLibGDX {
    private final static float MAX_SPEED = 0.27f;
    private final World world;
    private final Explosion explosion;
    private final Vector2 speed;
    private final Vector2 position;

    public ExplosionParticle(float x, float y, World world, float xsp, float ysp,
                             Explosion explosion) {
        position = new Vector2(x, y);
        this.world = world;
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
        position.add(speed);
        if (explosion.secondaryExplosions > 0 && Utils.rnd() > 0.999) {
            int k = explosion.secondaryExplosions / 2;
            explosion.secondaryExplosions -= k;
            world.objectsToAdd.add(new Explosion(position.x, position.y, world, speed, k));
        }
    }

    public void draw() {
    }

    @Override
    public void draw(SpriteBatch batch) {
        Battery battery = world.btr;
        batch.draw(battery.explosionParticleTexture, position.x, Battery.VIEWPORT_HEIGHT - (position.y - world.btr.screen.camY()));
    }
}
