package b.b.monsters;

import b.b.core.Config;
import b.b.core.World;
import b.util.Time77;
import b.util.U77;

public class TankAI {
    protected Tank tank;
    protected World world;

    /*
     * Direction: 0-up, 1-right, 2-down, 3-left
     */
    protected int dir;
    protected int turretDir;

    /*
     * Since last turn
     */
    private double distance;

    private double timeWhenStand;
    private double timeWhenMoved;
    private boolean stays;
    private Player p;


    protected TankAI(Tank t, World w, int direction) {
        tank = t;
        world = w;
        dir = direction;
        turretDir = direction;
        distance = 0;
        timeWhenStand = -9999;
        timeWhenMoved = -9999;
        stays = false;
        p = world.gfx.battery.player;
    }

    private double time() {
        return world.gfx.battery.time.time;
    }

    protected static int opposite(int dir) {
        return U77.rem(dir + 2, 4);
    }

    protected void move() {
        if (!stays) {
            if (U77.rnd(distance) > U77.rnd(Config.Monsters.Tank.turnDistance)) {
                if (dir == dirToPlayer()) {
                    if (U77.rnd() > Config.Monsters.Tank.wishForPlayer) {
                        dir = anotherDirToPlayer();
                        turretDir = dirToPlayer();
                    } else {
                        turnRnd90();
                    }
                } else {
                    if (U77.rnd() > Config.Monsters.Tank.wishForPlayer) {
                        dir = dirToPlayer();
                        turretDir = dir;
                    } else {
                        turnRnd90();
                        turretDir = dir;
                    }
                }
                distance = 0;
            }
            moveDir();
            distance += Config.Monsters.Tank.speed * Time77.STEP;
            if (onPlayerLine() && movedEnough()) {
                turretDir = anotherDirToPlayer();
                timeWhenStand = time();
                stays = true;
            }
        }
        if (stood()) {
            stays = false;
            timeWhenMoved = time();
            distance = 0;
            tank.gun.shoot(turretDir);
        }
    }

    private boolean movedEnough() {
        return (time() - timeWhenMoved > Config.Monsters.Tank.minMovePeriod && !stays);
    }

    private boolean stood() {
        return (time() - timeWhenStand > Config.Monsters.Tank.stayPeriod) && stays;
    }

    private boolean onPlayerLine() {
        return (tank.x >= p.xStart() && tank.x <= p.xEnd()) ||
                (tank.y >= p.yStart() && tank.y <= p.yEnd());
    }

    protected void turnRnd90() {
        if (U77.rnd() > 0.5) {
            dir++;
            if (dir == 4) dir = 0;
        } else {
            dir--;
            if (dir == -1) dir = 3;
        }
    }

    private void moveDir() {
        double speed = Config.Monsters.Tank.speed * Time77.STEP;
        if (dir == 0) {
            tank.y -= speed;
        } else if (dir == 1) {
            tank.x += speed;
        } else if (dir == 2) {
            tank.y += speed;
        } else tank.x -= speed;
    }

    /**
     * To come online
     */
    private int dirToPlayer() {
        if (tank.x <= p.x) {
            double xDist = p.x - tank.x;
            if (tank.y <= p.y) {
                double yDist = p.y - tank.y;
                if (xDist < yDist) {
                    return 1;
                } else {
                    return 2;
                }
            } else {/*tank.y>p.y*/
                double yDist = tank.y - p.y;
                if (xDist < yDist) {
                    return 1;
                } else {
                    return 0;
                }
            }
        } else {/*tank.x>p.x*/
            double xDist = tank.x - p.x;
            if (tank.y <= p.y) {
                double yDist = p.y - tank.y;
                if (xDist < yDist) {
                    return 3;
                } else {
                    return 2;
                }
            } else {/*tank.y>p.y*/
                double yDist = tank.y - p.y;
                if (xDist < yDist) {
                    return 3;
                } else {
                    return 0;
                }
            }
        }
    }

    private int anotherDirToPlayer() {
        if (tank.x <= p.x) {
            double xDist = p.x - tank.x;
            if (tank.y <= p.y) {
                double yDist = p.y - tank.y;
                if (xDist < yDist) {
                    return 2;
                } else {
                    return 1;
                }
            } else {/*tank.y>p.y*/
                double yDist = tank.y - p.y;
                if (xDist < yDist) {
                    return 0;
                } else {
                    return 1;
                }
            }
        } else {/*tank.x>p.x*/
            double xDist = tank.x - p.x;
            if (tank.y <= p.y) {
                double yDist = p.y - tank.y;
                if (xDist < yDist) {
                    return 2;
                } else {
                    return 3;
                }
            } else {/*tank.y>p.y*/
                double yDist = tank.y - p.y;
                if (xDist < yDist) {
                    return 0;
                } else {
                    return 3;
                }
            }
        }
    }
}
