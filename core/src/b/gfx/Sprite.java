package b.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Sprite {
    private String name;

    public int[] pixels;
    public int width;
    public int height;
    public double halfWidth;
    public double halfHeight;

    public Sprite(String filename, int width, int height) {
        name = filename.substring(0, filename.indexOf('.'));
        setWH(width, height);

        Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
        ByteBuffer nativeData = pixmap.getPixels();
        byte[] managedData = new byte[nativeData.remaining()];
        nativeData.get(managedData);
        pixmap.dispose();
        pixels = createPixelArrayFromBytes(width, height, managedData);
    }

    public Sprite(String name, Sprite sprite, boolean newBuf) {
        this(name, new BufGfx(sprite, newBuf));
    }

    public Sprite(String name, BufGfx buf) {
        this.name = name;
        setWH(buf.w, buf.h);
        pixels = Arrays.copyOf(buf.pixels, buf.pixels.length);
    }

    static int[] createPixelArrayFromBytes(int width, int height, byte[] managedData) {
        int[] pixels = new int[width * height];
        for (int i = 0; i < pixels.length; i++) {
            float r = unsignedToBytes(managedData[i * 3]) / 255f;
            float g = unsignedToBytes(managedData[i * 3 + 1]) / 255f;
            float b = unsignedToBytes(managedData[i * 3 + 2]) / 255f;
            float a = 255 / 255f;
            int colorInt = Color.argb8888(a, r, g, b);
            pixels[i] = colorInt;
        }
        return pixels;
    }

    private static int unsignedToBytes(byte b) {
        return b & 0xFF;
    }

    public final void setWH(int width, int height) {
        this.width = width;
        this.height = height;
        halfWidth = (double) this.width / 2;
        halfHeight = (double) this.height / 2;
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

    public final void flipHorizontal() {
        int xBorder = width / 2;
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            int offset2 = y * width + width - 1;
            for (int x = 0; x < xBorder; x++) {
                int c = pixels[offset];
                pixels[offset++] = pixels[offset2];
                pixels[offset2--] = c;
            }
        }
    }
}
