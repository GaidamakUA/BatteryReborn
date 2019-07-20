/* refactoring0 */
package b.b.core;

import b.gfx.*;

public abstract class Drawable implements Comparable {
  public int id;
  public Screen screen;
  /*
   * Center
   */
  public double x;
  public double y;

  public int w;
  public int h;
  public double hw;
  public double hh;
  public boolean isShape;

  /*
   * Level of height (0-8)
   */
  public int lvl;

  protected World world;

  public Drawable(double x, double y, int size, boolean isShape,
      int lvl, World world) {
    this(x, y, size, size, isShape, lvl, world);
  }

  public Drawable(double x, double y, int width, int height,
      boolean isShape, int lvl,World world) {
    this.world=world;
    id=world.nextId++;
    this.screen=world.btr.screen;
    this.x=x;
    this.y=y;
    setWH(width, height);
    this.isShape=isShape;
    this.lvl=lvl;
  }

  protected void setWH(int width, int height) {
    w=width;
    h=height;
    hw=(double)w/2;
    hh=(double)h/2;
  }
      
  public int compareTo(Object o) throws ClassCastException {
    if (lvl != ((Drawable)o).lvl) return lvl - ((Drawable)o).lvl;
    return this.hashCode()-o.hashCode();
  }

  public abstract void draw();

  public abstract void draw(int xStart, int yStart, int borderX, int borderY);

  public final double xStart() {
    return x-hw;
  }

  public final double yStart() {
    return y-hh;
  }

  public double xEnd() {
    return x+hw;
  }

  public double yEnd() {
    return y+hh;
  }

  public final int xScreenStart() {
    return (int)(x-hw);
  }

  public final int yScreenStart() {
    return (int)(y-hh-screen.camY());
  }

  protected int xScreenBorder() {
    return xScreenStart() + w;
  }

  protected int yScreenBorder() {
    return yScreenStart() + h;
  }
}
