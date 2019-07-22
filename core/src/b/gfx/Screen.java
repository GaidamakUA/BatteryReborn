package b.gfx;

import b.b.core.Config;
import b.b.core.World;

public class Screen {
    public static int lastYGc = -1;
    public int w;
    public int h;
    public int[] b;
    private double cameraY;
    private int camY;

    public Screen(int w, int h) {
        init(w, h);
    }

    public void init(int w, int h) {
        this.w = w;
        this.h = h;
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

    public void setCameraY(double v, World w) {
        cameraY = v;
        camY = (int) cameraY;
        w.activateMonsters();
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
