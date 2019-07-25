package b.b;

import b.b.core.*;
import b.b.core.objs.ChanSquare;
import b.b.core.objs.Water;
import b.b.gfx.Gfx;
import b.b.gfx.Intro;
import b.b.monsters.Monster;
import b.b.monsters.Player;
import b.b.monsters.bosses.Boss2AI;
import b.gfx.Screen;
import b.util.Pair;
import b.util.Time77;
import b.util.U77;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.blogspot.androidgaidamak.BatteryGame;

import java.applet.AudioClip;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Battery extends BatteryGame {
    public World world;
    public Player player;
    public Screen screen;
    public Time77 time;
    public Logger logger;
    public Keyboard77 kbd;
    public Gfx gfx;
    public volatile String loading;
    /* Double - activation time; Action */
    public java.util.List<Pair> timers;
    private final Color siftwareRendererColor = new Color();

    /* Mouse clicked (for the very begining) */
    public boolean activated;

    /* to display controls at the first game screen */
    public boolean justStarted;

    /*
     * To not act but kbd for Intervals.nextLevelDelay time
     * When player just died=time.time
     */
    public double timeWhenLevelCompleted;

    /*
     * Is set in WorldLoader::load
     * After this time nothing but kbd acts Intervals.nextLevelDelay
     */
    public double timeWhenLevelLoaded;

    public Map<String, Object> things;

    /* for loading screen */
    private Intro intro = null;
    private double lastFpsLog;
    private AudioClip audio = null;


    public void initialize() {
        loading = "core";
        activated = false;
        justStarted = true;
        timers = new ArrayList<Pair>();
        things = new HashMap<String, Object>();
        time = new Time77();
        logger = new Logger(this);
        logger.log("scores 0 ");
        kbd = new Keyboard77(this);
        loading = "config";
        new ConfigLoader(this);
        loading = "gfx";
        gfx = new Gfx(this);
        screen = new Screen(gfx.w, gfx.h);
        intro = new Intro(gfx);
        Water.init(this);
        timeWhenLevelCompleted = 0;
        timeWhenLevelLoaded = 0;
        world = null;
        player = null;
        lastFpsLog = 0;
        loading = "rest";
        System.gc();
    }

    private void init2() {
        if (player != null && player.lifes > 0) {
            world.restartLevel();
        } else {
            intro = null;
            world = new World(gfx);
            world.activeObjects.add(player);
            world.addToMap(player);
            screen.setCameraY(screen.cameraY(), world);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (audio != null) audio.stop();
        kbd.stop();
    }

    @Override
    public void paint() {
        try {
            while (time.step()) {
                step();
            }
            time.nextFrame();
            if (intro != null) {
                intro.draw(time.time);
            } else {
                gfx.drawAll();
            }
            Shop.draw(this);
            gfx.updateScreen();
            Thread.sleep(2);
        } catch (Exception e) {
            exception(e);
        }
    }

    private void step() {
        if (Boss2AI.wellLeft != null) {
            Boss2AI.wellLeft.newFrame();
        }
        if (Boss2AI.wellRight != null) {
            Boss2AI.wellRight.newFrame();
        }
        serveTimers();
        if (time.time - lastFpsLog > Config.Intervals.fpsLogPeriod) {
            lastFpsLog = time.time;
            logger.log("fps " + " " + time.fps + " " + U77.sprecision(time.time));
        }
        kbd.next();
        if (!activated && kbd.anyKey()) activated = true;
        if (intro == null || time.time > intro.startTime + intro.duration) {
            if (player == null) {
                init2();
            }
            if (Shop.on) {
                Shop.step(this);
                return;
            }
            if (time.time - timeWhenLevelLoaded >
                    Config.Intervals.nextLevelDelay) {
                if ((time.time - timeWhenLevelCompleted >
                        Config.Intervals.nextLevelDelay - (Time77.STEP * 2)) &&
                        (time.time - timeWhenLevelCompleted <
                                Config.Intervals.nextLevelDelay) && (world != null)) {
                    if (player != null && player.life <= 0) {
                        init2();
                    } else if (!Shop.on) {
                        kbd.clear(this);//todo ne sdesj
                        Shop.on = true;
                        Shop.step(this);
                        return;
                    }
                } else if (time.time - timeWhenLevelCompleted >
                        Config.Intervals.nextLevelDelay) {
                    step2();
                }
            }
        }
        if (Boss2AI.wellLeft != null) {
            Boss2AI.wellLeft.nextFrame();
        }
        if (Boss2AI.wellRight != null) {
            Boss2AI.wellRight.nextFrame();
        }
    }

    private void serveTimers() {
        for (int i = 0; i < timers.size(); i++) {
            Pair p = timers.get(i);
            double t = (Double) p.o1;
            if (t <= time.time) {
                ((Action) p.o2).act(this);
                timers.remove(i--);
            }
        }
    }

    private void step2() {
        if (activated) {
            if (justStarted && time.time - timeWhenLevelLoaded >
                    Config.Intervals.nextLevelDelay) justStarted = false;
            screen.setCameraY(screen.cameraY() - Config.cameraSpeed * Time77.STEP, world);
            if (screen.camY() < Config.squareSize) {
                screen.setCameraY(screen.cameraY(), world);
                logger.log("lvlcompl " + U77.sprecision(time.time));
                timeWhenLevelCompleted = time.time;
            } else {
                if (!((time.time - timeWhenLevelCompleted >=
                        Config.Intervals.nextLevelDelay) &&
                        (time.time - timeWhenLevelLoaded >=
                                Config.Intervals.nextLevelDelay))) {
                    screen.setCameraY(screen.cameraY() + (Config.cameraSpeed * Time77.STEP),
                            world);
                } else {
                    actAll();
                }
            }
        }
    }

    private void actAll() {
        for (Monster a : world.activeObjects) a.act();
        while (!world.objectsToAdd.isEmpty()) {
            world.activeObjects.addAll(world.objectsToAdd);
            ArrayList<Monster> objs = new ArrayList<Monster>(world.objectsToAdd);
            world.objectsToAdd.clear();
            for (Monster obj : objs) {
                if (obj == null) throw
                        new RuntimeException("null object at Battery.actAll loop");
                obj.act();
            }
        }
        for (Monster a : world.objectsToRemove) {
            world.activeObjects.remove(a);
            world.removeFromMap(a);
        }
        world.objectsToRemove.clear();
        for (ChanSquare cs : world.notMonsters) {
            cs.act();
        }
    }

    public void exception(Exception e) {
        if (!exception) {
            try {
                logger.log(U77.toString(e));
            } catch (Exception ignored) {
            }
        }
        super.exception(e);
    }

    public void drawVideoBuffer(int[] pixels) {
        // Low quality software renderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < VIEWPORT_WIDTH; i++) {
            for (int j = 0; j < VIEWPORT_HEIGHT; j++) {
                int argbColor = pixels[i + j * VIEWPORT_HEIGHT];
                Color.argb8888ToColor(siftwareRendererColor, argbColor);
                shapeRenderer.setColor(siftwareRendererColor);
                int fixedYCoordinate = VIEWPORT_HEIGHT - j - 1;
                shapeRenderer.rect(i, fixedYCoordinate, 1, 1);
            }
        }
        shapeRenderer.end();
    }
}
