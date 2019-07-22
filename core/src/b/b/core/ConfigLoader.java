/* refactoring0 */
package b.b.core;

import b.b.Battery;
import b.util.P;
import b.util.Str77;
import b.util.U77;

import java.util.Arrays;

public class ConfigLoader {
    Battery btr;

    public ConfigLoader(Battery battery) {
        btr = battery;
        initConfig();
        logConfig();
    }

    private final void initConfig() {
        try {
            setLevels(getParam("levels"));
        } catch (Exception e) {
            try {
                P.log("probably error getting levels parameter:[" + getParam("levels") + "]");
                e.printStackTrace();
            } catch (Exception ignored) {
            }
            Config.levels = Config.getLevels();
        }
        Config.speed = getDouble("speed", Config.speed);
        double speed = Config.speed;
        Config.Monsters.Player.maxSpeed =
                getDouble("maxSpeed", Config.Monsters.Player.maxSpeed) * speed;
        Config.Monsters.Player.maxStrafeSpeed =
                getDouble("maxStrafeSpeed", Config.Monsters.Player.maxStrafeSpeed) *
                        speed;
        Config.Monsters.Player.minSpeed =
                getDouble("minSpeed", Config.Monsters.Player.minSpeed) * speed;
        Config.Monsters.Player.life =
                getInt("playerLife", Config.Monsters.Player.life);
        Config.Monsters.Heli.maxSpeed =
                getDouble("heliMaxSpeed", Config.Monsters.Heli.maxSpeed) * speed;
        Config.Monsters.Heli.life =
                getInt("heliLife", Config.Monsters.Heli.life);
        Config.cameraSpeed =
                getDouble("cameraSpeed", Config.cameraSpeed) * speed;
        Config.Monsters.Boss1.life =
                getInt("boss1Life", Config.Monsters.Boss1.life);
        Config.Gfx.dirtK = getDouble("dirtK", Config.Gfx.dirtK);
        Config.Gfx.restDirtK = 1 - Config.Gfx.dirtK;
        Config.Monsters.Bullet.speed =
                getDouble("bulletSpeed", Config.Monsters.Bullet.speed) * speed;
        Config.Monsters.defaultA = getDouble("defaultA", Config.Monsters.defaultA);
        Config.Monsters.Player.bullets = getInt("startBullets",
                Config.Monsters.Player.bullets);
        Config.Monsters.Boss1.shotInterval = getDouble("b1shotInterval",
                Config.Monsters.Boss1.shotInterval) / speed;
        Config.Monsters.Boss1.bulletSpeed = getDouble("b1bulletSpeed",
                Config.Monsters.Boss1.bulletSpeed) * speed;
        Config.Monsters.Boss2.paneLife = getInt("b2paneLife",
                Config.Monsters.Boss2.paneLife);
        Config.Monsters.Boss2.headYSpeed = getDouble("b2headYSpeed",
                Config.Monsters.Boss2.headYSpeed) * speed;
        Config.Monsters.Boss2.headXSpeed = getDouble("b2headXSpeed",
                Config.Monsters.Boss2.headXSpeed) * speed;
        Config.Monsters.Boss2.gunLife = getInt("b2gunLife",
                Config.Monsters.Boss2.gunLife);
        Config.Monsters.Boss2.wellMaxRadius = getDouble("b2wellMaxRadius",
                Config.Monsters.Boss2.wellMaxRadius);
        Config.Monsters.Boss2.wellYShift = getDouble("b2wellYShift",
                Config.Monsters.Boss2.wellYShift);
        Config.Monsters.Cannon.shotInterval = getDouble("canShotInterval",
                Config.Monsters.Cannon.shotInterval) / speed;
        Config.Monsters.Cannon.ballSpeed = getDouble("canBallSpeed",
                Config.Monsters.Cannon.ballSpeed) * speed;
        Config.Damages.cannonBall = getDouble("dmgCannonBall",
                Config.Damages.cannonBall);
    }

    private final void logConfig() {
        log("start clienttime " + System.currentTimeMillis());
        log("version " + U77.sprecision(Config.version, 2));
        log("levels " + Config.getLevelsString());
        double speed = Config.speed;
        log("speed " + speed);
        log("maxSpeed " + Config.Monsters.Player.maxSpeed / speed);
        log("maxStrafeSpeed " + Config.Monsters.Player.maxStrafeSpeed / speed);
        log("minSpeed " + Config.Monsters.Player.minSpeed / speed);
        log("playerLife " + Config.Monsters.Player.life);
        log("heliMaxSpeed " + Config.Monsters.Heli.maxSpeed / speed);
        log("heliLife " + Config.Monsters.Heli.life);
        log("cameraSpeed " + Config.cameraSpeed / speed);
        log("boss1Life " + Config.Monsters.Boss1.life);
        log("dirtK " + Config.Gfx.dirtK);
        log("bulletSpeed " + Config.Monsters.Bullet.speed / speed);
        log("defaultA " + Config.Monsters.defaultA);
        log("startBullets " + Config.Monsters.Player.bullets);//here
        log("b1shotInterval " + Config.Monsters.Boss1.shotInterval * speed);
        log("b1bulletSpeed " + Config.Monsters.Boss1.bulletSpeed / speed);
        log("b2paneLife " + Config.Monsters.Boss2.paneLife);
        log("b2headYSpeed " + Config.Monsters.Boss2.headYSpeed / speed);
        log("b2headXSpeed " + Config.Monsters.Boss2.headXSpeed / speed);
        log("b2gunLife " + Config.Monsters.Boss2.gunLife);
        log("b2wellMaxRadius " + Config.Monsters.Boss2.wellMaxRadius);
        log("b2wellYShift " + Config.Monsters.Boss2.wellYShift);
        log("canShotInterval " + Config.Monsters.Cannon.shotInterval * speed);
        log("canBallSpeed " + Config.Monsters.Cannon.ballSpeed / speed);
        log("dmgCannonBall " + Config.Damages.cannonBall);
    }

    private final void log(String s) {
        btr.logger.log(s);
    }

    public static final void setLevels(String s) {
        Integer[] lvls = Str77.integerArr(s);
        Config.levels = Arrays.asList(lvls);
    }

    private final double getDouble(String paramName, double defaultValue) {
        try {
            return Double.parseDouble(getParam(paramName));
        } catch (Exception e) {
            P.log("error getting " + paramName + " parameter");
            return defaultValue;
        }
    }

    private final int getInt(String name, int defaultValue) {
        try {
            return (int) Double.parseDouble(getParam(name));
        } catch (Exception e) {
            P.log("error getting " + name + " parameter");
            return defaultValue;
        }
    }

    private final boolean getBool(String paramName, boolean defaultValue) {
        try {
            return getParam(paramName).equals("true");
        } catch (Exception e) {
            P.log("error getting " + paramName + " parameter");
            return defaultValue;
        }
    }

    /**
     * @return null on error
     */
    private final String getParam(String name) {
        throw new RuntimeException("error getting " + name + " parameter");
    }
}
