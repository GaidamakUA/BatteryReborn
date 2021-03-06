package b.b.monsters;

import b.b.core.Config;
import b.b.core.Square;
import b.b.core.World;
import b.b.core.objs.Water;
import b.gfx.Sprite;
import b.util.Utils;

public class Helicopter extends Monster {
    /**
     * Direction: 0-up, 1-right, 2-down, 3-left
     */
    private int dir;
    private Sprite flipped;

    public Helicopter(double x, double y, World world, int direction) {
        super(world, x, y, world.gfx.getSprite("heli"),
                Config.Monsters.Heli.life * Config.Damages.bullet, ZLayer.FIVE);
        dir = direction;
        flipped = world.gfx.getSprite("heliLeft");
        final double speed = Config.Monsters.Heli.maxSpeed;
        mover = new Mover(this, speed, 0, 0);
        mover.setSpeed(speed, dir);
    }

    protected void move() {
        mover.move(dir);
        mover.move();
    }

    public void draw() {
        super.draw();
        Sprite blades = world.gfx.getSprite("heli_blades" +
                Utils.rem((int) (world.gfx.battery.time.time *
                        Config.Monsters.Heli.bladesK), 4));
        if (dir == 3) {
            world.gfx.bufGfx.drawTransparentTrRangeCheck(blades, xScreenStart(),
                    yScreenStart(), 0.7);
        } else {
            world.gfx.bufGfx.drawTransparentTrRangeCheck(blades, xScreenBorder() - blades.width,
                    yScreenStart(), 0.7);
        }
    }

    protected boolean onSquare(Square square) {
        if (square instanceof Water) {
            return false;
        }
        removeFrom(square);
        mover.stop();
        if (dir == 1) dir = 3;
        else dir = 1;
        Sprite s = sprite;
        sprite = flipped;
        flipped = s;
        return true;
    }

    protected boolean checkScreenCollision() {
        if (super.checkScreenCollision()) return true;
        if (xEnd() < 0) {
            if (dir != 1) {
                world.objectsToRemove.add(this);
            }
        } else if (xStart() > screen.xEnd()) {
            if (dir != 3) {
                world.objectsToRemove.add(this);
            }
        }
        return false;
    }

    protected boolean onMonster(Monster monster) {
        if (monster instanceof Tank) {
            return false;
        }
        if (!(monster instanceof Helicopter)) {
            return super.onMonster(monster);
        }
        removeFrom(monster);
        mover.stop();
        monster.mover.stop();
        if (dir == 1) dir = 3;
        else dir = 1;
        Sprite s = sprite;
        sprite = flipped;
        flipped = s;
        return true;
    }
}
