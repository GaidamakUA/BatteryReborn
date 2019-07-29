package b.b.monsters.items;

import b.b.core.World;
import b.b.monsters.Monster;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class Explosion extends Item implements DrawableLibGDX {
    private static final double duration = 370;
    private static final int count = 30;

    private List<ExplosionParticle> list;
    private double time;
    protected int secondaryExplosions;

    public Explosion(double x, double y, World world, Monster monster) {
        this(x, y, world, monster, 1);
    }

    public Explosion(double x, double y, World world, Monster monster, int secondaryExplosions) {
        super(world, x, y, world.gfx.getSprite("expl"), ZLayer.SEVEN);
        this.secondaryExplosions = secondaryExplosions - 1;
        list = new ArrayList<ExplosionParticle>();
        double xSpeed = 0;
        double ySpeed = 0;
        if (monster.mover != null) {
            xSpeed = monster.mover.xSpeed;
            ySpeed = monster.mover.ySpeed;
        }
        for (int i = 0; i < count; i++) {
            list.add(new ExplosionParticle(x, y, world, xSpeed, ySpeed, this));
        }
        time = time();
        setWH(1, 1);
    }

    public void draw() {
    }

    protected void move() {
        y = world.btr.screen.camY() + (world.btr.screen.h / 2);
        for (ExplosionParticle e : list) {
            e.move();
        }
        if (time + duration < time()) {
            dmg(1, null);
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        for (DrawableLibGDX e : list) {
            e.draw(batch);
        }
    }
}
