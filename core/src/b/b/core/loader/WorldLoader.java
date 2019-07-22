/* refactoring0 */
package b.b.core.loader;

import b.b.Battery;
import b.b.core.*;
import b.b.core.objs.Cannon;
import b.b.core.objs.ChanSquare;
import b.b.core.objs.LandingGround;
import b.b.core.objs.Water;
import b.b.gfx.Gfx;
import b.b.monsters.*;
import b.b.monsters.bosses.Boss1AI;
import b.b.monsters.bosses.Boss2AI;
import b.b.monsters.items.FirstAid;
import b.gfx.C;
import b.gfx.Screen;
import b.gfx.Sprite;
import b.util.U77;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldLoader {
    private Battery battery;
    int lvl;
    private Gfx gfx;
    private World world;
    private int w;
    private int h;
    private WorldSquare[][] map;

    public WorldLoader(World world, boolean oldPlayer) {
        lvl = world.trueLevel();
        this.world = world;
        gfx = world.gfx;
        battery = gfx.battery;
        loadPart1(oldPlayer);
        loadMain();
        specialMonsters();
        new BrickManager(map, world, gfx.getSprite("lvl" + lvl)).prepare();
        battery.timeWhenLevelLoaded = battery.time.time;
        battery.timeWhenLevelCompleted = 0;
        U77.dropRandom();
        battery.kbd.clear(battery);
        battery.logger.log("lvl " + lvl);
        System.gc();
    }

    private void loadPart1(boolean oldPlayer) {
        Sprite sprite = gfx.getSprite("lvl" + lvl);
        w = world.width = sprite.w;
        h = world.height = sprite.h;
        map = new WorldSquare[h][w];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                map[y][x] = new WorldSquare(x, y, battery);
            }
        }
        world.setMap(map);
        world.objectsToAdd = new ArrayList<Monster>();
        world.activeObjects = new ArrayList<Monster>();
        world.objectsToRemove = new ArrayList<Monster>();
        world.objectsToAddInTime = new ArrayList<Monster>();
        world.notMonsters = new ArrayList<ChanSquare>();
        if (battery.player == null || battery.player.life <= 0) {
            Screen scr = battery.screen;
            int[] buf = scr.b;
            scr.init(scr.w, scr.h);
            scr.b = buf;
            if (!oldPlayer) {
                gfx.battery.player = new Player(battery.kbd, world, 0, 3, 0, new PlayerExtras());
                gfx.battery.logger.log("newgame " + U77.sprecision(gfx.battery.time.time));
            } else {
                Player p = gfx.battery.player;
                gfx.battery.player = new Player(battery.kbd, world, p.getScores(), p.lifes,
                        p.getCoins(), p.extras);
                gfx.battery.logger.log("newlife " + U77.sprecision(gfx.battery.time.time));
            }
        }
    }

    private void loadMain() {
        Random random = new Random(77);
        int offset = 0;
        Sprite sprite = gfx.getSprite("lvl" + lvl);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int c = sprite.pixels[offset++];
                List<Drawable> list = map[y][x].objects;
                if (c == 0xff0000ff) {
                    list.add(new Water(x, y, world));
                } else if (c == 0xffff0000 || c == 0xff808040) {
                    list.add(new Square(gfx.getSprite("brickbig"), x, y, world, true, 1));
                } else if (c == 0xffc0c0c0) {
                    list.add(new Square(gfx.getSprite("ground"), x, y, world, false, 0));
                } else if (c == 0xff808080) {
                    list.add(new Square(gfx.getSprite("warfloor"), x, y, world, false, 0));
                } else if (c == 0xffc0e6c0) {
                    list.add(new Square(gfx.getSprite("bck0"), x, y, world, false, 0));
                } else if (c == 0xff8000ff) {
                    Sprite spr = gfx.getSprite("grass");
                    list.add(new Square(spr, x, y, world, false, 0));
                } else if (c == 0xff008080) {
                    throw new RuntimeException("corner should not be defined on image " +
                            "map for a while x:" + x + " y:" + y);
                } else if (c == 0xff004040) {
                    list.add(new Square(gfx.getSprite("trap|"), x, y, world, false, 0));
                } else if (c == 0xff008282) {
                    list.add(new Square(gfx.getSprite("trap-"), x, y, world, false, 0));
                } else if (c == 0xffa2a2a2) {
                    list.add(new Square(gfx.getSprite("bck4_" + random.nextInt(4)), x, y,
                            world, false, 0));
                } else if (c == 0xff804000) {
                    int part = getLandingPart(x, y);
                    if (part == 0) {
                        list.add(new LandingGround(x, y, world));
                    } else if (part == 1) {
                        list.add(getLG(map[y][x - 1].objects));
                    } else if (part == 2) {
                        list.add(getLG(map[y - 1][x - 1].objects));
                    } else {
                        list.add(getLG(map[y - 1][x].objects));
                    }
                } else if (c == 0xffff0080) {
                    list.add(background(x, y));
                    firstaid(x, y);
                } else if (c == 0xff008000) {
                    Drawable sq = background(x, y);
                    list.add(sq);
                    heli(x, y);
                } else if (c == 0xff00ffff) {
                    list.add(background(x, y));
                    enplane(x, y);
                } else if (c == 0xff00ff80) {
                    list.add(background(x, y));
                    tank(x, y);
                } else if (c == 0xff800080) {
                    list.add(background(x, y));
                    if (lvl == 3 || lvl == 103 || lvl == 203) {
                        boss1(x, y);
                    } else {
                        boss2(x, y);
                    }
                } else if (c == 0xffff8040) {
                    System.out.println("the first cannon");
                    cannon(list, x, y, 1);
                } else if (c == 0xffff8041) {
                    System.out.println("the second cannon");
                    cannon(list, x, y, 3);
                } else if (c != 0xff000000) {
                    throw new RuntimeException("WorldLoader.load lvl (true number):" + lvl +
                            " xy(" + x + "," + y + ") color:" + C.string(c) + " should not be");
                }
            }
        }
    }

    private int getLandingPart(int x, int y) {
        if (x == 0 || !contains(map[y][x - 1].objects, "landingg")) {
            /*left*/
            if (y == 0 || !contains(map[y - 1][x].objects, "landingg")) {
                /*up*/
                return 0;
            } else return 3;
        } else {
            /*right*/
            if (y == 0 || !contains(map[y - 1][x].objects, "landingg")) {
                /*up*/
                return 1;
            } else return 2;
        }
    }

    private LandingGround getLG(List<Drawable> list) {
        for (Drawable d : list)
            if (d instanceof LandingGround) {
                return (LandingGround) d;
            }
        throw new RuntimeException("have not found LG in this list");
    }

    private void specialMonsters() {
/*  if (lvl==1) {
      heli(w, -2, 72);
    }*/
    }

    private Drawable background(int x, int y) {
        /* warfloor ground bck0 grass trap| water trap- bck4*/
        int[] wcg = new int[8];
        for (int i = 0; i < 8; i++) wcg[i] = 0;
        if (x > 0) {
            background(map[y - 1][x - 1].objects, wcg);
            background(map[y][x - 1].objects, wcg);
        } else if (x < h - 1) {
            background(map[y - 1][x + 1].objects, wcg);
        }
        background(map[y - 1][x].objects, wcg);
        int max = U77.maxIndex(wcg);
        if (max == 0) {
            return new Square(gfx.getSprite("warfloor"), x, y, world, false, 0);
        } else if (max == 1) {
            return new Square(gfx.getSprite("ground"), x, y, world, false, 0);
        } else if (max == 2) {
            return new Square(gfx.getSprite("bck0"), x, y, world, false, 0);
        } else if (max == 3) {
            return new Square(gfx.getSprite("grass"), x, y, world, false, 0);
        } else if (max == 4) {
            return new Square(gfx.getSprite("trap|"), x, y, world, false, 0);
        } else if (max == 5) {
            return new Water(x, y, world);
        } else if (max == 6) {
            return new Square(gfx.getSprite("trap-"), x, y, world, false, 0);
        } else if (max == 7) {
            return new Square(gfx.getSprite("bck4_0"), x, y, world, false, 0);
        } else {
            throw new RuntimeException("Should not be: unknown background lvl:" +
                    lvl + " x:" + x + "y:" + y);
        }
    }

    private static boolean contains(List<Drawable> list, String starts) {
        for (Drawable d : list) {
            if (d instanceof Square) {
                Square sq = (Square) d;
                if (sq.sprite.name().startsWith(starts)) return true;
            }
        }
        return false;
    }

    private static void background(List<Drawable> square, int[] wcg) {
        for (Drawable d : square) {
            if (d instanceof Square) {
                Square s = (Square) d;
                if (s.sprite.name().startsWith("warfloor")) {
                    wcg[0]++;
                } else if (s.sprite.name().startsWith("ground")) {
                    wcg[1]++;
                } else if (s.sprite.name().startsWith("bck0")) {
                    wcg[2]++;
                } else if (s.sprite.name().startsWith("grass")) {
                    wcg[3]++;
                } else if (s.sprite.name().equals("trap|")) {
                    wcg[4]++;
                } else if (s.sprite.name().startsWith("wat")) {
                    wcg[5]++;
                } else if (s.sprite.name().equals("trap-")) {
                    wcg[6]++;
                } else if (s.sprite.name().startsWith("bck4")) {
                    wcg[7]++;
                }
            }
        }
    }

    private void enplane(int x, int y) {
        EnPlane enplane = new EnPlane(squareCenter(x), squareCenter(y), world);
        world.objectsToAddInTime.add(enplane);
    }

    private void heli(int x, int y) {
        Helicopter helicopter = new Helicopter(squareCenter(x), squareCenter(y), world, 1);
        world.objectsToAddInTime.add(helicopter);
    }

    private void tank(int x, int y) {
        Tank tank = new Tank(squareCenter(x), squareCenter(y), world, 2);
        world.objectsToAddInTime.add(tank);
    }

    private void cannon(List list, int x, int y, int dir) {
        System.out.println("cannon " + dir);
        list.add(background(x, y));
        list.add(new Cannon(dir, x, y, world));
    }

    private void boss1(int x, int y) {
        Boss1AI boss = new Boss1AI(squareCenter(x), squareCenter(y), world);
        world.objectsToAddInTime.add(boss);
    }

    private void boss2(int x, int y) {
        Boss2AI boss = new Boss2AI(squareCenter(x), squareCenter(y), world);
        world.objectsToAddInTime.add(boss);
    }

    private void firstaid(int x, int y) {
        FirstAid fa = new FirstAid(squareCenter(x), squareCenter(y), world);
        world.objectsToAddInTime.add(fa);
    }

    private static double squareCenter(int square) {
        return (double) square * Config.squareSize + Config.hSquareSize;
    }
}
