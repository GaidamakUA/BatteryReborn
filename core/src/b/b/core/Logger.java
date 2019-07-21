/* refactoring0 */
package b.b.core;

import b.b.Battery;

public class Logger {

    public Logger(Battery btr) {
    }

    public synchronized void stop() {
    }

    public synchronized void log(String s) {
        System.out.println(s);
    }
}
