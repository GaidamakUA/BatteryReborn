package b.b.monsters.bosses;

import b.b.core.Config;
import b.b.core.World;
import b.b.monsters.MonsterPart;
import b.gfx.BufGfx;
import b.util.Utils;

public class Boss2Well extends MonsterPart {
    protected Boss2Well parent;
    protected Boss2Well child = null;
    protected double angle = Math.PI;
    protected double radius = 20;
    protected boolean left;

    private Boss2AI boss;
    private double newAngle = Math.PI;
    private double newRadius = 20;
    private double angleSpeed = 0;
    private double rSpeed = 0;

    protected Boss2Well(World world, Boss2AI pboss, Boss2Well pparent,
                        boolean pleft) {
        super(world, 0, 0, world.gfx.getSprite("boss2_well"),
                Config.Monsters.Boss2.wellLife, pboss, ZLayer.SIX);
        parent = pparent;
        left = pleft;
        boss = pboss;
    }

    public void draw() {
    }

    protected void drawFromBoss() {
        if (life > 0) {
            BufGfx b = world.gfx.bufGfx;
            if (afterDmg() || life == 0) {
                if (afterWrongDmg()) {
                    b.drawTransparentBlackRangeCheck(sprite, xScreenStart(), yScreenStart());
                } else {
                    b.drawTransparentWhiteRangeCheck(sprite, xScreenStart(), yScreenStart());
                }
            } else {
                b.drawTransparentRangeCheck(sprite, xScreenStart(), yScreenStart());
            }
        }
        if (child != null) {
            child.drawFromBoss();
        }
    }

    public final void nextFrame() {
        angle = newAngle;
        radius = newRadius;
        if (child != null) {
            child.nextFrame();
        }
    }

    public final void newFrame() {
        double maxASpeed = Config.Monsters.Boss2.wellChaoticAngleSpeed;
        angleSpeed += (Utils.rndBool() ? 1 : -1) * (maxASpeed / 10);
        if (angleSpeed > maxASpeed) {
            angleSpeed = maxASpeed;
        } else if (angleSpeed < -maxASpeed) {
            angleSpeed = -maxASpeed;
        }
        newAngle = Utils.angle(angle + angleSpeed);
        if (newAngle < Utils.hpi && Utils.rnd() < 0.3) {
            newAngle += angleSpeed;
        }
        if (newAngle > Utils.pi34 && Utils.rnd() < 0.3) {
            newAngle -= angleSpeed;
        }
        double maxSpeed = Config.Monsters.Boss2.wellChaoticSpeed;
        if (rSpeed > maxSpeed) {
            rSpeed = maxSpeed;
        } else if (rSpeed < -maxSpeed) {
            rSpeed = -maxSpeed;
        }
        newRadius = radius + rSpeed;
        double maxRadius = Config.Monsters.Boss2.wellMaxRadius;
        if (newRadius < 0) {
            newRadius = 0;
        } else if (newRadius > maxRadius) {
            newRadius = maxRadius;
        }
        if (child != null) {
            child.newFrame();
        }
    }

    protected final void setXY() {
        if (parent == null) {
            if (left) {
                x = boss.x - 156;
            } else {
                x = boss.x + 155;
            }
            y = boss.y + 35;
        } else {
            x = getX();
            y = getY();
        }
    }

    private double getX() {
        if (parent == null) {
            if (left) {
                return boss.x - 156;
            } else {
                return boss.x + 155;
            }
        } else {
            return parent.getX() + (Math.sin(angle) * radius);
        }
    }

    private double getY() {
        if (parent == null) {
            return boss.y + 25;
        } else {
            return parent.getY() + (Math.cos(angle) * radius) +
                    Config.Monsters.Boss2.wellYShift;
        }
    }
}
