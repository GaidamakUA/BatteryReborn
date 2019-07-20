package b.b.core;

import java.awt.event.*;
import java.util.*;
import b.b.*;

public class Keyboard77 implements KeyListener, MouseListener {
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
    bat=btr;
    keys=new HashSet<String>();
    downKeys=new HashSet<String>();
    mouse=new HashSet<String>();
    currentMouse = new HashSet<String>();
    x=0;
    y=0;
    exX=0;
    exY=0;
    bat.addKeyListener(this);
    bat.addMouseListener(this);
    logger=btr.logger;
    f1 = true;
  }

  synchronized public void stop() {
    logger.stop();
  }

  synchronized public void next() {
    for (Iterator<String> it=keys.iterator(); it.hasNext();) {
      String key=it.next();
      if (!downKeys.contains(key)) {
        logger.log("key "+(long)bat.time.time);
      }
    }
    for (Iterator it=downKeys.iterator(); it.hasNext();) {
      String key=(String)it.next();
      if (!keys.contains(key)) {
        logger.log("key "+(long)bat.time.time);
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
    x=exX;
    y=exY;
  }

  synchronized public void keyPressed(KeyEvent ke) {
    keyPressed(keyEventToString(ke));
  }

  synchronized public boolean mouseClicked() {
    return mouse.size()>0;
  }

  private void keyPressed(String key) {
    downKeys.add(key);
  }
  synchronized public void keyReleased(KeyEvent ke) {
    downKeys.remove(keyEventToString(ke));
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

  synchronized public void mousePressed(MouseEvent e) {
    currentMouse.add("mouse "+e.getX()+" "+e.getY());
  }

  synchronized public void mouseClicked(MouseEvent e) {}

  synchronized public void keyTyped(KeyEvent ke) {}

  synchronized public void mouseEntered(MouseEvent e) {}

  synchronized public void mouseExited(MouseEvent e) {}

  synchronized public void mouseReleased(MouseEvent e) {}

  private static String keyEventToString(KeyEvent ke) {
    int code=ke.getKeyCode();
    switch (code) {
      case KeyEvent.VK_UP:
        return "up";
      case KeyEvent.VK_DOWN:
        return "down";
      case KeyEvent.VK_LEFT :
        return "left";
      case KeyEvent.VK_RIGHT:
        return "right";
      case KeyEvent.VK_CONTROL:
        return "ctrl";
      case KeyEvent.VK_SPACE:
        return "space";
      case 112:
        return "f1";
    }
    return "another";      
  }
}
