package b.b.core;

import b.b.Battery;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Keyboard77 {
    private Battery bat;
    private Logger logger;

    private Set<String> keys;
    private Set<String> downKeys;

    public Set<String> mouse;
    private Set<String> currentMouse;
    public int x;
    public int y;
    private int exX;
    private int exY;

    private boolean f1;

    public Keyboard77(Battery btr) {
        clear(btr);
    }

    synchronized public void clear(Battery btr) {
        bat = btr;
        keys = new HashSet<String>();
        downKeys = new HashSet<String>();
        mouse = new HashSet<String>();
        currentMouse = new HashSet<String>();
        x = 0;
        y = 0;
        exX = 0;
        exY = 0;
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                keyPressed(keyCodeToString(keycode));
                return true;
            }

            @Override
            public boolean keyUp(int keycode) {
                downKeys.remove(keyCodeToString(keycode));
                return true;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                currentMouse.add("mouse " + screenX + " " + screenY);
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return mouse.size() > 0;
            }
        });
        logger = btr.logger;
        f1 = true;
    }

    synchronized public void stop() {
        logger.stop();
    }

    synchronized public void next() {
        for (Iterator<String> it = keys.iterator(); it.hasNext(); ) {
            String key = it.next();
            if (!downKeys.contains(key)) {
                logger.log("key " + (long) bat.time.time);
            }
        }
        for (Iterator it = downKeys.iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            if (!keys.contains(key)) {
                logger.log("key " + (long) bat.time.time);
            }
        }
        keys.clear();
        keys.addAll(downKeys);
        if (!f1 && keys.contains("f1")) {
            keys.remove("f1");
        } else if (!keys.contains("f1")) {
            f1 = true;
        } else {
            f1 = false;
        }
        mouse.clear();
        mouse.addAll(currentMouse);
        currentMouse.clear();
        x = exX;
        y = exY;
    }

    private void keyPressed(String key) {
        downKeys.add(key);
    }

    synchronized public boolean up() {
        return keys.contains("up");
    }

    synchronized public boolean right() {
        return keys.contains("right");
    }

    synchronized public boolean down() {
        return keys.contains("down");
    }

    synchronized public boolean left() {
        return keys.contains("left");
    }

    synchronized public boolean ctrl() {
        return keys.contains("ctrl");
    }

    synchronized public boolean space() {
        return keys.contains("space");
    }

    synchronized public boolean f1() {
        return keys.contains("f1");
    }

    synchronized public boolean anyKey() {
        return !keys.isEmpty() || !mouse.isEmpty();
    }

    private static String keyCodeToString(int keyCode) {
        int code = keyCode;
        switch (code) {
            case Input.Keys.UP:
                return "up";
            case Input.Keys.DOWN:
                return "down";
            case Input.Keys.LEFT:
                return "left";
            case Input.Keys.RIGHT:
                return "right";
            case Input.Keys.CONTROL_LEFT:
            case Input.Keys.CONTROL_RIGHT:
                return "ctrl";
            case Input.Keys.SPACE:
                return "space";
            case Input.Keys.F1:
                return "f1";
        }
        return "another";
    }
}
