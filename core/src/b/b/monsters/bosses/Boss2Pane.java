package b.b.monsters.bosses;

import b.b.core.*;
import b.b.monsters.*;
import b.gfx.*;

public class Boss2Pane extends MonsterPart {
  public Boss2Pane(World world, double x, double y, Sprite sprite, Boss2AI boss) {
    super(world, x, y, sprite, Config.Monsters.Boss2.paneLife, boss, 4);
  }

  public void draw() {}

  protected void drawFromBoss() {
    super.draw();
  }
}
