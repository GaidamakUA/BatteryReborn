package b.b.monsters.items;

import b.b.core.World;
import b.b.monsters.Monster;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class Explosion implements DrawableLibGDX {
    private static final float DURATION = 370;
    private static final int PARTICLES_COUNT = 30;

    private final World world;
    private final List<ExplosionParticle> list = new ArrayList<ExplosionParticle>();
    private long time;
    protected int secondaryExplosions;

    public Explosion(Vector2 position, World world, Monster monster) {
        this(position, world, monster, 1);
    }

    public Explosion(Vector2 position, World world, Monster monster, int secondaryExplosions) {
        this(position, world, monster.getSpeed(), secondaryExplosions);
    }

    public Explosion(Vector2 position, World world, Vector2 initialSpeed, int secondaryExplosions) {
        this.world = world;
        this.secondaryExplosions = secondaryExplosions - 1;
        for (int i = 0; i < PARTICLES_COUNT; i++) {
            list.add(new ExplosionParticle(position, world, initialSpeed, this));
        }
        time = TimeUtils.millis();
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (TimeUtils.timeSinceMillis(time) > DURATION) {
            world.level7.remove(this);
        }
        for (DrawableLibGDX e : list) {
            e.draw(batch);
        }
    }
}
