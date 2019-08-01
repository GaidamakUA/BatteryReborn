package b.b.monsters;

import b.b.core.Config;
import b.b.core.Square;
import b.b.core.World;
import b.b.core.objs.Water;
import b.util.Utils;

public class EnPlane extends Monster {
    private double angle;

    public EnPlane(double x, double y, World world) {
        super(world, x, y, world.gfx.getSprite("enplane"),
                Config.Monsters.EnPlane.life * Config.Damages.bullet, ZLayer.FIVE);
        final double speed = Config.Monsters.EnPlane.speed;
        mover = new Mover(this, Config.Monsters.EnPlane.shiftSpeed, 0, speed);
        mover.setSpeed(speed, 2);
        angle = Utils.rnd(Utils.dpi);
    }

    protected void move() {
        angle = Utils.angle(angle + Config.Monsters.EnPlane.turnSpeed);
        if (angle < Math.PI) mover.move(3);
        else mover.move(1);
        mover.move(2);
        mover.move();
    }

    public void draw() {
        super.draw();
    }

    protected boolean onMonster(Monster monster) {
        if (monster instanceof Tank) {
            return false;
        }
        if (monster instanceof EnPlane) {
            removeFrom(monster);
            return true;
        }
        return super.onMonster(monster);
    }

    protected boolean onSquare(Square square) {
        if (square instanceof Water) {
            return false;
        }
        removeFrom(square);
        return true;
    }
}
