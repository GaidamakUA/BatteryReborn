package b.b.monsters.bosses;

import b.b.core.Config;
import b.b.core.World;
import b.b.monsters.MonsterPart;
import b.gfx.BufGfx;
import b.util.U77;

public class Boss2Well extends MonsterPart {
    protected Boss2Well parent;
    protected Boss2Well child = null;
    protected double angle = Math.PI;
    protected double radius = 20;
    protected boolean left;

    private Boss2AI boss;
    private double newAngle = Math.PI;
    private double newRadius = 20;
    private double aSpeed = 0;
    private double rSpeed = 0;

    protected Boss2Well(World world, Boss2AI pboss, Boss2Well pparent,
                        boolean pleft) {
        super(world, 0, 0, world.g.getSprite("boss2_well"),
                Config.Monsters.Boss2.wellLife, pboss, 6);
        parent = pparent;
        left = pleft;
        boss = pboss;
    }

    public void draw() {
    }

    protected void drawFromBoss() {
        if (life > 0) {
            BufGfx b = world.g.b;
            if (afterDmg() || life == 0) {
                if (afterWrongDmg()) {
                    b.drawTranspBlackRangeCheck(sprite, xScreenStart(), yScreenStart());
                } else {
                    b.drawTranspWhiteRangeCheck(sprite, xScreenStart(), yScreenStart());
                }
            } else {
                b.drawTranspRangeCheck(sprite, xScreenStart(), yScreenStart());
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
        double maxASpeed = Config.Monsters.Boss2.wellChaoticASpeed;
        aSpeed += (U77.rndBool() ? 1 : -1) * (maxASpeed / 10);
        if (aSpeed > maxASpeed) {
            aSpeed = maxASpeed;
        } else if (aSpeed < -maxASpeed) {
            aSpeed = -maxASpeed;
        }
        newAngle = U77.angle(angle + aSpeed);
        if (newAngle < U77.hpi && U77.rnd() < 0.3) {
            newAngle += aSpeed;
        }
        if (newAngle > U77.pi34 && U77.rnd() < 0.3) {
            newAngle -= aSpeed;
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

    private final double getX() {
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

    private final double getY() {
        if (parent == null) {
            return boss.y + 25;
        } else {
            return parent.getY() + (Math.cos(angle) * radius) +
                    Config.Monsters.Boss2.wellYShift;
        }
    }
}
