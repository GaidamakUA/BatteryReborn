package b.gfx;

import b.util.U77;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;

import java.nio.ByteBuffer;

public class Sprite {
    private String name;

    public int[] b;
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
            b = new int[managedData.length];
            for (int i = 0; i < managedData.length; i++) {
                b[i] = managedData[i];
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

    public String toString() {
        return name;
    }
}
