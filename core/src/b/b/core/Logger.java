/* refactoring0 */
package b.b.core;

import b.b.Battery;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Logger implements Runnable {
    private PrintWriter out;
    private List<String> toLog;
    private boolean failed;
    private Battery bat;

    public Logger(Battery btr) {
        bat = btr;
        failed = false;
        toLog = new ArrayList<String>();
        if (!failed) {
            try {
                Socket s = new Socket(Config.Server.site, Config.Server.port);
                out = new PrintWriter(s.getOutputStream(), true);
                Thread t = new Thread(this);
                t.setPriority(Thread.MIN_PRIORITY);
                t.start();
            } catch (Exception e) {
                e.printStackTrace();
                failed = true;
            }
        }
    }

    public synchronized void stop() {
        failed = true;
    }

    public synchronized void log(String s) {
        if (!failed) {
            toLog.add(s);
        }
    }

    public void run() {
        while (!failed) {
            List<String> strs = step();
            if (strs.size() != 0) {
                log(strs);
            }
            try {
                Thread.sleep(5);
            } catch (Exception ignored) {
            }
        }
    }

    synchronized private List step() {
        List<String> strs = toLog;
        toLog = new ArrayList<String>();
        return strs;
    }

    private synchronized final void log(List<String> strs) {
        if (!failed) {
            try {
                for (String s : strs) {
                    out.println(s);
                }
            } catch (Exception e) {
                bat.exception(e);
                failed = true;
            }
        }
    }
}
