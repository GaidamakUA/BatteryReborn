package b.b.core;

import b.util.P;
import b.util.Str77;

import java.util.Arrays;
import java.util.List;

public class Config {
    public static final double version = 1.09;
    public static double speed = 1.305;
    public static final int squareSize = 30;
    public static final int hSquareSize = squareSize / 2;
    public static double cameraSpeed = 0.0444;
    public static double activationDistance = 3.04;
    public static final boolean debugMode = true;
    public static final String siteUrl = "http:" + P.bs + "btrgame.com/";

    /*numbers of image map*/
    public static List<Integer> levels = getLevels();

    public static List<Integer> getLevels() {
        Integer[] lvls = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        return Arrays.asList(lvls);
    }

    public static final String getLevelsString() {
        return Str77.toString(levels);
    }

    public static class Intervals {
        public static double nextLevelDelay = 700;
        public static double afterShotTime = 309;
        public static final double fpsLogPeriod = 10000;
        public static final double immortality = 5000;
        public static double afterBossPeriod = 6000;
    }

    public static class Monsters {
        public static final int maxCollisionCount = 30;
        public static double defaultA = 0.0118;
        public static double afterDmgTime = 272;
        public static double wrongDmgTime = 125;

        public static class FirstAid {
            public static double bigRotRadius = 13.6;
            public static double smallRotRadius = 6.6;
            public static double bigRotSpeed = 0.0025;
            public static double smallRotSpeed = 0.03;
            public static double effect = 25;
        }

        public static class Coin {
            public static double duration = 2600;
            public static double lineDuration = 590;
        }

        public static class Player {
            public static double maxSpeed = 0.1248;
            public static double maxStrafeSpeed = 0.0715;
            public static double minSpeed = 0.0409;
            public static int life = 6;
            public static double afterDmgTime = 595;
            public static int bullets = 80;
        }

        public static class EnPlane {
            public static double speed = 0.09;
            public static double shiftSpeed = 0.06;
            public static double turnSpeed = 0.01;
            public static int life = 2;
        }

        public static class Heli {
            public static double maxSpeed = 0.055;
            public static int life = 2;
            public static double bladesK = 0.0343;
        }

        public static class Tank {
            public static double turnDistance = 1430;
            public static double speed = 0.018;
            public static int life = 3;
            public static double stayPeriod = 3200;
            public static double minMovePeriod = 3250;
            public static double bulletSpeed = 0.15;
            /* the more wish the less result */
            public static double wishForPlayer = 0.6;
            public static double turretTime = 350;
        }

        public static class Boss1 {
            public static int life = 12;
            public static double maxSpeed = 0.112;
            public static double maxStrafeSpeed = 0.045;
            public static double minSpeed = 0.038;
            public static double shotInterval = 1060;
            public static double bulletSpeed = 0.083;
            public static int rewardCoins = 17;
        }

        public static class Boss2 {
            public static double maxSpeed = 0.08;
            public static double maxStrafeSpeed = 0.09;
            public static double minSpeed = 0.041;
            public static double bulletSpeed = 0.052;
            public static int paneLife = 13;
            public static double headYSpeed = 0.103;
            public static double headXSpeed = 0.1024;
            public static int gunLife = 2;
            public static int wellLife = 1;
            public static double gunPartSpeed = 0.0103;
            public static double wellChaoticSpeed = 0.0103;
            public static double wellChaoticASpeed = 0.0107;
            public static double wellMaxRadius = 35;
            public static double wellYShift = 27.5;
            public static int rewardCoins = 20;
        }

        public static class Cannon {
            public static double shotInterval = 2400;
            public static double ballSpeed = 0.09;
        }

        public static class Bullet {
            public static double speed = 0.3600;
            public static final double startShift = 3;
            public static double growth = 0.03;
        }
    }

    public static class Bcks {
        public static double waterTimeK = 500;
        public static double waterWave = 0.509;
        public static double waterXWaveTimeK = 500;

        public static class YellowBorder {
            public static boolean bck4 = true;
            public static boolean warfloor = true;
        }
    }

    public static class Damages {
        public static double bullet = 18.3;
        public static double square = 11.5;
        public static double monster = 18;
        public static double cannonBall = 50;
    }

    public static class Gfx {
        public static double dirtK = 0.46;
        public static double restDirtK = 1 - dirtK;
        public static final boolean dirtOn = true;
        public static boolean dirtGround = true;
        public static double groundDirtK = 0.08;
    }
}
