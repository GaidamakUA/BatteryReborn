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
                      ZLayer lvl) {
        super(sprite, xSquare, ySquare, world, isShape, lvl);
    }

    public ChanSquare(Sprite s, int x, int y, int width, int height,
                      World w, boolean isShape, ZLayer lvl) {
        super(s, x, y, width, height, w, isShape, lvl);
    }

    public void draw() {
        Sprite ex = sprite;
        changeSprite();
        super.draw();
        sprite = ex;
    }

    protected abstract void changeSprite();

    protected boolean onPlayer() {
        Player p = world.btr.player;
        return ((new Rectangle2D.Double(p.xStart(), p.yStart(), p.width, p.height)).intersects(
                new Rectangle2D.Double(xStart(), yStart(), width, height)));
    }

    public void act() {
    }
}
