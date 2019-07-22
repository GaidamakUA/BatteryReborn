package b.gfx;

import java.util.Arrays;

public class BufGfx {
    public static final int transp = 0xffffffff;

    public int[] pixels;
    public int w;
    public int h;

    public BufGfx(int w, int h) {
        pixels = new int[h * w];
        this.w = w;
        this.h = h;
    }

    public BufGfx(int[] pixels, int w, int h) {
        this.pixels = pixels;
        this.w = w;
        this.h = h;
    }

    public BufGfx(Sprite sprite) {
        pixels = sprite.pixels;
        w = sprite.w;
        h = sprite.h;
    }

    public BufGfx(Sprite sprite, boolean anotherbufferplease) {
        w = sprite.w;
        h = sprite.h;
        if (anotherbufferplease) {
            pixels = new int[w * h];
            System.arraycopy(sprite.pixels, 0, pixels, 0, h * w);
        } else {
            pixels = sprite.pixels;
        }
    }

    public BufGfx(Sprite sprite, int[] buf) {
        w = sprite.w;
        h = sprite.h;
        pixels = buf;
        System.arraycopy(sprite.pixels, 0, pixels, 0, h * w);
    }

    public Effects effects() {
        return new Effects(this);
    }

    public final void replaceColor(int what, int with) {
        for (int i = w * h - 1; i >= 0; i--) {
            if (pixels[i] == what) pixels[i] = with;
        }
    }

    public final void draw(Sprite sprite, int x, int y) {
        draw(sprite.pixels, sprite.w, sprite.h, x, y);
    }

    public final void drawRangeCheck(BufGfx buf, int x, int y) {
        drawRangeCheck(buf.pixels, buf.w, buf.h, x, y);
    }

    public final void drawTransp(Sprite sprite, int x, int y) {
        drawTransp(sprite.pixels, sprite.w, sprite.h, x, y);
    }

    public final void drawTranspRangeCheck(Sprite sprite, int x, int y) {
        drawTranspRangeCheck(sprite.pixels, sprite.w, sprite.h, x, y);
    }

    /**
     * @param solid 1-not transp.; 0-absolutely transp.
     */
    public final void drawTranspTrRangeCheck(Sprite sprite, int x, int y,
                                             double solid) {
        drawTranspTrRangeCheck(sprite.pixels, sprite.w, sprite.h, x, y, solid);
    }

    public final void drawTranspWhiteRangeCheck(Sprite sprite, int x, int y) {
        drawTranspWhiteRangeCheck(sprite.pixels, sprite.w, sprite.h, x, y);
    }

    public final void drawTranspBlackRangeCheck(Sprite sprite, int x, int y) {
        drawTranspBlackRangeCheck(sprite.pixels, sprite.w, sprite.h, x, y);
    }

    public final void drawTranspWhiteRangeCheck(BufGfx buf, int x, int y) {
        drawTranspWhiteRangeCheck(buf.pixels, buf.w, buf.h, x, y);
    }

    public final void drawRangeCheck(Sprite sprite, int x, int y) {
        drawRangeCheck(sprite.pixels, sprite.w, sprite.h, x, y);
    }

    public final void flipHorizontal() {
        int xBorder = w / 2;
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            int offset2 = y * w + w - 1;
            for (int x = 0; x < xBorder; x++) {
                int c = pixels[offset];
                pixels[offset++] = pixels[offset2];
                pixels[offset2--] = c;
            }
        }
    }

    public final void flipVertical() {
        int yBorder = h / 2;
        int offset = 0;
        for (int y = 0; y < yBorder; y++) {
            int offset2 = (h - y - 1) * w;
            for (int x = 0; x < w; x++) {
                int c = pixels[offset];
                pixels[offset++] = pixels[offset2];
                pixels[offset2++] = c;
            }
        }
    }

    public final void rot90() {
        if (w == h) {
            int p[] = new int[h * w];
            int offset = 0;
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    p[x * w + w - y - 1] = pixels[offset++];
                }
            }
            System.arraycopy(p, 0, pixels, 0, h * w);
        } else {
            rot90NotSquare(null);
        }
    }

    /**
     * Changes b (reference)
     */
    public final void rot90NotSquare(Sprite s) {
        int p[] = new int[h * w];
        int offset = 0;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                p[x * h + y] = pixels[offset++];
            }
        }
        System.arraycopy(p, 0, pixels, 0, h * w);
        int exW = w;
        w = h;
        h = exW;
        if (s != null) s.setWH(w, h);
    }

    public final void drawRangeCheck(int[] p, int width, int height, int x, int y) {
        int xStart = x < 0 ? 0 : x;
        int yStart = y < 0 ? 0 : y;
        int xBorder = x + width;
        int yBorder = y + height;
        int xxBorder = xBorder > w ? w : xBorder;
        int yyBorder = yBorder > h ? h : yBorder;
        int pixel = 0;
        for (int yy = y; yy < yBorder; yy++) {
            int offset = yy * w + x;
            for (int xx = x; xx < xBorder; xx++) {
                if (xx >= xStart && xx < xxBorder && yy >= yStart && yy < yyBorder) {
                    pixels[offset++] = p[pixel++];
                } else {
                    offset++;
                    pixel++;
                }
            }
        }
    }

    public final void draw(int[] buf, int w, int h, int x, int y) {
        int xxBorder = x + w;
        int yyBorder = y + h;
        int pixel = 0;
        for (int yy = y; yy < yyBorder; yy++) {
            int offset = yy * this.w + x;
            for (int xx = x; xx < xxBorder; xx++) pixels[offset++] = buf[pixel++];
        }
    }

    public final void drawTransp(int[] p, int width, int height, int x, int y) {
        int xBorder = x + width;
        int yBorder = y + height;
        int pixel = 0;
        for (int yy = y; yy < yBorder; yy++) {
            int offset = yy * w + x;
            for (int xx = x; xx < xBorder; xx++) {
                int c = p[pixel++];
                if (c != transp) {
                    pixels[offset++] = c;
                } else {
                    offset++;
                }
            }
        }
    }

    public final void rectShadow(int x, int y, int width, int height,
                                 double shadow) {
        int xBorder = x + width;
        int yBorder = y + height;
        for (int yy = y; yy < yBorder; yy++) {
            int offset = yy * w + x;
            for (int xx = x; xx < xBorder; xx++) {
                pixels[offset] = C.dark(pixels[offset], shadow);
                offset++;
            }
        }
    }

    public final void drawTranspRangeCheck(int[] p, int width, int height, int x,
                                           int y) {
        int xStart = x < 0 ? 0 : x;
        int yStart = y < 0 ? 0 : y;
        int xBorder = x + width;
        int yBorder = y + height;
        int xxBorder = xBorder > w ? w : xBorder;
        int yyBorder = yBorder > h ? h : yBorder;
        int pixel = 0;
        for (int yy = y; yy < yBorder; yy++) {
            int offset = yy * w + x;
            for (int xx = x; xx < xBorder; xx++) {
                int c = p[pixel++];
                if ((c != transp) && (xx >= xStart && xx < xxBorder && yy >= yStart &&
                        yy < yyBorder)) {
                    pixels[offset++] = c;
                } else {
                    offset++;
                }
            }
        }
    }

    public final void drawTranspTrRangeCheck(int[] p, int width, int height,
                                             int x, int y, double solid) {
        int xStart = x < 0 ? 0 : x;
        int yStart = y < 0 ? 0 : y;
        int xBorder = x + width;
        int yBorder = y + height;
        int xxBorder = xBorder > w ? w : xBorder;
        int yyBorder = yBorder > h ? h : yBorder;
        int pixel = 0;
        for (int yy = y; yy < yBorder; yy++) {
            int offset = yy * w + x;
            for (int xx = x; xx < xBorder; xx++) {
                int c = p[pixel++];
                if ((c != transp) && (xx >= xStart && xx < xxBorder && yy >= yStart &&
                        yy < yyBorder)) {
                    pixels[offset] = C.mix(pixels[offset], c, solid);
                }
                offset++;
            }
        }
    }

    public final void drawBlackRangeCheck(int[] p, int width, int height, int x,
                                          int y) {
        int xStart = x < 0 ? 0 : x;
        int yStart = y < 0 ? 0 : y;
        int xBorder = x + width;
        int yBorder = y + height;
        int xxBorder = xBorder > w ? w : xBorder;
        int yyBorder = yBorder > h ? h : yBorder;
        int pixel = 0;
        for (int yy = y; yy < yBorder; yy++) {
            int offset = yy * w + x;
            for (int xx = x; xx < xBorder; xx++) {
                int c = p[pixel++];
                if ((c == 0xff000000) && (xx >= xStart && xx < xxBorder && yy >= yStart &&
                        yy < yyBorder)) {
                    pixels[offset++] = c;
                } else {
                    offset++;
                }
            }
        }
    }

    public final void drawTranspWhiteRangeCheck(int[] p, int width, int height, int x,
                                                int y) {
        int xStart = x < 0 ? 0 : x;
        int yStart = y < 0 ? 0 : y;
        int xBorder = x + width;
        int yBorder = y + height;
        int xxBorder = xBorder > w ? w : xBorder;
        int yyBorder = yBorder > h ? h : yBorder;
        int pixel = 0;
        for (int yy = y; yy < yBorder; yy++) {
            int offset = yy * w + x;
            for (int xx = x; xx < xBorder; xx++) {
                int c = p[pixel++];
                if (xx >= xStart && xx < xxBorder && yy >= yStart && yy < yyBorder) {
                    if (c != transp) {
                        if (c == 0xff000000) {
                            pixels[offset++] = 0xff000000;
                        } else {
                            pixels[offset++] = 0xffffffff;
                        }
                    } else {
                        offset++;
                    }
                } else {
                    offset++;
                }
            }
        }
    }

    public final void drawTranspBlackRangeCheck(int[] p, int width, int height,
                                                int x, int y) {
        int xStart = x < 0 ? 0 : x;
        int yStart = y < 0 ? 0 : y;
        int xBorder = x + width;
        int yBorder = y + height;
        int xxBorder = xBorder > w ? w : xBorder;
        int yyBorder = yBorder > h ? h : yBorder;
        int pixel = 0;
        for (int yy = y; yy < yBorder; yy++) {
            int offset = yy * w + x;
            for (int xx = x; xx < xBorder; xx++) {
                int c = p[pixel++];
                if (xx >= xStart && xx < xxBorder && yy >= yStart && yy < yyBorder) {
                    if (c != transp) {
                        pixels[offset++] = 0xff000000;
                    } else {
                        offset++;
                    }
                } else {
                    offset++;
                }
            }
        }
    }

    public final void drawAtCenter(Sprite s) {
        draw(s.pixels, s.w, s.h, (w - s.w) / 2, (h - s.h) / 2);
    }

    public final void rect(int startX, int startY, int width, int height, int c) {
        hline(startX, startY, width, c);
        hline(startX, startY + height - 1, width, c);
        vline(startX, startY, height, c);
        vline(startX + width - 1, startY, height, c);
    }

    public final void filledRect(int startX, int startY, int width, int height, int c) {
        int offset = startY * w + startX;
        int yBorder = startY + height;
        for (int y = startY; y < yBorder; y++) {
            Arrays.fill(pixels, offset, offset + width, c);
            offset += w;
        }
    }

    public final void hline(int x, int y, int width, int c) {
        int start = y * w + x;
        Arrays.fill(pixels, start, start + width, c);
    }

    public final void vline(int x, int y, int height, int c) {
        int yBorder = y + height;
        int offset = y * w + x;
        for (int i = y; i < yBorder; i++) {
            pixels[offset] = c;
            offset += w;
        }
    }
}
