package b.b.gfx;

import b.b.Battery;
import b.b.core.Config;
import b.b.core.Drawable;
import b.b.core.WorldSquare;
import b.b.core.objs.Water;
import b.b.monsters.Player;
import b.b.monsters.bosses.Boss2AI;
import b.b.monsters.items.DrawableLibGDX;
import b.gfx.*;
import b.util.Utils;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.blogspot.androidgaidamak.BatteryGame;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Gfx {
    public int w;
    public int h;
    public BufGfx bufGfx;
    public Battery battery;
    public b.gfx.Font77 font;
    public Console console;

    protected Map<String, Sprite> sprites;


    public Gfx(Battery battery) {
        this.battery = battery;
        w = BatteryGame.VIEWPORT_WIDTH;
        h = BatteryGame.VIEWPORT_HEIGHT;
        bufGfx = new BufGfx(new int[w * h], w, h);
        font = new b.gfx.Font77(0xffffffff, 0xff000000, bufGfx);
        console = new Console(font);
        sprites = new HashMap<String, Sprite>();
        new SpriteLoader(this).load();
    }

    public final Sprite getSprite(String name) {
        Sprite s = sprites.get(name);
        if (s == null) {
            throw new RuntimeException("Gfx:getSprite [" + name + "] returns null");
        }
        return s;
    }

    protected final void putSprite(Sprite sprite) {
        sprites.put(sprite.name(), sprite);
    }

    public void drawAll() {
        drawNewBackAndForeGround();
        drawChangeables();
        if (battery.justStarted) {
            console.addString("Use arrow keys to move the airplane.");
            console.addString("Use Ctrl to shoot.");
            console.addString("");
        }
        if (!battery.activated) {
            console.addString("CLICK MOUSE TO START");
        } else {
            if (battery.time.time - battery.timeWhenLevelCompleted <
                    Config.Intervals.nextLevelDelay) {
                if (battery.player.life == 0) {
                    console.addString("GAME OVER");
                    if (battery.player.lives > 0) {
                        console.addString("TO BE CONTINUED...");
                    }
                } else {
                    console.addString("LEVEL COMPLETED");
                }
            }
            // TODO reimplement
//            else if (!battery.hasFocus()) {
//                console.displayString("CLICK MOUSE");
//            }
        }
        if (Config.debugMode) {
            String fps = "" + battery.time.fps;
            font.p(fps, w - (fps.length() * Font77.CHAR_WIDTH_MIN) - 4, h - 17);
        }
        drawStats();
        console.displayString();
    }

    private void drawStats() {
        Player p = battery.player;
        percentBar(p.getScores(), 0, 2, h - 93, "ic_star");
        percentBar(p.bullets, 0, 2, h - 70, "ic_ammo");
        percentBar(p.getCoins(), 0, 2, h - 47, "ic_coin");
        percentBar(p.life, p.maxLife, 2, h - 24, "ic_plane");
        for (int i = 0; i < p.lives; i++) {
            bufGfx.drawTransparent(getSprite("ic_plane"), 106 + (i * 15), h - 24 + 3);
        }
        if (p.extras.immortalities > 0) {
            bufGfx.rect(445, 445, 63, 63, 0xffffffff);
            Sprite s = getSprite("immune");
            bufGfx.rectShadow(446, 446, 61, 61, 0.5);
            bufGfx.drawTransparent(s, 458, 448);
            font.p("F1x" + p.extras.immortalities, 448, 492);
        }
    }

    /**
     * @param max if max==0 - shadow bar
     */
    private final void percentBar(double units, double max, int x, int y,
                                  String icon) {
        bufGfx.rect(x, y, (int) 100 + 4, 22, 0xff000000);
        bufGfx.rect(x + 1, y + 1, (int) 100 + 2, 20, 0xffffffff);
        if (max > 0) {/* lifebar */
            int c = 0xff006600;
            if (battery.player.afterDmg()) {
                c = 0xffffffff;
            } else if (units / max < 0.5) {
                if (units / max >= 0.25) {
                    c = 0xffffff00;
                } else {
                    c = 0xffcc0000;
                }
            }
            bufGfx.filledRect(x + 2, y + 2, (int) (100 * (units / max)), 18, c);
            bufGfx.filledRect(x + 2 + (int) (100 * (units / max)), y + 2, 100 - (int) (100 * (units / max)),
                    18, 0xff000000);
        } else {
            if (icon.equals("ic_ammo") && battery.player.bullets < 20
                    && Utils.rem(((int) battery.time.time) / 500, 3) < 2) {
                bufGfx.filledRect(x + 2, y + 2, 100, 18, 0xffff0000);
            } else {
                bufGfx.rectShadow(x + 2, y + 2, 100, 18, 0.5);
            }
        }
        bufGfx.drawTransparent(getSprite(icon), 6, y + 3);
        String s = (max > 0) ? ("" + (int) units + "/" + (int) max) : ("" + (int) units);
        font.pCenter(s, (int) 100 / 2 + 4, y + 10);
    }

    /**
     * Now for scroll up only.
     */
    private void drawNewBackAndForeGround() {
        Screen scr = battery.screen;
        int lvl = battery.world.trueLevel();
        if (lvl == 6) {
            Sprite map = getSprite("cosmos");
            System.arraycopy(map.pixels, 0, bufGfx.pixels, 0, 510 * 510);
        }
        Water.curlWaterSprite(battery);
        WorldSquare[][] map = battery.world.getMap();
        int yBorder = scr.getYBorder();
        int xBorder = scr.getXBorder();
        for (int yy = scr.getStartY(); yy < yBorder; yy++) {
            for (int xx = 0; xx < xBorder; xx++) {
                map[yy][xx].draw();
            }
        }
        if (scr.getStartY() + 18 < battery.world.height &&
                Utils.rem(scr.getStartY() + 18 - Screen.lastYGc, 10) == 0 &&
                scr.getStartY() + 18 != Screen.lastYGc) {
            Screen.lastYGc = scr.getStartY() + 18;
            for (int yy = Screen.lastYGc - 10; yy < Screen.lastYGc; yy++) {
                for (int xx = 0; xx < 17; xx++) {
                    map[yy][xx].drop();
                }
            }
            System.gc();
        }
    }

    private void drawChangeables() {
        Boss2AI.alreadyDrawn = false;
        java.util.List<Drawable> objs = battery.world.getChangeablesOnScreen();
        for (Drawable d : objs) {
            d.draw();
        }
    }

    public void updateScreen() {
        if (battery.exception && Config.debugMode) {
            font.p("ERROR", 3, 1);
        }
        battery.drawVideoBuffer(bufGfx.pixels);
    }

    public void newDraw() {
        Collection<DrawableLibGDX> newDrawables = battery.world.level7;
        SpriteBatch batch = battery.batch;
        batch.begin();
        for (DrawableLibGDX newDrawable : newDrawables) {
            newDrawable.draw(batch);
        }
        batch.end();
    }
}
