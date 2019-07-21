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
import b.util.P;
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
import java.util.Random;

public class Battery extends BatteryGame {
    /*for gfx need*/
    public static Random r = new Random();
    public World world;
    public Player player;
    public Screen screen;
    public Time77 time;
    public Logger logger;
    public Keyboard77 kbd;
    public Gfx gfx;
    public volatile String loading;
    public String prevLoading;
    private int loadingScreenY;
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
    public static String name;
    private AudioClip audio = null;
    private boolean loadingScreenFirstTime = true;


    public void initialize() {
        loading = "core";
        initInProgress = true;
        activated = false;
        justStarted = true;
        timers = new ArrayList<Pair>();
        name = "anonymous";
        // TODO implement it another way
//        try {
//            name = getParameter("name");
//            if (!NickAndPassValidator.valide(name)) {
//                name = "anonimous";
//            }
//        } catch (Exception e) {
//            name = "anonymous";
//        }
        things = new HashMap<String, Object>();
        time = new Time77();
        logger = new Logger(this);
        logger.log("name " + name + " ");
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
        initialized = true;
    }

    private final void init2() {
        if (player != null && player.lifes > 0) {
            world.restartLevel();
        } else {
            intro = null;
            world = new World(gfx);
            world.activeObjs.add(player);
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
            if (!initialized) {
                loadingScreen();
            } else {
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
            }
            Thread.sleep(2);
        } catch (Exception e) {
            exception(e);
        }
    }

    private void loadingScreen() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if (loadingScreenFirstTime) {
            loadingScreenFirstTime = false;
            loadingScreenY = 92;
            shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.rect(0, 0, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        }
        shapeRenderer.end();
        batch.begin();
        font.setColor(new Color(0x808040FF));
        drawText("BATTERY " + U77.ssprecision(Config.version, 2), 10, 20);
        drawText("http:" + P.bs + "btrgame.com", 10, 42);
        drawText("\2512009 M77 & enter.dreams", 10, 56);
        drawText("Loading.......", 10, 78);
        if (loading == null || loading.equals("core")) {
            loading = "core";
            prevLoading = "core";
        }
        if (!loading.equals(prevLoading)) {
            loadingScreenY += 14;
            prevLoading = loading;
        }
        font.draw(batch, loading, 20, loadingScreenY);
        batch.end();
    }

    private void drawText(String text, int x, int yTop) {
        // Transforming font coordinates from AWT to LibGDX
        int fontY = VIEWPORT_HEIGHT - yTop + FONT_SIZE;
        font.draw(batch, text, x, fontY);
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

    private final void serveTimers() {
        for (int i = 0; i < timers.size(); i++) {
            Pair p = timers.get(i);
            double t = (Double) p.o1;
            if (t <= time.time) {
                ((Action) p.o2).act(this);
                timers.remove(i--);
            }
        }
    }

    private final void step2() {
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

    private final void actAll() {
        for (Monster a : world.activeObjs) a.act();
        while (!world.objsToAdd.isEmpty()) {
            world.activeObjs.addAll(world.objsToAdd);
            ArrayList<Monster> objs = new ArrayList<Monster>(world.objsToAdd);
            world.objsToAdd.clear();
            for (Monster obj : objs) {
                if (obj == null) throw
                        new RuntimeException("null object at Battery.actAll loop");
                obj.act();
            }
        }
        for (Monster a : world.objsToRemove) {
            world.activeObjs.remove(a);
            world.removeFromMap(a);
        }
        world.objsToRemove.clear();
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
                siftwareRendererColor.set(pixels[i + j * VIEWPORT_HEIGHT]);
                shapeRenderer.setColor(siftwareRendererColor);
                shapeRenderer.rect(i, j, 1, 1);
            }
        }
        shapeRenderer.end();
    }
}
