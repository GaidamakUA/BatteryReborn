package b.gfx.effects;

import b.gfx.BufGfx;
import b.gfx.C;
import b.util.Utils;

public class Appearing {
    private static final double k1 = 0;
    private static final double k2 = 25 / 6;
    private static final double k3 = 1;
    private static final double k4 = 13;
    private static final double k5 = 25 / 6 * 2.5;
    private static final double k6 = 1;
    private static final double k7 = 77;
    private static final double k8 = 25 / 6 * 2.5 * 2.5;
    private static final double k9 = 1;
    private static final double effectZone = 1.25;

    private BufGfx g;
    private int[] b;
    private int w;
    private int h;
    private boolean holes;
    private double[] map;
    private int xStart;
    private int xBorder;
    private int yStart;
    private int yBorder;
    private boolean tiled;

    public Appearing(BufGfx gfx, int xStart, int yStart, int width, int height,
                     boolean holes, boolean tiled) {
        g = gfx;
        b = gfx.pixels;
        w = width;
        h = height;
        this.holes = holes;
        map = new double[w * h];
        this.xStart = xStart;
        xBorder = xStart + w;
        this.yStart = yStart;
        yBorder = yStart + h;
        double min = 0;
        double max = 0;
        int offset = 0;
        this.tiled = tiled;
        for (int y = yStart; y < yBorder; y++) {
            for (int x = xStart; x < xBorder; x++) {
                double v = effect(x, y, k1, k2, k3) + effect(x, y, k4, k5, k6) +
                        effect(x, y, k7, k8, k9);
                if (tiled) {
                    double sin = Math.sin(((double) y) / height * Utils.dpi);
                    v = Math.sin(((double) x) / width * Utils.dpi) + (sin * sin * 3) + (sin * 0.2);
                }
                map[offset++] = v;
                if (v < min) min = v;
                if (v > max) max = v;
            }
        }
        double k = max - min;
        for (int i = 0; i < w * h; i++) {
            map[i] = (map[i] - min) / k;
        }
    }

    public void draw(double timeK) {
        /*
         * 1 - in the start and in the end, 0 - in the middle
         */
        timeK = 1 - (Math.abs(timeK - 0.5) * 2);

        int mapIndex = 0;
        if (tiled) mapIndex = ((int) (timeK * 4)) * w;
        for (int y = yStart; y < yBorder; y++) {
            int offset = y * g.w + xStart;
            for (int x = xStart; x < xBorder; x++) {
                int c;
                c = b[offset];
                if (c != 0xff000000) {
                    double effectValue = map[mapIndex];
                    if (effectValue * 0.8 < timeK * effectZone || !holes) {
                        double brightness = effectValue * timeK + timeK;
                        if (brightness > 1) brightness = 1;
                        b[offset] = C.dark(c, brightness);
                    } else {
                        b[offset] = 0xff000000;
                    }
                }
                offset++;
                mapIndex++;
                if (mapIndex == w * h) mapIndex = 0;
            }
        }
    }

    private double effect(double x, double y, double shift, double k1, double k2) {
        return (Math.sin((x + shift) / k1) + Math.sin((y + shift) / k1)) * k2;
    }
}
