package b.gfx;

import b.b.core.Config;
import b.b.core.World;
import b.util.U77;

public class Screen {
    public static int lastYGc = -1;
    public int w;
    public int h;
    public int[] b;
    private double cameraY;
    private int camY;

    /* Previous camera coord */
    private double yGlobal;

    /* Screen index */
    private int yStart;

    public Screen(int w, int h) {
        init(w, h);
    }

    public void init(int w, int h) {
        this.w = w;
        this.h = h;
        yStart = 0;
        yGlobal = 999999999;
        camY = 0;
        cameraY = 0;
        b = new int[h * w];
    }

    public double cameraY() {
        return cameraY;
    }

    public int camY() {
        return camY;
    }

    public int yGlobal() {
        return (int) yGlobal;
    }

    public void setCameraY(double v, World w) {
        cameraY = v;
        camY = (int) cameraY;
        w.activateMonsters();
    }

    /**
     * |x|_|o|o|o|o| shiftY=1
     * |x|_|o|o|o|o|
     * |X|x|x|x|x|x| cameraY(yStart)
     * |x|_|o|o|o|o|
     * |x|_|o|o|o|o|
     */
    public void getNewFrom(int[] p) {
        int shiftY = camY - (int) yGlobal;
        yGlobal = cameraY;
        if (shiftY <= -h || shiftY >= h) {
            yStart = 0;
            System.arraycopy(p, 0, b, 0, h * w);
            return;
        }
        yStart = U77.rem(yStart + shiftY, h);
        if (shiftY < 0) {
            getNew(-shiftY, p);
        }
    }

    /**
     * |x|_|_|_|_|_|
     * |x|_|_|_|_|_|
     * |X|x|x|x|x|x| yStart
     * |x|_|_|_|_|_|
     * |x|_|_|_|_|_|
     */
    public void copyTo(int[] p) {
        int offset = 0;
        for (int y = yStart; y < h; y++) {
            int pix = y * w;
            System.arraycopy(b, pix, p, offset, w);
            offset += w;
        }
        for (int y = 0; y < yStart; y++) {
            int pix = y * w;
            System.arraycopy(b, pix, p, offset, w);
            offset += w;
        }
    }

    /**
     * From yStart height lines down
     */
    private void getNew(int height, int[] p) {
        int offset = 0;
        int yScreenBorder = yStart + height;
        if (yScreenBorder > h) yScreenBorder = h;
        for (int y = yStart; y < yScreenBorder; y++) {
            int pix = y * w;
            System.arraycopy(p, offset, b, pix, w);
            offset += w;
        }
        if (yScreenBorder < yStart + height) {
            yScreenBorder = yStart + height - h;
            for (int y = 0; y < yScreenBorder; y++) {
                int pix = y * w;
                System.arraycopy(p, offset, b, pix, w);
                offset += w;
            }
        }
    }

    public int xEnd() {
        return w - 1;
    }

    public double yEnd() {
        return camY + h - 1;
    }

    public int getStartY() {
        return (int) (cameraY / Config.squareSize);
    }

    public int getXBorder() {
        return (int) ((w - 1) / Config.squareSize) + 1;
    }

    public int getYBorder() {
        return (int) ((cameraY + h - 1) / Config.squareSize) + 1;
    }
}
