package b.b.core;

import java.util.Arrays;
import java.util.List;

import b.util.Str77;

public class Config {
    public static final double version = 1.09;
    public static final double speed = 1.305;
    public static final int squareSize = 30;
    public static final int hSquareSize = squareSize / 2;
    public static double cameraSpeed = 0.0444;
    public static final double activationDistance = 3.04;
    public static final boolean debugMode = true;

    /*numbers of image map*/
    public static final List<Integer> levels = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    public static String getLevelsString() {
        return Str77.toString(levels);
    }

    public static class Intervals {
        public static final double nextLevelDelay = 700;
        public static final double afterShotTime = 309;
        public static final double fpsLogPeriod = 10000;
        public static final double immortality = 5000;
        public static final double afterBossPeriod = 6000;
    }

    public static class Monsters {
        public static final int maxCollisionCount = 30;
        public static final double defaultA = 0.0118;
        public static final double afterDmgTime = 272;
        public static final double wrongDmgTime = 125;

        public static class FirstAid {
            public static final double bigRotRadius = 13.6;
            public static final double smallRotRadius = 6.6;
            public static final double bigRotSpeed = 0.0025;
            public static final double smallRotSpeed = 0.03;
            public static final double effect = 25;
        }

        public static class Coin {
            public static final double duration = 2600;
            public static final double lineDuration = 590;
        }

        public static class Player {
            public static double maxSpeed = 0.1248;
            public static double maxStrafeSpeed = 0.0715;
            public static double minSpeed = 0.0409;
            public static final int life = 6;
            public static final double afterDmgTime = 595;
            public static final int bullets = 80;
        }

        public static class EnPlane {
            public static final double speed = 0.09;
            public static final double shiftSpeed = 0.06;
            public static final double turnSpeed = 0.01;
            public static final int life = 2;
        }

        public static class Heli {
            public static double maxSpeed = 0.055;
            public static final int life = 2;
            public static final double bladesK = 0.0343;
        }

        public static class Tank {
            public static final double turnDistance = 1430;
            public static final double speed = 0.018;
            public static final int life = 3;
            public static final double stayPeriod = 3200;
            public static final double minMovePeriod = 3250;
            public static final double bulletSpeed = 0.15;
            /* the more wish the less result */
            public static final double wishForPlayer = 0.6;
            public static final double turretTime = 350;
        }

        public static class Boss1 {
            public static final int life = 12;
            public static final double maxSpeed = 0.112;
            public static final double maxStrafeSpeed = 0.045;
            public static final double minSpeed = 0.038;
            public static double shotInterval = 1060;
            public static double bulletSpeed = 0.083;
            public static final int rewardCoins = 17;
        }

        public static class Boss2 {
            public static final double maxSpeed = 0.08;
            public static final double maxStrafeSpeed = 0.09;
            public static final double minSpeed = 0.041;
            public static final double bulletSpeed = 0.052;
            public static final int paneLife = 13;
            public static double headYSpeed = 0.103;
            public static double headXSpeed = 0.1024;
            public static final int gunLife = 2;
            public static final int wellLife = 1;
            public static final double gunPartSpeed = 0.0103;
            public static final double wellChaoticSpeed = 0.0103;
            public static final double wellChaoticASpeed = 0.0107;
            public static final double wellMaxRadius = 35;
            public static final double wellYShift = 27.5;
            public static final int rewardCoins = 20;
        }

        public static class Cannon {
            public static double shotInterval = 2400;
            public static double ballSpeed = 0.09;
        }

        public static class Bullet {
            public static double speed = 0.3600;
            public static final double startShift = 3;
            public static final double growth = 0.03;
        }
    }

    public static class Bcks {
        public static final double waterWave = 0.509;
        public static final double waterXWaveTimeK = 500;

        public static class YellowBorder {
            public static final boolean bck4 = true;
            public static final boolean warfloor = true;
        }
    }

    public static class Damages {
        public static final double bullet = 18.3;
        public static final double square = 11.5;
        public static final double monster = 18;
        public static final double cannonBall = 50;
    }

    public static class Gfx {
        public static final double dirtK = 0.46;
        public static final double restDirtK = 1 - dirtK;
        public static final boolean dirtOn = true;
        public static final boolean dirtGround = true;
        public static final double groundDirtK = 0.08;
    }
}
