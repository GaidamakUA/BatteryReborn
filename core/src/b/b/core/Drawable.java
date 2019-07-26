/* refactoring0 */
package b.b.core;

import b.gfx.Screen;

public abstract class Drawable implements Comparable {
    public final int randomSeed;
    public final Screen screen;
    public final boolean isShape;
    /*
     * Center
     */
    public double x;
    public double y;

    public int width;
    public int height;
    public double halfWidth;
    public double halfHeight;

    /*
     * Level of height (0-8)
     */
    private final int zLayer;

    protected World world;

    public Drawable(double x, double y, int width, int height,
                    boolean isShape, int zLayer, World world) {
        this.world = world;
        randomSeed = world.nextId++;
        this.screen = world.btr.screen;
        this.x = x;
        this.y = y;
        setWH(width, height);
        this.isShape = isShape;
        this.zLayer = zLayer;
    }

    protected void setWH(int width, int height) {
        this.width = width;
        this.height = height;
        halfWidth = (double) this.width / 2;
        halfHeight = (double) this.height / 2;
    }

    public int compareTo(Object o) throws ClassCastException {
        if (zLayer != ((Drawable) o).zLayer) {
            return zLayer - ((Drawable) o).zLayer;
        }
        return this.hashCode() - o.hashCode();
    }

    public abstract void draw();

    public final double xStart() {
        return x - halfWidth;
    }

    public final double yStart() {
        return y - halfHeight;
    }

    public double xEnd() {
        return x + halfWidth;
    }

    public double yEnd() {
        return y + halfHeight;
    }

    public final int xScreenStart() {
        return (int) (x - halfWidth);
    }

    public final int yScreenStart() {
        return (int) (y - halfHeight - screen.camY());
    }

    protected int xScreenBorder() {
        return xScreenStart() + width;
    }

    protected int yScreenBorder() {
        return yScreenStart() + height;
    }
}
