package b.b.monsters.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import b.b.core.World;
import b.b.monsters.Monster;

public class Explosion implements DrawableLibGDX {
    private static final double DURATION = 370;
    private static final int PARTICLES_COUNT = 30;

    private final World world;
    private final List<ExplosionParticle> list = new ArrayList<ExplosionParticle>();
    private long time;
    protected int secondaryExplosions;

    public Explosion(double x, double y, World world, Monster monster) {
        this(x, y, world, monster, 1);
    }

    public Explosion(double x, double y, World world, Monster monster, int secondaryExplosions) {
        this(x, y, world, monster.getSpeed(), secondaryExplosions);
    }

    public Explosion(double x, double y, World world, Vector2 initialSpeed, int secondaryExplosions) {
        this.world = world;
        this.secondaryExplosions = secondaryExplosions - 1;
        double xSpeed = initialSpeed.x;
        double ySpeed = initialSpeed.y;
        for (int i = 0; i < PARTICLES_COUNT; i++) {
            list.add(new ExplosionParticle((float) x, (float) y, world, (float) xSpeed, (float) ySpeed, this));
        }
        time = TimeUtils.millis();
    }

    @Override
    public void draw(SpriteBatch batch) {
        for (ExplosionParticle e : list) {
            e.move();
        }
        if (TimeUtils.timeSinceMillis(time) > DURATION) {
            world.level7.remove(this);
        }
        for (DrawableLibGDX e : list) {
            e.draw(batch);
        }
    }
}
