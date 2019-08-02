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
        setWH(buf.width, buf.height);
        pixels = Arrays.copyOf(buf.pixels, buf.pixels.length);
    }

    private Sprite(String name, int width, int height, int[] pixels) {
        this.name = name;
        this.pixels = pixels;
        setWH(width, height);
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

    public Sprite copy() {
        int[] pixelsCopy = Arrays.copyOf(pixels, pixels.length);
        return new Sprite(name, width, height, pixelsCopy);
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

    public final void rot90() {
        if (width == height) {
            int p[] = new int[height * width];
            int offset = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    p[x * width + width - y - 1] = pixels[offset++];
                }
            }
            System.arraycopy(p, 0, pixels, 0, height * width);
        } else {
            rot90NotSquare();
        }
    }

    /**
     * Changes b (reference)
     */
    public final void rot90NotSquare() {
        int[] p = new int[height * width];
        int offset = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                p[x * height + y] = pixels[offset++];
            }
        }
        System.arraycopy(p, 0, pixels, 0, height * width);
        int exW = width;
        width = height;
        height = exW;
    }

    public final void horizontalLine(int x, int y, int width, int c) {
        int start = y * this.width + x;
        Arrays.fill(pixels, start, start + width, c);
    }

    public final void flipVertical() {
        int yBorder = height / 2;
        int offset = 0;
        for (int y = 0; y < yBorder; y++) {
            int offset2 = (height - y - 1) * width;
            for (int x = 0; x < width; x++) {
                int c = pixels[offset];
                pixels[offset++] = pixels[offset2];
                pixels[offset2++] = c;
            }
        }
    }

    public final void replaceColor(int what, int with) {
        for (int i = width * height - 1; i >= 0; i--) {
            if (pixels[i] == what) {
                pixels[i] = with;
            }
        }
    }
}
