package b.b.core;

import b.b.Battery;
import b.b.gfx.Gfx;
import b.b.monsters.Player;
import b.util.Str77;

public class Shop {
    private static int y;

    public static boolean on = false;

    public static final void step(Battery btr) {
        if (on) {
            double shopTime = btr.time.time - btr.timeWhenLevelCompleted -
                    Config.Intervals.nextLevelDelay;
            y = -510;
            double speed = 0;
            int hits = 0;
            for (double i = 0; i < shopTime; i += 10) {
                speed += 0.3;
                y += speed;
                if (y > 0) {
                    y = 0;
                    speed = -speed * 0.7;
                    hits++;
                    if (hits == 6) {
                        break;
                    }
                }
            }
            if (y == 0) {
                Player p = btr.player;
                for (String key : btr.kbd.mouse) {
                    int x = Str77.iparam(key, 1);
                    int y = Str77.iparam(key, 2);
                    if (x >= 161 && y >= 99 && x < 224 && y < 162) {
                        if (p.getCoins() >= 100) {
                            p.minusCoins(100);
                            p.lifes++;
                        }
                    } else if (x >= 161 && y >= 173 && x < 224 && y < 236) {
                        if (p.getCoins() >= 20) {
                            p.minusCoins(20);
                            p.extras.immortalities++;
                        }
                    } else if (x >= 159 && y >= 378 && x < 379 && y < 404) {
                        on = false;
                        btr.world.nextLevel();
                        return;
                    }
                }
            }
        }
    }

    public static final void draw(Battery btr) {
        if (on) {
            Gfx gfx = btr.gfx;
            System.arraycopy(gfx.getSprite("shop").b, -y * 510, gfx.b.b, 0,
                    (510 + y) * 510);
            if (y == 0) {
                gfx.b.drawTransp(gfx.getSprite("immune"), 174, 181);
                gfx.font.p("" + btr.player.getCoins(), 210, 328);
            }
        }
    }
}
