package b.b.monsters.items;

import b.b.core.Config;
import b.b.core.World;
import b.b.monsters.Monster;
import b.util.Utils;

public class FirstAid extends Item {
    private double xCenter;
    private double yCenter;
    private double angle1;
    private double angle2;

    public FirstAid(double x, double y, World world) {
        super(world, x, y, world.gfx.getSprite("firstaid"), ZLayer.FIVE);
        angle1 = Utils.rnd(Utils.dpi);
        angle2 = Utils.rnd(Utils.dpi);
        xCenter = x;
        yCenter = y;
        calcXY();
    }

    protected void move() {
        angle1 = Utils.angle(angle1 + Config.Monsters.FirstAid.bigRotSpeed);
        angle2 = Utils.angle(angle2 - Config.Monsters.FirstAid.smallRotSpeed);
        calcXY();
    }

    public boolean onMonster(Monster m) {
        if (life > 0 && m.life > 0) {
            dmg(1, null);
            m.life += Config.Monsters.FirstAid.effect;
            if (m.life > m.maxLife) m.life = m.maxLife;
        }
        return false;
    }

    private final void calcXY() {
        x = xCenter + (Math.sin(angle1) * Config.Monsters.FirstAid.bigRotRadius) +
                (Math.sin(angle2) * Config.Monsters.FirstAid.smallRotRadius);
        y = yCenter + (Math.cos(angle1) * Config.Monsters.FirstAid.bigRotRadius) +
                (Math.cos(angle2) * Config.Monsters.FirstAid.smallRotRadius);
    }
}
