package b.util;

import java.io.*;

public class Serialization {
    public static synchronized void serialize(Serializable obj, String pathname) {
        File77.createNotExistingDirs(pathname);
        ObjectOutputStream s = null;
        try {
            s = new ObjectOutputStream(new FileOutputStream(pathname));
            s.writeObject(obj);
            s.flush();
            s.close();
        } catch (Exception e) {
            try {
                s.close();
            } catch (Exception ignored) {
            }
            U77.throwException(e);
        }
    }

    /**
     * on error throws RuntimeException
     */
    public static synchronized Object deserialize(String pathname) {
        ObjectInputStream s = null;
        try {
            s = new ObjectInputStream(new FileInputStream(pathname));
            Object o = s.readObject();
            s.close();
            return o;
        } catch (Exception e) {
            try {
                s.close();
            } catch (Exception ignored) {
            }
            U77.throwException(e);
            /* heh */
            return null;
        }
    }
}
