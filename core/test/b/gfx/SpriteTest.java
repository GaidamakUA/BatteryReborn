package b.gfx;

import org.junit.Assert;
import org.junit.Test;

public class SpriteTest {

    @Test
    public void conversion_singlePixel_white() {
        int[] out = Sprite.createPixelArrayFromBytes(1, 1, new byte[]{(byte) 255, (byte) 255, (byte) 255});
        Assert.assertArrayEquals(new int[]{0xFFFFFFFF}, out);
    }

    @Test
    public void conversion_singlePixel_red() {
        int[] out = Sprite.createPixelArrayFromBytes(1, 1, new byte[]{(byte) 255, (byte) 0, (byte) 0});
        Assert.assertArrayEquals(new int[]{0xFFFF0000}, out);
    }
}