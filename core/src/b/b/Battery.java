package b.b;

import b.b.core.*;
import b.b.core.objs.ChanSquare;
import b.b.core.objs.Water;
import b.b.gfx.Gfx;
import b.b.gfx.Intro;
import b.b.monsters.Monster;
import b.b.monsters.Player;
import b.b.monsters.bosses.Boss2AI;
import b.gfx.GfxApplet;
import b.gfx.Screen;
import b.util.*;

import java.applet.AudioClip;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Battery extends GfxApplet implements Runnable {
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
        try {
            name = getParameter("name");
            if (!NickAndPassValidator.valide(name)) {
                name = "anonimous";
            }
        } catch (Exception e) {
            name = "anonymous";
        }
        things = new HashMap<String, Object>();
        enableEvents(AWTEvent.KEY_EVENT_MASK);
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
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

    public void stop() {
        super.stop();
        if (audio != null) audio.stop();
        kbd.stop();
    }

    public synchronized void paint(Graphics g) {
        try {
            if (!initialized) {
                loadingScreen(g);
            } else {
                if (thread != null) {
                    gfx.M.newPixels();
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
            }
            Thread.sleep(2);
        } catch (Exception e) {
            exception(e);
        }
    }

    private void loadingScreen(Graphics g) {
        if (loadingScreenFirstTime) {
            loadingScreenFirstTime = false;
            loadingScreenY = 92;
            g.setColor(Color.black);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        g.setColor(new Color(128, 128, 64));
        g.setFont(new Font("Fixedsys", Font.BOLD, 10));
        g.drawString("BATTERY " + U77.ssprecision(Config.version, 2), 10, 20);
        g.drawString("http:" + P.bs + "btrgame.com", 10, 42);
        g.drawString("\2512009 M77 & enter.dreams", 10, 56);
        g.drawString("Loading.......", 10, 78);
        if (loading == null || loading.equals("core")) {
            loading = "core";
            prevLoading = "core";
        }
        if (!loading.equals(prevLoading)) {
            loadingScreenY += 14;
            prevLoading = loading;
        }
        g.drawString(loading, 20, loadingScreenY);
        repaint();
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
                        Config.Intervals.nextLevelDelay - (Time77.step * 2)) &&
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
            screen.setCameraY(screen.cameraY() - Config.cameraSpeed * Time77.step, world);
            if (screen.camY() < Config.squareSize) {
                screen.setCameraY(screen.cameraY(), world);
                logger.log("lvlcompl " + U77.sprecision(time.time));
                timeWhenLevelCompleted = time.time;
            } else {
                if (!((time.time - timeWhenLevelCompleted >=
                        Config.Intervals.nextLevelDelay) &&
                        (time.time - timeWhenLevelLoaded >=
                                Config.Intervals.nextLevelDelay))) {
                    screen.setCameraY(screen.cameraY() + (Config.cameraSpeed * Time77.step),
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
}
