package b.b.gfx;

import b.b.core.Config;
import b.gfx.BufGfx;
import b.gfx.Sprite;

import java.util.HashSet;
import java.util.Set;

public class SpriteLoader {
    private Gfx g;

    protected SpriteLoader(Gfx gfx) {
        g = gfx;
    }

    protected final void load() {
        for (int i = 0; i < Config.levels.size(); i++) {
            addSprite("lvl" + Config.levels.get(i), 17,
                    getHeight(Config.levels.get(i)));
        }
        addSprite("battery", 355, 64);
        addSprite("ic_plane", 16, 16);
        addSprite("ic_coin", 16, 16);
        addSprite("ic_ammo", 16, 16);
        addSprite("ic_star", 16, 16);
        addSprite("copy", 245, 17);
        initDecor();
        initMonsters();
        initItems();
    }

    private final static int getHeight(int lvl) {
        if (lvl(lvl, 4)) return 123;
        if (lvl(lvl, 5) || lvl(lvl, 8)) return 100;
        if (lvl(lvl, 7)) return 77;
        return 200;
    }

    private final static boolean lvl(int lvl, int version) {
        return ((lvl - version == 100) || (lvl - version == 200) || lvl == version);
    }

    private final void initDecor() {
        addSprite("watcur", "water");
        addSprite("water");
        addSprite4Dir("cannon", 30);
        addSprite("yellow_border", 72);
        addSprite4Dir("bordercorner", 30);
        addSprite4Dir("borderalmost", 30);
        addSprite4Dir("border", 30);
        addSpriteRot("bordertube");
        addSprite("bordered");
        addSprite("borderno");
        addSpriteRot("trap");
        addSprite("warfloor", 34);
        addSpriteUnitedSimmetricaly("landingground", 30);
        addSprite("brickbig");
        addSpriteRot("brick");
        addSpriteRot("bricko");
        addSpriteRot("brick1");
        addSpriteRot("brick1o");
        addSpriteRot("brick2");
        addSpriteRot("brick2o");
        addSpriteRot("brick3");
        addSpriteRot("brick3o");
        addSprite("s_brickbig");
        addSpriteRot("s_brick");
        addSpriteRot("s_bricko");
        addSpriteRot("s_brick1");
        addSpriteRot("s_brick1o");
        addSpriteRot("s_brick2");
        addSpriteRot("s_brick2o");
        addSpriteRot("s_brick3");
        addSpriteRot("s_brick3o");
        addSprite("ss_brickbig");
        addSpriteRot("ss_brick");
        addSpriteRot("ss_bricko");
        addSpriteRot("ss_brick1");
        addSpriteRot("ss_brick1o");
        addSpriteRot("ss_brick2");
        addSpriteRot("ss_brick2o");
        addSpriteRot("ss_brick3");
        addSpriteRot("ss_brick3o");
        addCyanBricks();
        addSprite("ground");
        addSprite("bck0", 24);
        addSprite("grass");
        for (int i = 0; i < 4; i++) {
            addSprite("bck4_" + i);
        }
        addShop();
        if (Config.levels.contains(6) || Config.levels.contains(106) ||
                Config.levels.contains(206)) {
            addSprite("cosmos", 510, 510);
        }
    }

    private final void addShop() {
        addSprite("shop", 510, 510);
    }

    private final void addCyanBricks() {
        Set<String> sprites = (Set<String>) g.sprites.keySet();
        Set<Sprite> toAdd = new HashSet();
        for (String name : sprites) {
            if (name.startsWith("brick")) {
                Sprite s = g.getSprite(name);
                BufGfx b = new BufGfx(s);
                b.replaceColor(0xffff0000, 0xff800000);
                b = new BufGfx(s, true);
                b.replaceColor(0xffff0000, 0xff808040);
                b.replaceColor(0xff800000, 0xff808040);
                s = new Sprite("c_" + name, b);
                toAdd.add(s);
            }
        }
        for (Sprite sprite : toAdd) {
            g.sprites.put(sprite.name(), sprite);
        }
    }

    private final void addSpriteUnitedSimmetricaly(String name, int w) {
        BufGfx b = new BufGfx(w * 2, w * 2);
        Sprite sp = new Sprite(g.btr, name + ".png", w, w);
        b.draw(sp, 0, 0);
        new BufGfx(sp).flipHorizontal();
        b.draw(sp, w, 0);
        new BufGfx(sp).flipVertical();
        b.draw(sp, w, w);
        new BufGfx(sp).flipHorizontal();
        b.draw(sp, 0, w);
        Sprite s = new Sprite(name, b);
        g.putSprite(s);
    }

    private final void initMonsters() {
        initBosses();
        addSprite("cannon_ball", 21);
        addSprite4Dir("bullet", 4, 9);
        addSprite("plane", 38, 47);
        addSprite("immune", 38, 47);
        addSprite("enplane", 51, 28);
        Sprite heli = new Sprite(g.btr, "heli.png", 49, 43);
        putSprite("heli", heli);
        Sprite heliLeft = new Sprite("heliLeft", heli, false);
        new BufGfx(heliLeft).flipHorizontal();
        putSprite("heliLeft", heliLeft);
        for (int i = 0; i < 4; i++) addSprite("heli_blades" + i, 43);
        addSprite("tank_base", 46);
        addSprite4Dir("tank_turret", 46);
        Sprite expl = addSprite("expl", 15);
    }

    private final void initBosses() {
        addSprite("pix", 1, 1);
        addSpritePlusFlip("boss1shield_left", "boss1_shield.png", 27, 63,
                "boss1shield_right");
        addSprite("boss1_top", 62, 45);
        addSprite("boss1_bottom", 62, 32);
        addSprite("boss1core", 91, 48);

        addSprite("boss2_main_carcas", 181, 108);
        addSprite("boss2_carcas_cross", 10, 10);
        addSprite("boss2_carcas_horiz", 169, 6);
        addSprite("boss2_carcas_vert", 6, 100);
        addSprite("boss2_gun", 4, 31);
        addSprite("boss2_gun_part", 6, 9);
        addSpritePlusFlip("boss2_head_left", "boss2_head_left.png", 63, 87,
                "boss2_head_right");
        addSpritePlusFlip("boss2_pane_left", "boss2_pane_left.png", 135, 137,
                "boss2_pane_right");
        addSprite("boss2_well", 74, 74);
    }

    private final void addSpritePlusFlip(String name, String file, int w, int h,
                                         String flip) {
        Sprite left = new Sprite(g.btr, file, w, h);
        putSprite(name, left);
        Sprite right = new Sprite(flip, left, true);
        new BufGfx(right).flipHorizontal();
        putSprite(flip, right);
    }

    private final void initItems() {
        addSprite("firstaid");
        addSprite("caterpillar", 16, 46);
        addSprite("coin", 32);
    }

    private final Sprite addSprite(String name) {
        Sprite s = new Sprite(g.btr, name + ".png",
                Config.squareSize, Config.squareSize);
        putSprite(name, s);
        return s;
    }

    private final Sprite addSprite(String name, String fname) {
        Sprite s = new Sprite(g.btr, fname + ".png",
                Config.squareSize, Config.squareSize);
        putSprite(name, s);
        return s;
    }

    /**
     * Original, flipped horizontaly, flipped horizontaly & verticaly,
     * flipped verticaly
     */
    private final void addSprite4Dir(String name, int size) {
        Sprite sprite = new Sprite(g.btr, name + ".png", size, size);
        putSprite(name + "0", sprite);

        Sprite s = new Sprite(name + "1", sprite, false);
        new BufGfx(s).rot90();
        putSprite(name + "1", s);

        s = new Sprite(name + "2", s, false);
        BufGfx killme = new BufGfx(s);
        killme.rot90();
        putSprite(name + "2", s);

        s = new Sprite(name + "3", s, false);
        killme = new BufGfx(s);
        killme.rot90();
        putSprite(name + "3", s);
    }

    private final void addSprite4Dir(String name, int width, int height) {
        Sprite sprite = new Sprite(g.btr, name + ".png", width, height);
        putSprite(name + "0", sprite);

        Sprite s = new Sprite(name + "1", sprite, false);
        BufGfx b = new BufGfx(s);
        b.rot90();
        s.setWH(b.w, b.h);
        putSprite(name + "1", s);

        s = new Sprite(name + "2", s, false);
        b = new BufGfx(s);
        b.rot90();
        s.setWH(b.w, b.h);
        putSprite(name + "2", s);

        s = new Sprite(name + "3", s, false);
        b = new BufGfx(s);
        b.rot90();
        s.setWH(b.w, b.h);
        putSprite(name + "3", s);
    }

    private final void addSpriteRot(String name) {
        Sprite s = new Sprite(g.btr, name + ".png", Config.squareSize,
                Config.squareSize);
        putSprite(name + "|", s);
        s = new Sprite(name + "-", s, false);
        new BufGfx(s).rot90();
        putSprite(name + "-", s);
    }

    private final Sprite addSprite(String name, int size) {
        return addSprite(name, size, size);
    }

    private final Sprite addSprite(String name, int w, int h) {
        Sprite s = new Sprite(g.btr, name + ".png", w, h);
        putSprite(name, s);
        return s;
    }

    private final void putSprite(String name, Sprite s) {
        s.setName(name);
        g.putSprite(s);
    }
}