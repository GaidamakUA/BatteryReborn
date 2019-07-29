/* refactoring0 */
package b.b.core;

import b.util.Utils;

public class ConfigLoader {

    public ConfigLoader() {
        initConfig();
        logConfig();
    }

    private void initConfig() {
        double speed = Config.speed;
        Config.Monsters.Player.maxSpeed = Config.Monsters.Player.maxSpeed * speed;
        Config.Monsters.Player.maxStrafeSpeed = Config.Monsters.Player.maxStrafeSpeed * speed;
        Config.Monsters.Player.minSpeed = Config.Monsters.Player.minSpeed * speed;
        Config.Monsters.Heli.maxSpeed = Config.Monsters.Heli.maxSpeed * speed;
        Config.cameraSpeed = Config.cameraSpeed * speed;
        Config.Monsters.Bullet.speed = Config.Monsters.Bullet.speed * speed;
        Config.Monsters.Boss1.shotInterval = Config.Monsters.Boss1.shotInterval / speed;
        Config.Monsters.Boss1.bulletSpeed = Config.Monsters.Boss1.bulletSpeed * speed;
        Config.Monsters.Boss2.headYSpeed = Config.Monsters.Boss2.headYSpeed * speed;
        Config.Monsters.Boss2.headXSpeed = Config.Monsters.Boss2.headXSpeed * speed;
        Config.Monsters.Cannon.shotInterval = Config.Monsters.Cannon.shotInterval / speed;
        Config.Monsters.Cannon.ballSpeed = Config.Monsters.Cannon.ballSpeed * speed;
    }

    private void logConfig() {
        System.out.println("start clienttime " + System.currentTimeMillis());
        System.out.println("version " + Utils.sprecision(Config.version, 2));
        System.out.println("levels " + Config.getLevelsString());
        double speed = Config.speed;
        System.out.println("speed " + speed);
        System.out.println("maxSpeed " + Config.Monsters.Player.maxSpeed / speed);
        System.out.println("maxStrafeSpeed " + Config.Monsters.Player.maxStrafeSpeed / speed);
        System.out.println("minSpeed " + Config.Monsters.Player.minSpeed / speed);
        System.out.println("playerLife " + Config.Monsters.Player.life);
        System.out.println("heliMaxSpeed " + Config.Monsters.Heli.maxSpeed / speed);
        System.out.println("heliLife " + Config.Monsters.Heli.life);
        System.out.println("cameraSpeed " + Config.cameraSpeed / speed);
        System.out.println("boss1Life " + Config.Monsters.Boss1.life);
        System.out.println("dirtK " + Config.Gfx.dirtK);
        System.out.println("bulletSpeed " + Config.Monsters.Bullet.speed / speed);
        System.out.println("defaultA " + Config.Monsters.defaultA);
        System.out.println("startBullets " + Config.Monsters.Player.bullets);//here
        System.out.println("b1shotInterval " + Config.Monsters.Boss1.shotInterval * speed);
        System.out.println("b1bulletSpeed " + Config.Monsters.Boss1.bulletSpeed / speed);
        System.out.println("b2paneLife " + Config.Monsters.Boss2.paneLife);
        System.out.println("b2headYSpeed " + Config.Monsters.Boss2.headYSpeed / speed);
        System.out.println("b2headXSpeed " + Config.Monsters.Boss2.headXSpeed / speed);
        System.out.println("b2gunLife " + Config.Monsters.Boss2.gunLife);
        System.out.println("b2wellMaxRadius " + Config.Monsters.Boss2.wellMaxRadius);
        System.out.println("b2wellYShift " + Config.Monsters.Boss2.wellYShift);
        System.out.println("canShotInterval " + Config.Monsters.Cannon.shotInterval * speed);
        System.out.println("canBallSpeed " + Config.Monsters.Cannon.ballSpeed / speed);
        System.out.println("dmgCannonBall " + Config.Damages.cannonBall);
    }
}
