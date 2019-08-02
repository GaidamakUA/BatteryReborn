package b.gfx;

import java.util.Random;

import b.b.core.Config;

public class Effects {
    private BufGfx b;

    protected Effects(BufGfx buf) {
        b = buf;
    }

    public final void diagonal(double part, int length, int c) {
        int color = ColorUtils.light(c, 0.5);
        int start = (int) (part * (length + length + b.width)) - length;
        int border = start + length;
        for (int x = start; x < border; x++) {
            hline(x, c, color);
        }
    }

    public final void flipVertical() {
        int[] res = new int[b.height * b.width];
        for (int yy = 0; yy < b.height; yy++) {
            System.arraycopy(b.pixels, b.width * yy, res, (b.height - 1) * b.width - (yy * b.width), b.width);
        }
        System.arraycopy(res, 0, b.pixels, 0, b.width * b.height);
    }

    private final void hline(int x, int c, int color) {
        int[] p = b.pixels;
        if (x >= 0 && x < b.width) {
            int index = x;
            for (int y = 0; y < b.height; y++) {
                if (p[index] == c) {
                    p[index] = color;
                }
                index += b.width;
            }
        }
    }

    public final void dark(int c, double dark) {
        int[] p = b.pixels;
        int cc = ColorUtils.dark(c, dark);
        for (int i = p.length - 1; i >= 0; i--) {
            if (p[i] == c) p[i] = cc;
        }
    }

    public final void change(int c0, int c1) {
        int[] p = b.pixels;
        for (int i = p.length - 1; i >= 0; i--) {
            if (p[i] == c0) p[i] = c1;
        }
    }

    public final void dirt(int c, int randomSeed, double k) {
        Random r = new Random(randomSeed);
        int[] p = b.pixels;
        double rest = 1 - k;
        for (int i = p.length - 1; i >= 0; i--) {
            if (p[i] == c) {
                p[i] = ColorUtils.dark(c, r.nextDouble() * k + rest);
            }
        }
    }

    public final void dirt(int randomSeed) {
        Random r = new Random(randomSeed);
        int[] p = b.pixels;
        for (int i = p.length - 1; i >= 0; i--) {
            p[i] = ColorUtils.dark(p[i], r.nextDouble() * Config.Gfx.dirtK + Config.Gfx.restDirtK);
        }
    }
}
