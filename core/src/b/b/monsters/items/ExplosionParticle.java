package b.b.monsters.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import b.b.Battery;
import b.b.core.World;
import b.util.Time77;
import b.util.U77;

public class ExplosionParticle extends Item implements DrawableLibGDX {
    public final static double maxSpeed = 0.27;
    private double xSpeed;
    private double ySpeed;
    private Explosion explosion;

    public ExplosionParticle(double x, double y, World world, double xsp, double ysp,
                             Explosion explosion) {
        super(world, x, y, world.gfx.getSprite("expl"), ZLayer.FIVE);
        this.explosion = explosion;
        double speed = U77.rnd(maxSpeed * 0.4) + (maxSpeed * 0.6);
        double angle = U77.rnd(U77.dpi);
        xSpeed = (Math.sin(angle) * speed + (xsp * 0.6)) * Time77.STEP;
        ySpeed = (Math.cos(angle) * speed + (ysp * 0.6)) * Time77.STEP;
        move();
        move();
        move();
    }

    protected void move() {
        x += xSpeed;
        y += ySpeed;
        if (explosion.k > 0 && U77.rnd() > 0.999) {
            int k = explosion.k / 2;
            explosion.k -= k;
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
