/* refactoring0 */
package b.b.core.objs;

import b.b.core.Changeable;
import b.b.core.Square;
import b.b.core.World;
import b.b.monsters.Player;
import b.gfx.Sprite;

import java.awt.geom.Rectangle2D;

public abstract class ChanSquare extends Square implements Changeable {
    public ChanSquare(Sprite sprite, int xSquare, int ySquare, World world, boolean isShape,
                      int lvl) {
        super(sprite, xSquare, ySquare, world, isShape, lvl);
    }

    public ChanSquare(Sprite s, int x, int y, int width, int height,
                      World w, boolean isShape, int lvl) {
        super(s, x, y, width, height, w, isShape, lvl);
    }

    public void draw() {
        Sprite ex = sprite;
        changeSprite();
        super.draw();
        sprite = ex;
    }

    public final void draw(int xScreen, int yScreen, int xScreenBorder,
                           int yScreenBorder) {
        Sprite ex = sprite;
        changeSprite();
        super.draw(xScreen, yScreen, xScreenBorder, yScreenBorder);
        sprite = ex;
    }

    protected abstract void changeSprite();

    protected boolean onPlayer() {
        Player p = world.btr.player;
        return ((new Rectangle2D.Double(p.xStart(), p.yStart(), p.w, p.h)).intersects(
                new Rectangle2D.Double(xStart(), yStart(), w, h)));
    }

    public void act() {
    }
}
