/* refactoring0 */
package b.b.core.loader;

import b.b.core.*;
import b.b.core.objs.YellowBorder;
import b.b.gfx.Gfx;
import b.gfx.Sprite;
import b.util.Utils;

import java.util.List;

public class BrickManager {
    private WorldSquare[][] map;
    private int w;
    private int h;
    private Gfx gfx;
    private World world;
    private Sprite sprite;

    protected BrickManager(WorldSquare[][] map, World world, Sprite sprite) {
        this.world = world;
        gfx = world.gfx;
        this.sprite = sprite;
        this.map = map;
        h = map.length;
        w = map[0].length;
    }

    protected final void prepare() {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                List<Drawable> objs = map[y][x].objects;
                for (Drawable o : objs) {
                    if (o instanceof Square) {
                        if (((Square) o).sprite.name().startsWith("brick")) {
                            ((Square) o).sprite = gfx.getSprite(prepareBrick(x, y));
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

    private void yellowBorder() {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (isGround(x, y)) {
                    if (isWall(y - 1, x - 1) || isWall(y - 1, x) || isWall(y - 1, x + 1) ||
                            isWall(y, x - 1) || isWall(y, x) || isWall(y, x + 1) ||
                            isWall(y + 1, x - 1) || isWall(y + 1, x) || isWall(y + 1, x + 1)) {
                        YellowBorder border = new YellowBorder(gfx.getSprite("yellow_border"),
                                x, y, world, false);
                        map[y][x].objects.add(border);
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

    private boolean isGround(int x, int y) {
        List<Drawable> objs = map[y][x].objects;
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
    }

    private boolean isWall(int y, int x) {
        if (isPointOutOfBounds(y, x)) {
            return false;
        }
        for (Drawable d : map[y][x].objects) {
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

    private void prepareWalls() {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                List<Drawable> objs = map[y][x].objects;
                for (Drawable o : objs) {
                    if (o instanceof Square) {
                        String name = ((Square) o).sprite.name();
                        if (name.startsWith("brick") && !name.equals("brickbig")) {
                            ((Square) o).sprite = gfx.getSprite(prepareBrickWall(x, y));
                        }
                    }
                }
            }
        }
    }

    private void anotherBricks() {
        int offset = 0;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                List<Drawable> objs = map[y][x].objects;
                for (Drawable o : objs) {
                    if (o instanceof Square) {
                        String name = ((Square) o).sprite.name();
                        if (name.startsWith("brick")) {
                            int c = sprite.pixels[offset];
                            if (c != 0xff808040) {
                                if (Utils.rnd() < 0.5) {
                                    ((Square) o).sprite = gfx.getSprite("c_" + name);
                                } else if (Utils.rnd() < 0.5) {
                                    ((Square) o).sprite = gfx.getSprite("s_" + name);
                                } else {
                                    ((Square) o).sprite = gfx.getSprite("ss_" + name);
                                }
                            }
                        }
                    }
                }
                offset++;
            }
        }
    }

    private void serveCrossingWalls() {
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

    private void prepareBorders() {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Square brickbig = getBrickSquare(x, y);
                if (brickbig != null && brickbig.sprite.name().equals("brickbig")) {
                    if (brorbo(x - 1, y) && brorbo(x, y - 1) && brorbo(x + 1, y) && brorbo(x, y + 1)) {
                        if (!(isSprite(x - 1, y, "brick", "|") || isSprite(x + 1, y, "brick", "|") ||
                                isSprite(x, y - 1, "brick", "-") || isSprite(x, y + 1, "brick", "-"))) {
                            brickbig.sprite = gfx.getSprite("borderno");
                        }
                    }
                }
            }
        }
    }

    private void drawBorders() {
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
                                    b.sprite = gfx.getSprite("borderno");
                                } else {
                                    b.sprite = gfx.getSprite("border2");
                                }
                            } else {/*nodown left up */
                                if (down) {
                                    b.sprite = gfx.getSprite("border1");
                                } else {
                                    b.sprite = gfx.getSprite("bordercorner2");
                                }
                            }
                        } else {/*noup left*/
                            if (right) {
                                if (down) {
                                    b.sprite = gfx.getSprite("border0");
                                } else {
                                    b.sprite = gfx.getSprite("bordertube-");
                                }
                            } else {/*noup noright left*/
                                if (down) {
                                    b.sprite = gfx.getSprite("bordercorner1");
                                } else {
                                    b.sprite = gfx.getSprite("borderalmost1");
                                }
                            }
                        }
                    } else {/*noleft */
                        if (up) {
                            if (right) {
                                if (down) {
                                    b.sprite = gfx.getSprite("border3");
                                } else {
                                    b.sprite = gfx.getSprite("bordercorner3");
                                }
                            } else {/*noleft noright*/
                                if (down) {
                                    b.sprite = gfx.getSprite("bordertube|");
                                } else {
                                    b.sprite = gfx.getSprite("borderalmost2");
                                }
                            }
                        } else {/*noleft noup*/
                            if (right) {
                                if (down) {
                                    b.sprite = gfx.getSprite("bordercorner0");
                                } else {
                                    b.sprite = gfx.getSprite("borderalmost3");
                                }
                            } else {/*noleft noup noright*/
                                if (down) {
                                    b.sprite = gfx.getSprite("borderalmost0");
                                } else {
                                    b.sprite = gfx.getSprite("bordered");
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
    private void serveCrossingBrick(Square brick) {
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
                br.sprite = gfx.getSprite("brickbig");
            } else {
                brick.sprite = gfx.getSprite("brickbig");
            }
        }
    }

    /**
     * Brick should be - or |
     */
    private int getWallLength(Square brick) {
        String name = brick.sprite.name();
        int x = (int) (brick.x / Config.squareSize);
        int y = (int) (brick.y / Config.squareSize);
        int length = 1;
        if (name.endsWith("|")) {
            for (int xx = x + 1; xx < w; xx++) {
                Square br = this.getBrickSquare(xx, y);
                if (br == null || !br.sprite.name().endsWith("|")) {
                    break;
                } else {
                    length++;
                }
            }
        } else {
            for (int yy = y - 1; yy >= 0; yy--) {
                Square br = this.getBrickSquare(x, yy);
                if (br == null || !br.sprite.name().endsWith("-")) {
                    break;
                } else {
                    length++;
                }
            }
            for (int xx = x + 1; xx < w; xx++) {
                Square br = this.getBrickSquare(xx, y);
                if (br == null || !br.sprite.name().endsWith("-")) {
                    break;
                } else {
                    length++;
                }
            }
        }
        return length;
    }

    private String prepareBrick(int x, int y) {
        if (brick(x - 1, y) && brick(x + 1, y)) {
            if (notBrick(x, y - 1) || notBrick(x, y + 1)) {
                if (y / 2 * 2 == y) {
                    return "brick1o|";
                } else {
                    return "brick1|";
                }
            } else {
                return "brickbig";
            }
        } else if (brick(x, y - 1) && brick(x, y + 1)) {
            if (notBrick(x - 1, y) || notBrick(x + 1, y)) {
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
    private String prepareBrickWall(int x, int y) {
        Square brick = getBrickSquare(x, y);
        String name = brick.sprite.name();
        if (name.endsWith("|")) {
            if (brickWall(x - 1, y)) {
                if (brickWall(x + 1, y)) {
                    name = name.replaceFirst("1", "3");
                } else {
                    name = name.replaceFirst("1", "");
                }
            } else if (brickWall(x + 1, y)) {
                name = name.replaceFirst("1", "2");
            }
        } else {
            if (brickWall(x, y - 1)) {
                if (brickWall(x, y + 1)) {
                    name = name.replaceFirst("1", "3");
                } else {
                    name = name.replaceFirst("1", "");
                }
            } else if (brickWall(x, y + 1)) {
                name = name.replaceFirst("1", "2");
            }
        }
        return name;
    }

    private Square getSquare(int x, int y, String starts) {
        List<Drawable> objs = map[y][x].objects;
        for (Drawable d : objs) {
            if (d instanceof Square) {
                if (((Square) d).sprite.name().startsWith(starts)) {
                    return (Square) d;
                }
            }
        }
        return null;
    }

    private Square getBrickSquare(int x, int y) {
        return getSquare(x, y, "brick");
    }

    private boolean brickWall(int x, int y) {
        List<Drawable> objs = map[y][x].objects;
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
    }

    private boolean brickEnds(int x, int y, String ends) {
        if (isSprite(x, y, "brick")) {
            Square brick = getBrickSquare(x, y);
            if (brick == null) {
                return false;
            }
            return brick.sprite.name().endsWith(ends);
        } else {
            return false;
        }
    }

    private boolean brick(int x, int y) {
        return isSprite(x, y, "brick");
    }

    private boolean brorbo(int x, int y) {
        return isSprite(x, y, "brick") || isSprite(x, y, "border");
    }

    private boolean isSprite(int x, int y, String start, String ends) {
        return isSprite(x, y, start) && isSpriteEnds(x, y, ends);
    }

    private boolean isSprite(int x, int y, String start) {
        if (isPointOutOfBounds(x, y)) {
            return false;
        }
        List<Drawable> objs = map[y][x].objects;
        for (Drawable o : objs) {
            if (o instanceof Square) {
                if (((Square) o).sprite.name().startsWith(start)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isSpriteEnds(int x, int y, String ends) {
        List<Drawable> objs = map[y][x].objects;
        for (Drawable o : objs) {
            if (o instanceof Square) {
                if (((Square) o).sprite.name().endsWith(ends)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean notBrick(int x, int y) {
        if (isPointOutOfBounds(x, y)) {
            return false;
        }
        List<Drawable> objs = map[y][x].objects;
        for (Drawable o : objs) {
            if (o instanceof Square) {
                if (((Square) o).sprite.name().startsWith("brick")) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isPointOutOfBounds(int x, int y) {
        return y < 0 || map.length <= y || x < 0 || map[y].length <= x;
    }
}
