/* refactoring0 */
package b.b.core;

import b.b.core.objs.ChanSquare;
import b.b.core.objs.YellowBorder;
import b.gfx.BufGfx;
import b.gfx.C;
import b.gfx.Sprite;
import b.util.U77;

/**
 * not obligatory square
 * not obligatory in "right" position
 */
public class Square extends Drawable {
    private static int[] util = new int[Config.squareSize * Config.squareSize];
    public Sprite sprite;

    protected BufGfx b;

    public Square(Sprite s, int xMap, int yMap, World w, boolean isShape,
                  int lvl) {
        this(s, xMap * Config.squareSize + Config.hSquareSize,
                yMap * Config.squareSize + Config.hSquareSize,
                s.w, s.h, w, isShape, lvl);
        setWH(Config.squareSize, Config.squareSize);
    }

    public Square(Sprite s, int x, int y, int width, int height,
                  World w, boolean isShape, int lvl) {
        super(x, y, width, height, isShape, lvl, w);
        sprite = s;
        b = w.g.bufGfx;
    }

    public void draw() {
        b.drawRangeCheck(sprite, (int) (x - hw),
                (int) (y - hh - screen.cameraY()) + chanSquareBonus());
    }

    protected int chanSquareBonus() {
        if (this instanceof ChanSquare &&
                (world.height * Config.squareSize - screen.cameraY() != 510)) return 1;
        return 0;
    }


    public void draw(int xScreen, int yScreen, int xScreenBorder,
                     int yScreenBorder) {
        int xxStart = xScreenStart();
        int yyStart = yScreenStart();
        if (xScreen < xxStart) xScreen = xxStart;
        if (yScreen < yyStart) yScreen = yyStart;
        int xxScreenBorder = xScreenBorder();
        int yyScreenBorder = yScreenBorder();
        if (xxScreenBorder < xScreenBorder) xScreenBorder = xxScreenBorder;
        if (yyScreenBorder < yScreenBorder) yScreenBorder = yyScreenBorder;
        int[] b = this.b.pixels;
        int[] p = sprite.b;
        if (Config.Gfx.dirtOn) {
            String sn = sprite.name();
            if (sn.startsWith("brick") ||
                    sn.startsWith("s_brick") ||
                    sn.startsWith("ss_brick") ||
                    sn.startsWith("c_brick")) {
                BufGfx buf = new BufGfx(sprite, util);
                buf.effects().dirt(id);
                p = buf.pixels;
            } else if (sn.equals("ground") && Config.Gfx.dirtGround) {
                new BufGfx(sprite, util).effects().dirt(0xffb5b5b5, id, Config.Gfx.groundDirtK);
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
                buf.effects().dirt(id);
            }
        }
        int yyy = U77.rem(yScreen + screen.camY(), (int) sprite.h);
        String name = sprite.name();
        boolean warfloor = name.startsWith("warfloor") ||
                name.equals("bck0");
        int xSpriteStart = U77.rem(xScreen, (int) sprite.w);
        int y0 = U77.rem(yScreen + screen.camY(), Config.squareSize);
        for (int yy = yScreen; yy < yScreenBorder; yy++) {
            int offset = yy * screen.w + xScreen;
            int pix = (yy - yyStart) * sprite.w + (xScreen - xxStart);
            int xxx = xSpriteStart;
            int x0 = 0;
            for (int xx = xScreen; xx < xScreenBorder; xx++) {
                if (warfloor) {
                    b[offset++] = p[yyy * sprite.w + xxx];
                } else if (name.equals("yellow_border")) {
                    ((YellowBorder) this).draw(offset++, p, yyy * sprite.w + xxx, x0, y0);
                } else {
                    b[offset++] = p[pix++];
                }
                xxx = U77.rem(xxx + 1, sprite.w);
                x0++;
            }
            yyy = U77.rem(yyy + 1, sprite.h);
            y0 = U77.rem(y0 + 1, Config.squareSize);
        }
    }

    public void draw2(int[] to) {
        int[] p = sprite.b;
        if (Config.Gfx.dirtOn) {
            String sn = sprite.name();
            if (sn.startsWith("brick") ||
                    sn.startsWith("s_brick") ||
                    sn.startsWith("ss_brick") ||
                    sn.startsWith("c_brick")) {
                BufGfx buf = new BufGfx(sprite, util);
                buf.effects().dirt(id);
                p = buf.pixels;
            } else if (sn.equals("ground") && Config.Gfx.dirtGround) {
                new BufGfx(sprite, util).effects().dirt(0xffb5b5b5, id, Config.Gfx.groundDirtK);
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
                buf.effects().dirt(id);
                buf.effects().flipVertical();
            }
        }
        int yyy = U77.rem((int) y, sprite.h);
        String name = sprite.name();
        boolean warfloor = name.startsWith("warfloor") ||
                name.equals("bck0");
        if (warfloor) {
            int offset = 0;
            for (int yy = 0; yy < 30; yy++) {
                int xxx = U77.rem((int) x, sprite.w);
                for (int xx = 0; xx < 30; xx++) {
                    if (warfloor) {
                        to[offset++] = p[yyy * sprite.w + xxx];
                    }
                    xxx = U77.rem(xxx + 1, sprite.w);
                }
                yyy = U77.rem(yyy + 1, sprite.h);
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
