package b.b.monsters.items;

import b.b.Battery;
import b.b.core.World;
import b.util.Utils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class ExplosionParticle implements DrawableLibGDX {
    private final static float MAX_SPEED = 270f;
    private final World world;
    private final Explosion explosion;
    private final Vector2 speed;
    private final Vector2 position;

    public ExplosionParticle(float x, float y, World world, float xsp, float ysp,
                             Explosion explosion) {
        position = new Vector2(x, y);
        this.world = world;
        this.explosion = explosion;
        float speedValue = (Utils.rnd(0.4) + 0.6f) * MAX_SPEED;
        speed = new Vector2(speedValue, 0);
        float rotationAngle = 360 * Utils.rnd();
        speed.rotate(rotationAngle);
        speed.add(xsp * 600f, ysp * 600f);
        Vector2 startingDisplacement = new Vector2(3, 0);
        startingDisplacement.rotate(rotationAngle);
        position.add(startingDisplacement);
    }

    private void move() {
        float time = Gdx.graphics.getDeltaTime();
        position.add(speed.x * time, speed.y * time);
        if (explosion.secondaryExplosions > 0 && Utils.rnd() > 0.999) {
            int k = explosion.secondaryExplosions / 2;
            explosion.secondaryExplosions -= k;
            world.level7.add(new Explosion(position.x, position.y, world, speed, k));
        }
    }

    public void draw() {
    }

    @Override
    public void draw(SpriteBatch batch) {
        move();
        Battery battery = world.btr;
        batch.draw(battery.explosionParticleTexture, position.x, Battery.VIEWPORT_HEIGHT - (position.y - world.btr.screen.camY()));
    }
}
