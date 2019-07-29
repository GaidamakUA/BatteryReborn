/* refactoring0 */
package b.b.core;

import b.b.core.objs.ChanSquare;
import b.b.core.objs.YellowBorder;
import b.gfx.BufGfx;
import b.gfx.C;
import b.gfx.Sprite;
import b.util.Utils;

/**
 * not obligatory square
 * not obligatory in "right" position
 */
public class Square extends Drawable {
    private static int[] util = new int[Config.squareSize * Config.squareSize];
    public Sprite sprite;

    protected BufGfx b;

    public Square(Sprite s, int xMap, int yMap, World w, boolean isShape,
                  ZLayer lvl) {
        this(s, xMap * Config.squareSize + Config.hSquareSize,
                yMap * Config.squareSize + Config.hSquareSize,
                s.width, s.height, w, isShape, lvl);
        setWH(Config.squareSize, Config.squareSize);
    }

    public Square(Sprite s, int x, int y, int width, int height,
                  World w, boolean isShape, ZLayer lvl) {
        super(x, y, width, height, isShape, lvl, w);
        sprite = s;
        b = w.gfx.bufGfx;
    }

    public void draw() {
        b.drawRangeCheck(sprite, (int) (x - halfWidth),
                (int) (y - halfHeight - screen.cameraY()) + chanSquareBonus());
    }

    protected int chanSquareBonus() {
        if (this instanceof ChanSquare &&
                (world.height * Config.squareSize - screen.cameraY() != 510)) return 1;
        return 0;
    }

    public void draw2(int[] to) {
        int[] p = sprite.pixels;
        if (Config.Gfx.dirtOn) {
            String sn = sprite.name();
            if (sn.startsWith("brick") ||
                    sn.startsWith("s_brick") ||
                    sn.startsWith("ss_brick") ||
                    sn.startsWith("c_brick")) {
                BufGfx buf = new BufGfx(sprite, util);
                buf.effects().dirt(randomSeed);
                p = buf.pixels;
            } else if (sn.equals("ground") && Config.Gfx.dirtGround) {
                new BufGfx(sprite, util).effects().dirt(0xffb5b5b5, randomSeed, Config.Gfx.groundDirtK);
                p = util;
            } else if (sn.startsWith("border")) {
                BufGfx buf = new BufGfx(sprite, util);
                p = buf.pixels;
                int offset = Config.squareSize * Config.squareSize - 1;
                int c;
                double yy = yStart() + Config.squareSize;
                for (int y = Config.squareSize - 1; y >= 0; y--) {
                    yy--;
                    double yK = Math.sin(yy / 11) + Math.sin(yy / 2);
                    double xx = xStart() + Config.squareSize;
                    for (int x = Config.squareSize - 1; x >= 0; x--) {
                        xx--;
                        c = p[offset];
                        if (c == 0xff004080) {
                            double z = ((Math.sin(xx / 11) + Math.sin(xx / 2) + yK + Math.sin((xx + yy) / 17) + Math.cos((xx - yy) / 19))
                                    / 12 + 0.5);
                            if (z > 0.3 && z < 0.7) {
                                p[offset] = C.dark(c, (Math.abs((z - 0.5)) * 5) * Config.Gfx.dirtK + Config.Gfx.restDirtK);
                            }
                        }
                        offset--;
                    }
                }
                buf.effects().dirt(randomSeed);
                buf.effects().flipVertical();
            }
        }
        int yyy = Utils.rem((int) y, sprite.height);
        String name = sprite.name();
        boolean warfloor = name.startsWith("warfloor") ||
                name.equals("bck0");
        if (warfloor) {
            int offset = 0;
            for (int yy = 0; yy < 30; yy++) {
                int xxx = Utils.rem((int) x, sprite.width);
                for (int xx = 0; xx < 30; xx++) {
                    if (warfloor) {
                        to[offset++] = p[yyy * sprite.width + xxx];
                    }
                    xxx = Utils.rem(xxx + 1, sprite.width);
                }
                yyy = Utils.rem(yyy + 1, sprite.height);
            }
        } else if (name.equals("yellow_border")) {
            ((YellowBorder) this).draw2(to, sprite);
        } else {
            for (int i = 0; i < 30; i++) {
                System.arraycopy(p, 870 - (i * 30), to, i * 30, 30);
            }
        }
    }
}
