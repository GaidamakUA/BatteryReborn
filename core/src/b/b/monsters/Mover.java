package b.b.monsters;

import b.b.core.Config;
import b.util.Time77;

public class Mover {
    public double dist;
    public double zeroYSpeed;

    private Monster m;
    private double maxXSpeed;
    private double maxYSpeedUp;
    private double maxYSpeedDown;
    private double a;

    public double xSpeed;
    public double ySpeed;

    public Mover(Monster mon, double maxXSpeed,
                 double maxYSpeedUp, double maxYSpeedDown) {
        m = mon;
        this.maxXSpeed = maxXSpeed;
        this.maxYSpeedUp = maxYSpeedUp;
        this.maxYSpeedDown = maxYSpeedDown;
        zeroYSpeed = 0;
        a = Config.Monsters.defaultA;
        stop();
    }

    public final void stop() {
        xSpeed = 0;
        ySpeed = zeroYSpeed;
        dist = 0;
    }

    public final void setSpeed(double speed, int dir) {
        if (dir == 0) ySpeed = -speed;
        else if (dir == 1) xSpeed = speed;
        else if (dir == 2) ySpeed = speed;
        else if (dir == 3) xSpeed = -speed;
    }

    public final void up() {
        ySpeed -= a * maxYSpeedUp;
    }

    public final void right() {
        xSpeed += a * maxXSpeed;
    }

    public final void down() {
        ySpeed += a * maxYSpeedDown;
    }

    public final void left() {
        xSpeed -= a * maxXSpeed;
    }

    public final void move(int dir) {
        if (dir == 0) up();
        if (dir == 1) right();
        if (dir == 2) down();
        if (dir == 3) left();
    }

    public final void move() {
        if (ySpeed < -zeroYSpeed) {
            ySpeed += maxYSpeedUp * a / 2;
            if (ySpeed > -zeroYSpeed) ySpeed = -zeroYSpeed;
        }
        if (xSpeed > 0) {
            xSpeed -= maxXSpeed * a / 2;
            if (xSpeed < 0) xSpeed = 0;
        }
        if (ySpeed > -zeroYSpeed) {
            ySpeed -= maxYSpeedDown * a / 2;
            if (ySpeed < -zeroYSpeed) ySpeed = -zeroYSpeed;
        }
        if (xSpeed < 0) {
            xSpeed += maxXSpeed * a / 2;
            if (xSpeed > 0) xSpeed = 0;
        }
        checkMaxs();
        double xx = m.x;
        double yy = m.y;
        m.x += xSpeed * Time77.STEP;
        m.y += ySpeed * Time77.STEP;
        xx -= m.x;
        yy -= m.y;
        dist += Math.sqrt(xx * xx + (yy * yy));
    }

    private final void checkMaxs() {
        if (ySpeed < -maxYSpeedUp) {
            ySpeed = -maxYSpeedUp;
        } else if (ySpeed > maxYSpeedDown) {
            ySpeed = maxYSpeedDown;
        }
        if (xSpeed < -maxXSpeed) {
            xSpeed = -maxXSpeed;
        } else if (xSpeed > maxXSpeed) {
            xSpeed = maxXSpeed;
        }
    }
}
