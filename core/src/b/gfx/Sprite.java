package b.gfx;

import b.util.U77;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;

import java.nio.ByteBuffer;

public class Sprite {
    private String name;

    public int[] pixels;
    public int w;
    public int h;
    public double hw;
    public double hh;

    public Sprite(String filename, int width, int height) {
        try {
            name = filename.substring(0, filename.indexOf('.'));
            setWH(width, height);

            Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
            ByteBuffer nativeData = pixmap.getPixels();
            byte[] managedData = new byte[nativeData.remaining()];
            nativeData.get(managedData);
            pixmap.dispose();
            pixels = createPixelArrayFromBytes(width, height, managedData);
        } catch (Exception e) {
            U77.throwException(e);
        }
    }

    static int[] createPixelArrayFromBytes(int width, int height, byte[] managedData) {
        int[] pixels = new int[width * height];
        for (int i = 0; i < pixels.length; i++) {
            byte r = managedData[i * 3];
            byte g = managedData[i * 3 + 1];
            byte b = managedData[i * 3 + 2];
            byte a = (byte) 255;
            pixels[i] = Color.toIntBits(r, g, b, a);
        }
        return pixels;
    }

    public Sprite(String name, Sprite sprite, boolean newBuf) {
        this(name, new BufGfx(sprite, newBuf));
    }

    public Sprite(String name, BufGfx buf) {
        this.name = name;
        setWH(buf.w, buf.h);
        pixels = new int[w * h];
        System.arraycopy(buf.pixels, 0, pixels, 0, w * h);
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

    public String toString() {
        return name;
    }
}
