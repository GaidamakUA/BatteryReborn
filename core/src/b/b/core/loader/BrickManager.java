/* refactoring0 */
package b.b.core.loader;

import b.b.core.*;
import b.b.core.objs.YellowBorder;
import b.b.gfx.Gfx;
import b.gfx.Sprite;
import b.util.U77;

import java.util.List;

public class BrickManager {
    private WorldSquare[][] map;
    private int w;
    private int h;
    private Gfx g;
    private World world;
    private Sprite s;

    protected BrickManager(WorldSquare[][] map, World world, Sprite s) {
        this.world = world;
        g = world.g;
        this.s = s;
        this.map = map;
        h = map.length;
        w = map[0].length;
    }

    protected final void prepare() {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                List<Drawable> objs = map[y][x].objs;
                for (Drawable o : objs) {
                    if (o instanceof Square) {
                        if (((Square) o).sprite.name().startsWith("brick")) {
                            ((Square) o).sprite = g.getSprite(prepareBrick(x, y));
                        }
                    }
                }
            }
        }
        prepareWalls();
        serveCrossingWalls();
        prepareBorders();
        drawBorders();
        anotherBricks();
        yellowBorder();
    }

    private final void yellowBorder() {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (isGround(x, y)) {
                    if (isWall(y - 1, x - 1) || isWall(y - 1, x) || isWall(y - 1, x + 1) ||
                            isWall(y, x - 1) || isWall(y, x) || isWall(y, x + 1) ||
                            isWall(y + 1, x - 1) || isWall(y + 1, x) || isWall(y + 1, x + 1)) {
                        YellowBorder border = new YellowBorder(g.getSprite("yellow_border"),
                                x, y, world, false);
                        map[y][x].objs.add(border);
                        border.leftUp = isWall(y - 1, x - 1);
                        border.up = isWall(y - 1, x);
                        border.upRight = isWall(y - 1, x + 1);
                        border.right = isWall(y, x + 1);
                        border.rightDown = isWall(y + 1, x + 1);
                        border.down = isWall(y + 1, x);
                        border.downLeft = isWall(y + 1, x - 1);
                        border.left = isWall(y, x - 1);
                    }
                }
            }
        }
    }

    private final boolean isGround(int x, int y) {
        try {
            List<Drawable> objs = map[y][x].objs;
            for (Drawable o : objs) {
                if (o instanceof Square) {
                    String name = ((Square) o).sprite.name();
                    if ((Config.Bcks.YellowBorder.bck4 && name.startsWith("bck4")) ||
                            (Config.Bcks.YellowBorder.warfloor && name.startsWith("warfloor"))) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private final boolean isWall(int y, int x) {
        if (x == -1 || y == -1 || x == w || y == h) return false;
        for (Drawable d : map[y][x].objs) {
            if (d instanceof Square) {
                String name = ((Square) d).sprite.name();
                if ((Config.Bcks.YellowBorder.warfloor && name.startsWith("warfloor"))
                        || (Config.Bcks.YellowBorder.bck4 && name.startsWith("bck4"))) {
                    return false;
                }
            }
        }
        return true;
    }

    private final void prepareWalls() {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                List<Drawable> objs = map[y][x].objs;
                for (Drawable o : objs) {
                    if (o instanceof Square) {
                        String name = ((Square) o).sprite.name();
                        if (name.startsWith("brick") && !name.equals("brickbig")) {
                            ((Square) o).sprite = g.getSprite(prepareBrickWall(x, y));
                        }
                    }
                }
            }
        }
    }

    private final void anotherBricks() {
        int offset = 0;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                List<Drawable> objs = map[y][x].objs;
                for (Drawable o : objs) {
                    if (o instanceof Square) {
                        String name = ((Square) o).sprite.name();
                        if (name.startsWith("brick")) {
                            int c = s.pixels[offset];
                            if (c != -8355776/*0xff804040*/) {
                                if (U77.rnd() < 0.5) {
                                    ((Square) o).sprite = g.getSprite("c_" + name);
                                } else if (U77.rnd() < 0.5) {
                                    ((Square) o).sprite = g.getSprite("s_" + name);
                                } else {
                                    ((Square) o).sprite = g.getSprite("ss_" + name);
                                }
                            }
                        }
                    }
                }
                offset++;
            }
        }
    }

    private final void serveCrossingWalls() {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Square brick = getBrickSquare(x, y);
                if (brick != null && !brick.sprite.name().equals("brickbig")) {
                    String name = brick.sprite.name();
                    String ends = name.substring(name.length() - 1);
                    if (ends.equals("|")) {
                        ends = "-";
                    } else {
                        ends = "|";
                    }
                    if (brickEnds(x - 1, y, ends) || brickEnds(x + 1, y, ends) ||
                            brickEnds(x, y - 1, ends) || brickEnds(x, y + 1, ends)) {
                        serveCrossingBrick(brick);
                    }
                }
            }
        }
    }

    private final void prepareBorders() {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Square brickbig = getBrickSquare(x, y);
                if (brickbig != null && brickbig.sprite.name().equals("brickbig")) {
                    if (brorbo(x - 1, y) && brorbo(x, y - 1) && brorbo(x + 1, y) && brorbo(x, y + 1)) {
                        if (!(isSprite(x - 1, y, "brick", "|") || isSprite(x + 1, y, "brick", "|") ||
                                isSprite(x, y - 1, "brick", "-") || isSprite(x, y + 1, "brick", "-"))) {
                            brickbig.sprite = g.getSprite("borderno");
                        }
                    }
                }
            }
        }
    }

    private final void drawBorders() {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Square b = getSquare(x, y, "border");
                if (b != null) {
                    boolean left = (getSquare(x - 1, y, "border") != null);
                    boolean up = (getSquare(x, y - 1, "border") != null);
                    boolean right = (getSquare(x + 1, y, "border") != null);
                    boolean down = (getSquare(x, y + 1, "border") != null);
                    if (left) {
                        if (up) {
                            if (right) {
                                if (down) {
                                    b.sprite = g.getSprite("borderno");
                                } else {
                                    b.sprite = g.getSprite("border2");
                                }
                            } else {/*nodown left up */
                                if (down) {
                                    b.sprite = g.getSprite("border1");
                                } else {
                                    b.sprite = g.getSprite("bordercorner2");
                                }
                            }
                        } else {/*noup left*/
                            if (right) {
                                if (down) {
                                    b.sprite = g.getSprite("border0");
                                } else {
                                    b.sprite = g.getSprite("bordertube-");
                                }
                            } else {/*noup noright left*/
                                if (down) {
                                    b.sprite = g.getSprite("bordercorner1");
                                } else {
                                    b.sprite = g.getSprite("borderalmost1");
                                }
                            }
                        }
                    } else {/*noleft */
                        if (up) {
                            if (right) {
                                if (down) {
                                    b.sprite = g.getSprite("border3");
                                } else {
                                    b.sprite = g.getSprite("bordercorner3");
                                }
                            } else {/*noleft noright*/
                                if (down) {
                                    b.sprite = g.getSprite("bordertube|");
                                } else {
                                    b.sprite = g.getSprite("borderalmost2");
                                }
                            }
                        } else {/*noleft noup*/
                            if (right) {
                                if (down) {
                                    b.sprite = g.getSprite("bordercorner0");
                                } else {
                                    b.sprite = g.getSprite("borderalmost3");
                                }
                            } else {/*noleft noup noright*/
                                if (down) {
                                    b.sprite = g.getSprite("borderalmost0");
                                } else {
                                    b.sprite = g.getSprite("bordered");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Brick is | or - and it has neighbor of another type
     */
    private final void serveCrossingBrick(Square brick) {
        int x = (int) (brick.x / Config.squareSize);
        int y = (int) (brick.y / Config.squareSize);
        int length = getWallLength(brick);
        String name = brick.sprite.name();
        String ends = name.substring(name.length() - 1);
        if (ends.equals("|")) {
            ends = "-";
        } else {
            ends = "|";
        }
        Square br = null;
        if (!ends.equals("-")) {
            Square b = getBrickSquare(x - 1, y);
            if (b != null && b.sprite.name().endsWith(ends)) {
                br = b;
            } else {
                b = getBrickSquare(x + 1, y);
                if (b != null && b.sprite.name().endsWith(ends)) {
                    br = b;
                }
            }
        } else {
            Square b = getBrickSquare(x, y - 1);
            if (b != null && b.sprite.name().endsWith(ends)) {
                br = b;
            } else {
                b = getBrickSquare(x, y + 1);
                if (b != null && b.sprite.name().endsWith(ends)) {
                    br = b;
                }
            }
        }
        if (br != null) {
            if (length > getWallLength(br)) {
                br.sprite = g.getSprite("brickbig");
            } else {
                brick.sprite = g.getSprite("brickbig");
            }
        }
    }

    /**
     * Brick should be - or |
     */
    private final int getWallLength(Square brick) {
        String name = brick.sprite.name();
        int x = (int) (brick.x / Config.squareSize);
        int y = (int) (brick.y / Config.squareSize);
        int length = 1;
        if (name.endsWith("|")) {
            for (int xx = x - 1; xx >= 0; xx--) {
                Square br = getBrickSquare(xx, y);
                if (br == null || !br.sprite.equals("|")) {
                    break;
                } else length++;
            }
            for (int xx = x + 1; xx < w; xx++) {
                Square br = this.getBrickSquare(xx, y);
                if (br == null || !br.sprite.name().endsWith("|")) {
                    break;
                } else length++;
            }
        } else {
            for (int yy = y - 1; yy >= 0; yy--) {
                Square br = this.getBrickSquare(x, yy);
                if (br == null || !br.sprite.name().endsWith("-")) {
                    break;
                } else length++;
            }
            for (int xx = x + 1; xx < w; xx++) {
                Square br = this.getBrickSquare(xx, y);
                if (br == null || !br.sprite.name().endsWith("-")) {
                    break;
                } else length++;
            }
        }
        return length;
    }

    private final String prepareBrick(int x, int y) {
        Square brick = getBrickSquare(x, y);
        if (brick(x - 1, y) && brick(x + 1, y)) {
            if (notbrick(x, y - 1) || notbrick(x, y + 1)) {
                if (y / 2 * 2 == y) {
                    return "brick1o|";
                } else {
                    return "brick1|";
                }
            } else {
                return "brickbig";
            }
        } else if (brick(x, y - 1) && brick(x, y + 1)) {
            if (notbrick(x - 1, y) || notbrick(x + 1, y)) {
                if (x / 2 * 2 == x) {
                    return "brick1o-";
                } else {
                    return "brick1-";
                }
            } else {
                return "brickbig";
            }
        } else {
            return "brickbig";
        }
    }

    /**
     * The brick is not brickbig
     */
    private final String prepareBrickWall(int x, int y) {
        Square brick = getBrickSquare(x, y);
        String name = brick.sprite.name();
        if (name.endsWith("|")) {
            if (brickwall(x - 1, y)) {
                if (brickwall(x + 1, y)) {
                    name = name.replaceFirst("1", "3");
                } else {
                    name = name.replaceFirst("1", "");
                }
            } else if (brickwall(x + 1, y)) {
                name = name.replaceFirst("1", "2");
            }
        } else {
            if (brickwall(x, y - 1)) {
                if (brickwall(x, y + 1)) {
                    name = name.replaceFirst("1", "3");
                } else {
                    name = name.replaceFirst("1", "");
                }
            } else if (brickwall(x, y + 1)) {
                name = name.replaceFirst("1", "2");
            }
        }
        return name;
    }

    private final Square getSquare(int x, int y, String starts) {
        try {
            List<Drawable> objs = map[y][x].objs;
            for (Drawable d : objs) {
                if (d instanceof Square) {
                    if (((Square) d).sprite.name().startsWith(starts)) {
                        return (Square) d;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private final Square getBrickSquare(int x, int y) {
        return getSquare(x, y, "brick");
    }

    private final boolean brickwall(int x, int y) {
        try {
            List<Drawable> objs = map[y][x].objs;
            for (Drawable o : objs) {
                if (o instanceof Square) {
                    String name = ((Square) o).sprite.name();
                    if (name.startsWith("brick") &&
                            (name.endsWith("|") || name.endsWith("-"))) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private final boolean brickEnds(int x, int y, String ends) {
        if (isSprite(x, y, "brick")) {
            Square brick = getBrickSquare(x, y);
            if (brick == null) return false;
            return brick.sprite.name().endsWith(ends);
        } else {
            return false;
        }
    }

    private final boolean brick(int x, int y) {
        return isSprite(x, y, "brick");
    }

    private final boolean brorbo(int x, int y) {
        return isSprite(x, y, "brick") || isSprite(x, y, "border");
    }

    private final boolean isSprite(int x, int y, String start, String ends) {
        return isSprite(x, y, start) && isSpriteEnds(x, y, ends);
    }

    private final boolean isSprite(int x, int y, String start) {
        try {
            List<Drawable> objs = map[y][x].objs;
            for (Drawable o : objs) {
                if (o instanceof Square) {
                    if (((Square) o).sprite.name().startsWith(start)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private final boolean isSpriteEnds(int x, int y, String ends) {
        try {
            List<Drawable> objs = map[y][x].objs;
            for (Drawable o : objs) {
                if (o instanceof Square) {
                    if (((Square) o).sprite.name().endsWith(ends)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private final boolean notbrick(int x, int y) {
        try {
            List<Drawable> objs = map[y][x].objs;
            for (Drawable o : objs) {
                if (o instanceof Square) {
                    if (((Square) o).sprite.name().startsWith("brick")) {
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }
}
