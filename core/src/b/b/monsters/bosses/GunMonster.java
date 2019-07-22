package b.b.monsters.bosses;

import b.b.core.Config;
import b.b.core.World;
import b.b.monsters.ComplexAI;
import b.b.monsters.Gun;
import b.b.monsters.MonsterPart;
import b.util.U77;

public class GunMonster extends MonsterPart {
    protected Gun gun;
    protected int shift;
    protected double partY;

    private double k;

    protected GunMonster(World world, ComplexAI boss, int pshift) {
        super(world, 0, 0, world.gfx.getSprite("boss2_gun"), Config.Monsters.Boss2.gunLife,
                boss, 3);
        shift = pshift;
        partY = -15;
        k = U77.rnd() + 1;
        gun = new Gun(this, world, 0.0001, Config.Monsters.Boss2.bulletSpeed, boss);
        gun.xShift = 0;
        gun.yShift = 0;
    }

    public void draw() {
        super.draw();
        world.gfx.bufGfx.drawRangeCheck(world.gfx.getSprite("boss2_gun_part"), xScreenStart() - 1,
                (int) (yScreenStart() + 23 + partY));
    }

    protected void shoot() {
        partY += Config.Monsters.Boss2.gunPartSpeed * k;
        if (partY >= 0) {
            partY = -15;
            gun.shoot(2);
        }
    }
}
