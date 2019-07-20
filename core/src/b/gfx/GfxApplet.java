/* refactoring0 */
package b.gfx;

import java.applet.*;
import java.awt.*;

public abstract class GfxApplet extends Applet implements Runnable {
  public boolean exception;
  public Exception firstException;
  protected volatile boolean initialized;
  protected volatile boolean initInProgress;

  protected volatile Thread thread;

  public void update(Graphics g) {
    paint(g);
  }

  abstract protected void initialize();

  public void init() {
    exception=false;
    firstException=null;
    initialized=false;
    initInProgress=false;
    thread=null;
  }

  public synchronized void paint(Graphics g) {}

  public void start() {
    if (thread==null) {
      thread=new Thread(this);
      thread.setPriority(Thread.MAX_PRIORITY);
      thread.start();
    }
  }

  public void stop() {
    thread=null;
    destroy();
  }

  public void run() {
    while (thread != null) {
      if (!initInProgress) initialize();
      repaint();
      try {
        Thread.sleep(3);
      } catch(Exception ignored) {}
    }
  }

  public void exception(Exception e) {
    if (!exception) {
      exception=true;
      firstException=e;
      e.printStackTrace();
    }
  }
}
