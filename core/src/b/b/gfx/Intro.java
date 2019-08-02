package b.b.gfx;

import java.util.Arrays;

import b.gfx.Sprite;
import b.gfx.effects.Appearing;
import b.gfx.effects.RunningDiagonals;
import b.gfx.effects.Waves;
import b.util.Utils;

/**
 * Starts at time: 0
 */
public class Intro {
    public static final double DURATION = 7000;

    private static final int WAVE_HEIGHT = 10;
    private static final int WAVE_LENGTH = 40;
    private static final int DIAGONAL_LENGTH = 120;

    private final Gfx gfx;
    private final Sprite logo;
    private final Waves waves;
    private final Appearing appearing;
    private final RunningDiagonals diagonals;

    private double startTime = Double.NaN;

    public Intro(Gfx gfx) {
        this.gfx = gfx;
        logo = gfx.getSprite("battery");
        int xStart = gfx.bufGfx.width / 2 - (logo.width / 2) - WAVE_HEIGHT - 1;
        int yStart = gfx.bufGfx.height / 2 - (logo.height / 2) - WAVE_HEIGHT - 1;
        int width = WAVE_HEIGHT * 2 + logo.width + 2;
        int height = WAVE_HEIGHT * 2 + logo.height + 2;
        waves = new Waves(gfx.bufGfx, xStart + WAVE_HEIGHT, yStart + WAVE_HEIGHT,
                logo.width + 1, logo.height + 1, WAVE_LENGTH, WAVE_HEIGHT, 4000);
        appearing = new Appearing(gfx.bufGfx, xStart, yStart, width, height, true, false);
        diagonals = new RunningDiagonals(gfx.bufGfx, xStart, yStart, xStart + width,
                yStart + height, DIAGONAL_LENGTH);
    }

    public double getStartTime() {
        return startTime;
    }

    public void draw(double time) {
        if (Double.isNaN(startTime)) {
            startTime = time;
        }
        if (time < 0) time = 0;
        Arrays.fill(gfx.bufGfx.pixels, 0xff000000);
        gfx.bufGfx.drawAtCenter(logo);
        double timeK = (time - startTime) / DURATION;
        waves.draw(time, Utils.k(timeK, 0.1));
        appearing.draw(timeK);
        diagonals.draw(Utils.k2(timeK, 0.25));
        if (timeK > 0.25 && timeK < 0.75) {
            Sprite s = gfx.getSprite("copy");
            gfx.bufGfx.draw(s, gfx.w / 2 - (int) s.halfWidth, gfx.h - 50);
        }
    }
}
