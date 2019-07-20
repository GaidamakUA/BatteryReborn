package b.b.monsters.bosses;

import b.b.core.Config;
import b.b.core.World;
import b.b.monsters.MonsterPart;
import b.gfx.Sprite;

public class Boss2Pane extends MonsterPart {
    public Boss2Pane(World world, double x, double y, Sprite sprite, Boss2AI boss) {
        super(world, x, y, sprite, Config.Monsters.Boss2.paneLife, boss, 4);
    }

    public void draw() {
    }

    protected void drawFromBoss() {
        super.draw();
    }
}
