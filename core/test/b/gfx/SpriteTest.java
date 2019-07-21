package b.gfx;

import org.junit.Assert;
import org.junit.Test;

public class SpriteTest {

    @Test
    public void createPixelArrayFromBytes() {
        int[] out = Sprite.createPixelArrayFromBytes(1, 1, new byte[]{(byte) 255, (byte) 255, (byte) 255});
        Assert.assertArrayEquals(new int[]{0xFFFFFFFF}, out);
    }
}