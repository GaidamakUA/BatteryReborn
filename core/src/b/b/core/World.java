/* refactoring0 */
package b.b.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import b.b.Battery;
import b.b.core.loader.WorldLoader;
import b.b.core.objs.ChanSquare;
import b.b.gfx.Gfx;
import b.b.monsters.Monster;
import b.b.monsters.Player;
import b.b.monsters.items.DrawableLibGDX;
import b.gfx.Screen;

public class World {
    public Gfx gfx;
    public Battery btr;
    public List<Monster> objectsToAdd;
    public List<Monster> activeObjects;
    public List<Monster> objectsToRemove;
    public List<Monster> objectsToAddInTime;
    public List<ChanSquare> notMonsters;
    public int nextId;

    public int width;
    public int height;

    private WorldSquare[][] map;

    public int lvl;

    public World(Gfx gfx) {
        nextId = 0;
        this.gfx = gfx;
        btr = gfx.battery;
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
        activeObjects.add(player);
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
                res.addAll(get(xx, yy).objects);
            }
        }
        return new ArrayList<Drawable>(res);
    }

    public List<Drawable> getChangeablesOnScreen() {
        Set<Drawable> res = new HashSet<Drawable>();
        Screen scr = btr.screen;
        int yBorder = scr.getYBorder();
        int xStart = 0;
        int xBorder = scr.getXBorder();
        for (int yy = scr.getStartY(); yy < yBorder; yy++) {
            for (int xx = xStart; xx < xBorder; xx++) {
                List<Drawable> drawables = map[yy][xx].objects;
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

    public List<DrawableLibGDX> getDrawablesOnScreen() {
        Set<DrawableLibGDX> res = new HashSet<DrawableLibGDX>();
        Screen scr = btr.screen;
        int yBorder = scr.getYBorder();
        int xStart = 0;
        int xBorder = scr.getXBorder();
        for (int yy = scr.getStartY(); yy < yBorder; yy++) {
            for (int xx = xStart; xx < xBorder; xx++) {
                List<Drawable> drawables = map[yy][xx].objects;
                for (Drawable d : drawables) {
                    if (d instanceof DrawableLibGDX) {
                        res.add((DrawableLibGDX) d);
                    }
                }
            }
        }
        List<DrawableLibGDX> list = new ArrayList<DrawableLibGDX>(res);
        return list;
    }

    public void removeFromMap(Drawable obj) {
        int yBorder = yMapBorder(obj);
        int xBorder = xMapBorder(obj);
        for (int y = yMapStart(obj); y < yBorder; y++) {
            for (int x = xMapStart(obj); x < xBorder; x++) {
                List<Drawable> drawables = get(x, y).objects;
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
                List<Drawable> objs = get(x, y).objects;
                if (!objs.contains(obj)) {
                    objs.add(obj);
                }
            }
        }
    }

    public final void activateMonsters() {
        for (int i = 0; i < objectsToAddInTime.size(); i++) {
            Monster m = objectsToAddInTime.get(i);
            if (m.yEnd() + Config.activationDistance >= btr.screen.cameraY()) {
                objectsToAdd.add(m);
                addToMap(m);
                objectsToAddInTime.remove(i--);
            }
        }
        int yStart = btr.screen.getStartY();
        int yBorder = btr.screen.getYBorder();
        notMonsters.clear();
        for (int y = yStart; y < yBorder; y++) {
            for (int x = 0; x < width; x++) {
                List<Drawable> list = map[y][x].objects;
                for (Drawable d : list) {
                    if (d instanceof ChanSquare && !notMonsters.contains(d)) {
                        notMonsters.add((ChanSquare) d);
                    }
                }
            }
        }
    }

    private int xMapStart(Drawable obj) {
        return (int) ((obj.x - obj.halfWidth)) / Config.squareSize;
    }

    private int yMapStart(Drawable obj) {
        return (int) ((obj.y - obj.halfHeight)) / Config.squareSize;
    }

    private int xMapBorder(Drawable obj) {
        return (int) ((obj.x + obj.halfWidth) - 0.0001) / Config.squareSize + 1;
    }

    private int yMapBorder(Drawable obj) {
        return (int) ((obj.y + obj.halfHeight) - 0.0001) / Config.squareSize + 1;
    }
}
