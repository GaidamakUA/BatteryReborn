package b.gfx;

import java.util.Arrays;

public class BufGfx {
    public static final int WHITE_COLOR = 0xffffffff;

    public int[] pixels;
    public int width;
    public int height;

    public BufGfx(int width, int height) {
        pixels = new int[height * width];
        this.width = width;
        this.height = height;
    }

    public BufGfx(int[] pixels, int width, int height) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
    }

    public BufGfx(Sprite sprite) {
        pixels = sprite.pixels;
        width = sprite.width;
        height = sprite.height;
    }

    public BufGfx(Sprite sprite, boolean anotherbufferplease) {
        width = sprite.width;
        height = sprite.height;
        if (anotherbufferplease) {
            pixels = new int[width * height];
            System.arraycopy(sprite.pixels, 0, pixels, 0, height * width);
        } else {
            pixels = sprite.pixels;
        }
    }

    public BufGfx(Sprite sprite, int[] buf) {
        width = sprite.width;
        height = sprite.height;
        pixels = buf;
        System.arraycopy(sprite.pixels, 0, pixels, 0, height * width);
    }

    public Effects effects() {
        return new Effects(this);
    }

    public final void draw(Sprite sprite, int x, int y) {
        draw(sprite.pixels, sprite.width, sprite.height, x, y);
    }

    public final void drawRangeCheck(BufGfx buf, int x, int y) {
        drawRangeCheck(buf.pixels, buf.width, buf.height, x, y);
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

    public final void drawRangeCheck(Sprite sprite, int x, int y) {
        drawRangeCheck(sprite.pixels, sprite.width, sprite.height, x, y);
    }

    private void drawRangeCheck(int[] p, int width, int height, int x, int y) {
        int xStart = x < 0 ? 0 : x;
        int yStart = y < 0 ? 0 : y;
        int xBorder = x + width;
        int yBorder = y + height;
        int xxBorder = xBorder > this.width ? this.width : xBorder;
        int yyBorder = yBorder > this.height ? this.height : yBorder;
        int pixel = 0;
        for (int yy = y; yy < yBorder; yy++) {
            int offset = yy * this.width + x;
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
            int offset = yy * this.width + x;
            for (int xx = x; xx < xxBorder; xx++) pixels[offset++] = buf[pixel++];
        }
    }

    private void drawTransparent(int[] p, int width, int height, int x, int y) {
        int xBorder = x + width;
        int yBorder = y + height;
        int pixel = 0;
        for (int yy = y; yy < yBorder; yy++) {
            int offset = yy * this.width + x;
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
            int offset = yy * this.width + x;
            for (int xx = x; xx < xBorder; xx++) {
                pixels[offset] = ColorUtils.dark(pixels[offset], shadow);
                offset++;
            }
        }
    }

    private void drawTransparentRangeCheck(int[] p, int width, int height, int x,
                                           int y) {
        int xStart = x < 0 ? 0 : x;
        int yStart = y < 0 ? 0 : y;
        int xBorder = x + width;
        int yBorder = y + height;
        int xxBorder = xBorder > this.width ? this.width : xBorder;
        int yyBorder = yBorder > this.height ? this.height : yBorder;
        int pixel = 0;
        for (int yy = y; yy < yBorder; yy++) {
            int offset = yy * this.width + x;
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
        int xxBorder = xBorder > this.width ? this.width : xBorder;
        int yyBorder = yBorder > this.height ? this.height : yBorder;
        int pixel = 0;
        for (int yy = y; yy < yBorder; yy++) {
            int offset = yy * this.width + x;
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
        int xxBorder = xBorder > this.width ? this.width : xBorder;
        int yyBorder = yBorder > this.height ? this.height : yBorder;
        int pixel = 0;
        for (int yy = y; yy < yBorder; yy++) {
            int offset = yy * this.width + x;
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
        int xxBorder = xBorder > this.width ? this.width : xBorder;
        int yyBorder = yBorder > this.height ? this.height : yBorder;
        int pixel = 0;
        for (int yy = y; yy < yBorder; yy++) {
            int offset = yy * this.width + x;
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
        draw(s.pixels, s.width, s.height, (width - s.width) / 2, (height - s.height) / 2);
    }

    public final void rect(int startX, int startY, int width, int height, int c) {
        horizontalLine(startX, startY, width, c);
        horizontalLine(startX, startY + height - 1, width, c);
        verticalLine(startX, startY, height, c);
        verticalLine(startX + width - 1, startY, height, c);
    }

    public final void filledRect(int startX, int startY, int width, int height, int c) {
        int offset = startY * this.width + startX;
        int yBorder = startY + height;
        for (int y = startY; y < yBorder; y++) {
            Arrays.fill(pixels, offset, offset + width, c);
            offset += this.width;
        }
    }

    private void horizontalLine(int x, int y, int width, int c) {
        int start = y * this.width + x;
        Arrays.fill(pixels, start, start + width, c);
    }

    private void verticalLine(int x, int y, int height, int c) {
        int yBorder = y + height;
        int offset = y * width + x;
        for (int i = y; i < yBorder; i++) {
            pixels[offset] = c;
            offset += width;
        }
    }
}
