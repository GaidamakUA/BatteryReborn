package b.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class File77 {
    /**
     * Creates file pathname with content. If some dirs of pathname doesn't
     * exist creates it.
     */
    public static void create(String pathname, String content) {
        try {
            createNotExistingDirs(pathname);
            File f = new File(pathname);
            f.createNewFile();
            PrintStream out = new PrintStream(new FileOutputStream(f));
            out.print(content);
            out.flush();
            out.close();
        } catch (Exception e) {
            U77.throwException(e, "path:\"" + pathname + "\"");
        }
    }

    /**
     * example of path: "C:/projects/Hodohi/file.txt"
     * part after last '/' is ignored
     */
    public static void createNotExistingDirs(String path) {
        try {
            char separator = '/';
            if (path.indexOf('\\') != -1) separator = '\\';
            int pos = path.lastIndexOf(separator);
            if (pos == -1) {
                File f = new File(path);
                if (f.exists()) return;
                if (path.indexOf('.') != -1) return;
                f.mkdir();
                return;
            }
            new File(path.substring(0, pos)).mkdirs();
        } catch (Exception e) {
            U77.throwException(e);
        }
    }
}
