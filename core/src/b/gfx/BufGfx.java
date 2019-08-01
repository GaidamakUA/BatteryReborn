package b.gfx;

import java.util.Arrays;

public class BufGfx {
    public static final int WHITE_COLOR = 0xffffffff;

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
        w = sprite.width;
        h = sprite.height;
    }

    public BufGfx(Sprite sprite, boolean anotherbufferplease) {
        w = sprite.width;
        h = sprite.height;
        if (anotherbufferplease) {
            pixels = new int[w * h];
            System.arraycopy(sprite.pixels, 0, pixels, 0, h * w);
        } else {
            pixels = sprite.pixels;
        }
    }

    public BufGfx(Sprite sprite, int[] buf) {
        w = sprite.width;
        h = sprite.height;
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
        draw(sprite.pixels, sprite.width, sprite.height, x, y);
    }

    public final void drawRangeCheck(BufGfx buf, int x, int y) {
        drawRangeCheck(buf.pixels, buf.w, buf.h, x, y);
    }

    public final void drawTransparent(Sprite sprite, int x, int y) {
        drawTransparent(sprite.pixels, sprite.width, sprite.height, x, y);
    }

    public final void drawTransparentRangeCheck(Sprite sprite, int x, int y) {
        drawTransparentRangeCheck(sprite.pixels, sprite.width, sprite.height, x, y);
    }

    /**
     * @param solid 1-not transp.; 0-absolutely transp.
     */
    public final void drawTransparentTrRangeCheck(Sprite sprite, int x, int y,
                                                  double solid) {
        drawTransparentTrRangeCheck(sprite.pixels, sprite.width, sprite.height, x, y, solid);
    }

    public final void drawTransparentWhiteRangeCheck(Sprite sprite, int x, int y) {
        drawTransparentWhiteRangeCheck(sprite.pixels, sprite.width, sprite.height, x, y);
    }

    public final void drawTransparentBlackRangeCheck(Sprite sprite, int x, int y) {
        drawTransparentBlackRangeCheck(sprite.pixels, sprite.width, sprite.height, x, y);
    }

    public final void drawTransparentWhiteRangeCheck(BufGfx buf, int x, int y) {
        drawTransparentWhiteRangeCheck(buf.pixels, buf.w, buf.h, x, y);
    }

    public final void drawRangeCheck(Sprite sprite, int x, int y) {
        drawRangeCheck(sprite.pixels, sprite.width, sprite.height, x, y);
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

    private void drawRangeCheck(int[] p, int width, int height, int x, int y) {
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

    private void drawTransparent(int[] p, int width, int height, int x, int y) {
        int xBorder = x + width;
        int yBorder = y + height;
        int pixel = 0;
        for (int yy = y; yy < yBorder; yy++) {
            int offset = yy * w + x;
            for (int xx = x; xx < xBorder; xx++) {
                int c = p[pixel++];
                if (c != WHITE_COLOR) {
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
                pixels[offset] = ColorUtils.dark(pixels[offset], shadow);
                offset++;
            }
        }
    }

    private final void drawTransparentRangeCheck(int[] p, int width, int height, int x,
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
                if ((c != WHITE_COLOR) && (xx >= xStart && xx < xxBorder && yy >= yStart &&
                        yy < yyBorder)) {
                    pixels[offset++] = c;
                } else {
                    offset++;
                }
            }
        }
    }

    private void drawTransparentTrRangeCheck(int[] p, int width, int height,
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
                if ((c != WHITE_COLOR) && (xx >= xStart && xx < xxBorder && yy >= yStart &&
                        yy < yyBorder)) {
                    pixels[offset] = ColorUtils.mix(pixels[offset], c, solid);
                }
                offset++;
            }
        }
    }

    private void drawTransparentWhiteRangeCheck(int[] p, int width, int height, int x,
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
                    if (c != WHITE_COLOR) {
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

    public final void drawTransparentBlackRangeCheck(int[] p, int width, int height,
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
                    if (c != WHITE_COLOR) {
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
        draw(s.pixels, s.width, s.height, (w - s.width) / 2, (h - s.height) / 2);
    }

    public final void rect(int startX, int startY, int width, int height, int c) {
        horizontalLine(startX, startY, width, c);
        horizontalLine(startX, startY + height - 1, width, c);
        verticalLine(startX, startY, height, c);
        verticalLine(startX + width - 1, startY, height, c);
    }

    public final void filledRect(int startX, int startY, int width, int height, int c) {
        int offset = startY * w + startX;
        int yBorder = startY + height;
        for (int y = startY; y < yBorder; y++) {
            Arrays.fill(pixels, offset, offset + width, c);
            offset += w;
        }
    }

    public final void horizontalLine(int x, int y, int width, int c) {
        int start = y * w + x;
        Arrays.fill(pixels, start, start + width, c);
    }

    private void verticalLine(int x, int y, int height, int c) {
        int yBorder = y + height;
        int offset = y * w + x;
        for (int i = y; i < yBorder; i++) {
            pixels[offset] = c;
            offset += w;
        }
    }
}
