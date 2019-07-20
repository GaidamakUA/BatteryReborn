package b.b.gfx;

import b.b.*;
import b.b.core.*;
import b.gfx.*;
import java.awt.image.*;
import java.util.*;
import java.awt.*;
import b.b.core.objs.*;
import b.b.monsters.*;
import b.b.monsters.bosses.Boss2AI;
import b.util.*;

public class Gfx {
  public MemoryImageSource M=null;
  public int w;
  public int h;
  public BufGfx b;
  public Battery btr;
  public b.gfx.Font77 font;
  public Console console;

  protected Map<String, Sprite> sprites;

  private Image screenImg;

  public Gfx(Battery battery) {
    btr=battery;
    w=btr.getSize().width;
    h=btr.getSize().height;
    b=new BufGfx(new int[w*h], w, h);
    font=new b.gfx.Font77(0xffffffff, 0xff000000, b);
    console=new Console(font);
    M=new MemoryImageSource(w, h, new DirectColorModel(32, 0xff0000, 65280,
        255, 0), b.b, 0, w);
    M.setAnimated(true);
    M.setFullBufferUpdates(true);
    screenImg=btr.createImage(M);
    sprites=new HashMap<String, Sprite>();
    new SpriteLoader(this).load();
  }

  public final Sprite getSprite(String name) {
    Sprite s=sprites.get(name);
    if (s==null) {
      throw new RuntimeException("Gfx:getSprite ["+name+"] returns null");
    }
    return s;
  }

  protected final void putSprite(Sprite sprite) {
    sprites.put(sprite.name(), sprite);
  }

  public void drawAll() {
    drawNewBackAndForeGround();
    int lvl = btr.world.trueLevel();
    drawChangeables();
    if (btr.justStarted) {
      console.print("Use arrow keys to move the airplane.");
      console.print("Use Ctrl to shoot.");
      console.print("");
    }
    if (!btr.activated) {
      console.print("CLICK MOUSE TO START");
    } else {
      if (btr.time.time-btr.timeWhenLevelCompleted<
          Config.Intervals.nextLevelDelay) {
        if (btr.player.life==0) {
          console.print("GAME OVER");
          if (btr.player.lifes>0) {
            console.print("TO BE CONTINUED...");
          }
        } else {
          console.print("LEVEL COMPLETED");
        }
      } else if (!btr.hasFocus()) {
        console.print("CLICK MOUSE");
      }
    }
    if (Config.debugMode) {
      String fps=""+btr.time.fps;
      font.p(fps, w-(fps.length()*Font77.charWMin)-4, h-17);
    }
    drawStats();
    console.print();
  }

  private void drawStats() {
    Player p = btr.player;
    percentBar(p.getScores(), 0, 2, h-93, "ic_star");
    percentBar(p.bullets, 0, 2, h-70, "ic_ammo");
    percentBar(p.getCoins(), 0, 2, h-47, "ic_coin");
    percentBar(p.life, p.maxLife, 2, h-24, "ic_plane");
    for (int i=0; i<p.lifes; i++) {
      b.drawTransp(getSprite("ic_plane"), 106+(i*15), h-24+3);
    }
    if (p.extras.immortalities>0) {
      b.rect(445, 445, 63, 63, 0xffffffff);
      Sprite s = getSprite("immune");
      b.rectShadow(446, 446, 61, 61, 0.5);
      b.drawTransp(s, 458, 448);
      font.p("F1x" + p.extras.immortalities, 448, 492);
    }
  }

  /**
   * @param max if max==0 - shadow bar
   */
  private final void percentBar(double units, double max, int x, int y,
      String icon) {
    b.rect(x, y, (int)100+4, 22, 0xff000000);
    b.rect(x+1, y+1, (int)100+2, 20, 0xffffffff);
    if (max>0) {/* lifebar */
      int c=0xff006600;
      if (btr.player.afterDmg()) {
        c=0xffffffff;
      } else if (units/max<0.5) {
        if (units/max>=0.25) {
          c=0xffffff00;
        } else {
          c=0xffcc0000;
        }
      }
      b.filledRect(x+2, y+2, (int)(100*(units/max)), 18, c);
      b.filledRect(x+2+(int)(100*(units/max)), y+2, 100-(int)(100*(units/max)),
          18, 0xff000000);
    } else {
      if (icon.equals("ic_ammo") && btr.player.bullets<20
           && U77.rem(((int)btr.time.time)/500, 3)<2) {
        b.filledRect(x+2, y+2, 100, 18, 0xffff0000);
      } else {
        b.rectShadow(x+2, y+2, 100, 18, 0.5);
      }
    }
    b.drawTransp(getSprite(icon), 6, y+3);
    String s=(max>0)?(""+(int)units+"/"+(int)max):(""+(int)units);
    font.pCenter(s, (int)100/2+4, y+10);
  }

  /**
   * Now for scroll up only.
   */
  public void drawNewBackAndForeGround() {
    Screen scr=btr.screen;
    int lvl = btr.world.trueLevel();
    if (lvl == 6) {
      Sprite map = getSprite("cosmos");
      System.arraycopy(map.b, 0, b.b, 0, 510*510);
    }
    Water.prepareWatCur(btr);
    WorldSquare[][] map = btr.world.getMap();
    int yBorder=scr.getYBorder();
    int xBorder=scr.getXBorder();
    for (int yy=scr.getStartY(); yy<yBorder; yy++) {
      for (int xx=0; xx<xBorder; xx++) {
        map[yy][xx].draw();
      }
    }
    if (scr.getStartY()+18<btr.world.height &&
        U77.rem(scr.getStartY()+18-Screen.lastYGc, 10) == 0 &&
        scr.getStartY()+18 != Screen.lastYGc) {
      Screen.lastYGc = scr.getStartY()+18;
      for (int yy = Screen.lastYGc-10; yy<Screen.lastYGc; yy++) {
        for (int xx=0; xx<17; xx++) {
          map[yy][xx].drop();
        }
      }
      System.gc();
    }
  }

  private void drawChangeables() {
    Boss2AI.alreadyDrawn = false;
    java.util.List<Drawable> objs=btr.world.getChangeablesOnScreen();
    for (Drawable d: objs) {
      d.draw();
    }
  }

  public void updateScreen() {
    if (btr.exception && Config.debugMode) {
      font.p("ERROR", 3, 1);
    }
    btr.getGraphics().drawImage(screenImg, 0, 0, btr);
  }
}
