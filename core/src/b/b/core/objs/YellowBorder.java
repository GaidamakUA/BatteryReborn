package b.b.core.objs;

import b.b.core.Config;
import b.b.core.Square;
import b.b.core.World;
import b.gfx.Sprite;
import b.util.U77;

public class YellowBorder extends Square {
    public boolean leftUp;
    public boolean up;
    public boolean upRight;
    public boolean right;
    public boolean rightDown;
    public boolean down;
    public boolean downLeft;
    public boolean left;

    public YellowBorder(Sprite s, int xMap, int yMap, World w, boolean isShape) {
        super(s, xMap * Config.squareSize + Config.hSquareSize,
                yMap * Config.squareSize + Config.hSquareSize,
                s.width, s.height, w, isShape, ZLayer.TWO);
        setWH(Config.squareSize, Config.squareSize);
    }

    public void draw(int offset, int[] buf, int pix, int x, int y) {
        if ((left && x < 10) ||
                (leftUp && x < 10 && y < 10) ||
                (up && y < 10) ||
                (upRight && x >= Config.squareSize - 10 && y < 10) ||
                (right && x >= Config.squareSize - 10) ||
                (rightDown && x >= Config.squareSize - 10 && y >= Config.squareSize - 10) ||
                (down && y >= Config.squareSize - 10) ||
                (downLeft && x < 10 && y >= Config.squareSize - 10) ||
                (left && x < 10)) {
            b.pixels[offset] = buf[pix];
        }
    }

    public void draw2(int[] to, Sprite sprite) {
        int[] buf = sprite.pixels;
        int offset = 0;
        int yyy = U77.rem((int) y, sprite.height);
        for (int yy = 0; yy < 30; yy++) {
            int xxx = U77.rem((int) x, sprite.width);
            for (int xx = 0; xx < 30; xx++) {
                if ((left && xx < 10) ||
                        (leftUp && xx < 10 && yy < 10) ||
                        (up && yy < 10) ||
                        (upRight && xx >= Config.squareSize - 10 && yy < 10) ||
                        (right && xx >= Config.squareSize - 10) ||
                        (rightDown && xx >= Config.squareSize - 10 && yy >= Config.squareSize - 10) ||
                        (down && yy >= Config.squareSize - 10) ||
                        (downLeft && xx < 10 && yy >= Config.squareSize - 10) ||
                        (left && xx < 10)) {
                    to[offset] = buf[yyy * sprite.width + xxx];
                }
                offset++;
                xxx = U77.rem(xxx + 1, sprite.width);
            }
            yyy = U77.rem(yyy + 1, sprite.height);
        }
    }
}
