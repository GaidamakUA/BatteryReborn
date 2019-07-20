package b.b.stats;

import b.b.core.Config;

import java.io.FileOutputStream;
import java.io.PrintWriter;

public class Out {
    private static PrintWriter out = createInstance();

    private static PrintWriter createInstance() {
        try {
            return new PrintWriter(new FileOutputStream(Config.Pathes.statsFile,
                    true), true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void p(String s) {
        out.println(s);
    }
}
