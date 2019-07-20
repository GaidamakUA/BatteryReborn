/* refactoring0 */
package b.b.core;

import b.b.Battery;
import b.b.core.loader.WorldLoader;
import b.b.core.objs.ChanSquare;
import b.b.gfx.Gfx;
import b.b.monsters.Monster;
import b.b.monsters.Player;
import b.gfx.Screen;

import java.util.*;

public class World {
    public Gfx g;
    public Battery btr;
    public List<Monster> objsToAdd;
    public List<Monster> activeObjs;
    public List<Monster> objsToRemove;
    public List<Monster> objsToAddInTime;
    public List<ChanSquare> notMonsters;
    public int nextId;

    public int width;
    public int height;

    private WorldSquare[][] map;

    public int lvl;

    public World(Gfx gfx) {
        nextId = 0;
        g = gfx;
        btr = gfx.btr;
        lvl = 1;
        new WorldLoader(this, false);
    }

    public WorldSquare[][] getMap() {
        return map;
    }

    public final int trueLevel() {
        return Config.levels.get(lvl - 1);
    }

    public final void restartLevel() {
        Screen screen = btr.screen;
        screen.init(screen.w, screen.h);
        new WorldLoader(this, true);
        Player player = btr.player;
        btr.player = player;
        player.x = (double) width / 2 * Config.squareSize;
        player.y = (height - 3) * Config.squareSize;
        screen.setCameraY(height * Config.squareSize - screen.h, this);
        activeObjs.add(player);
        addToMap(player);
    }

    public final void nextLevel() {
        lvl++;
        if (lvl == Config.levels.size() + 1) lvl = 1;
        restartLevel();
    }

    public void setMap(WorldSquare[][] map) {
        this.map = map;
    }

    public WorldSquare get(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return new WorldSquare(x, y, btr);
        }
        return map[y][x];
    }

    public List<Drawable> get(Drawable d) {
        Set<Drawable> res = new HashSet<Drawable>();
        int yBorder = (int) (d.yEnd() - 0.00001) / Config.squareSize + 1;
        int xBorder = (int) (d.xEnd() - 0.00001) / Config.squareSize + 1;
        int xStart = (int) d.xStart() / Config.squareSize;
        for (int yy = (int) d.yStart() / Config.squareSize; yy < yBorder; yy++) {
            for (int xx = xStart; xx < xBorder; xx++) {
                res.addAll(get(xx, yy).objs);
            }
        }
        return new ArrayList<Drawable>(res);
    }

    public List<Drawable> getNotChangeable(int xScreen, int yScreen,
                                           int xScreenBorder, int yScreenBorder) {
        Screen scr = btr.screen;
        int x = xScreen;
        int y = scr.camY() + yScreen;
        int xBorder = xScreenBorder;
        int yBorder = scr.camY() + yScreenBorder;
        Set<Drawable> res = new HashSet<Drawable>();
        xBorder = (xBorder - 1) / Config.squareSize + 1;
        yBorder = (yBorder - 1) / Config.squareSize + 1;
        int xStart = (int) (x) / Config.squareSize;
        for (int yy = (int) (y) / Config.squareSize; yy < yBorder; yy++) {
            for (int xx = xStart; xx < xBorder; xx++) {
                List<Drawable> drawables = get(xx, yy).objs;
                for (Drawable d : drawables) {
                    if (!(d instanceof Changeable)) {
                        res.add(d);
                    }
                }
            }
        }
        List<Drawable> list = new ArrayList<Drawable>(res);
        Collections.sort(list);
        return list;
    }

    public List<Drawable> getChangeablesOnScreen() {
        Set<Drawable> res = new HashSet<Drawable>();
        Screen scr = btr.screen;
        int yBorder = scr.getYBorder();
        int xStart = 0;
        int xBorder = scr.getXBorder();
        for (int yy = scr.getStartY(); yy < yBorder; yy++) {
            for (int xx = xStart; xx < xBorder; xx++) {
                List<Drawable> drawables = map[yy][xx].objs;
                for (Drawable d : drawables) {
                    if (d instanceof Changeable) {
                        res.add(d);
                    }
                }
            }
        }
        List<Drawable> list = new ArrayList<Drawable>(res);
        Collections.sort(list);
        return list;
    }

    public void removeFromMap(Drawable obj) {
        int yBorder = yMapBorder(obj);
        int xBorder = xMapBorder(obj);
        for (int y = yMapStart(obj); y < yBorder; y++) {
            for (int x = xMapStart(obj); x < xBorder; x++) {
                List<Drawable> drawables = get(x, y).objs;
                for (int i = 0; i < drawables.size(); i++) {
                    if (drawables.get(i) == obj) {
                        drawables.remove(i);
                    }
                }
            }
        }
    }

    public void addToMap(Drawable obj) {
        int yBorder = yMapBorder(obj);
        int xBorder = xMapBorder(obj);
        for (int y = yMapStart(obj); y < yBorder; y++) {
            for (int x = xMapStart(obj); x < xBorder; x++) {
                List<Drawable> objs = get(x, y).objs;
                if (!objs.contains(obj)) {
                    objs.add(obj);
                }
            }
        }
    }

    public final void activateMonsters() {
        for (int i = 0; i < objsToAddInTime.size(); i++) {
            Monster m = objsToAddInTime.get(i);
            if (m.yEnd() + Config.activationDistance >= btr.screen.cameraY()) {
                objsToAdd.add(m);
                addToMap(m);
                objsToAddInTime.remove(i--);
            }
        }
        int yStart = btr.screen.getStartY();
        int yBorder = btr.screen.getYBorder();
        notMonsters.clear();
        for (int y = yStart; y < yBorder; y++) {
            for (int x = 0; x < width; x++) {
                List<Drawable> list = map[y][x].objs;
                for (Drawable d : list) {
                    if (d instanceof ChanSquare && !notMonsters.contains(d)) {
                        notMonsters.add((ChanSquare) d);
                    }
                }
            }
        }
    }

    private int xMapStart(Drawable obj) {
        return (int) ((obj.x - obj.hw)) / Config.squareSize;
    }

    private int yMapStart(Drawable obj) {
        return (int) ((obj.y - obj.hh)) / Config.squareSize;
    }

    private int xMapBorder(Drawable obj) {
        return (int) ((obj.x + obj.hw) - 0.0001) / Config.squareSize + 1;
    }

    private int yMapBorder(Drawable obj) {
        return (int) ((obj.y + obj.hh) - 0.0001) / Config.squareSize + 1;
    }
}
