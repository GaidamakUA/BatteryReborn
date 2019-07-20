package b.gfx;

import b.util.U77;

import java.applet.Applet;
import java.awt.*;
import java.awt.image.PixelGrabber;

public class Sprite {
    private String name;

    public int[] b;
    public int w;
    public int h;
    public double hw;
    public double hh;

    public Sprite(Applet a, String filename, int width, int height) {
        try {
            name = filename.substring(0, filename.indexOf('.'));
            setWH(width, height);
            Image img = a.getImage(a.getDocumentBase(), filename);
            b = new int[w * h];
            PixelGrabber pg = new PixelGrabber(img, 0, 0, w, h, b, 0, w);
            pg.grabPixels();
            if (pg.status() == 192) {
                throw new RuntimeException("error loading " + filename + " sprite");
            }
        } catch (Exception e) {
            U77.throwException(e);
        }
    }

    public Sprite(String name, Sprite sprite, boolean newBuf) {
        this(name, new BufGfx(sprite, newBuf));
    }

    public Sprite(String name, BufGfx buf) {
        this.name = name;
        setWH(buf.w, buf.h);
        b = new int[w * h];
        System.arraycopy(buf.b, 0, b, 0, w * h);
    }

    public final void setWH(int width, int height) {
        w = width;
        h = height;
        hw = (double) w / 2;
        hh = (double) h / 2;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final String name() {
        return name;
    }

    public void tile(int xZoom, int yZoom) {
        int newW = w * xZoom;
        int newH = h * yZoom;
        int[] p = new int[newH * newW];
        for (int y = 0; y < yZoom; y++) {
            for (int x = 0; x < xZoom; x++) {
                for (int line = 0; line < h; line++) {
                    System.arraycopy(b, line * w, p, (y * h + line) * newW + (x * w), w);
                }
            }
        }
        b = p;
        setWH(newW, newH);
    }

    public String toString() {
        return name;
    }
}
