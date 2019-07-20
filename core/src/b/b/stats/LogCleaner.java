package b.b.stats;

import b.b.core.Config;
import b.util.File77;
import b.util.FileReader77;
import b.util.Str77;

import java.util.HashSet;
import java.util.Set;

public class LogCleaner {
    private static String file = Config.Pathes.logFile;
    private static final FileReader77 fr = new FileReader77(file);
    private static final int size = fr.size();
    private static final int[] blamedIds = {};
    private static String res;
    private static Set<String> parsedIDs;

    /**
     * Removes all games with banned IPs.
     * Removes all games with log string count<20.
     * Removes empty strings with id.
     */
    public static void main(String[] args) {
        try {
            if (args.length > 0) file = args[0];
            res = "";
            parsedIDs = new HashSet<String>();
            int start = getGame("138497089", 0);
            boolean found = true;
            while (found) {
                found = false;
                for (int i = start; i < size; i++) {
                    String s = fr.get(i);
                    if (Str77.strings(s) > 1 && Str77.startsWithNumber(s)) {
                        String id = Str77.first(s);
                        if (!parsedIDs.contains(id)) {
                            start = getGame(id, i);
                            found = true;
                            break;
                        }
                    }
                }
            }
            File77.create(file, res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final int getGame(String id, int start) {
        String game = "";
        int count = 0;
        int last = start;
        boolean failed = false;
        for (int i = start; i < size; i++) {
            String s = fr.get(i);
            if (s.startsWith(id)) {
                if (Str77.strings(s) > 1) {
                    if (BannedIPs.get().contains(Str77.after(s, " addr:"))
                            || blamedContains(id)) {
                        parsedIDs.add(id);
                        return last;
                    }
                    game += s + "\n";
                    count++;
                    if (!failed) last++;
                } else failed = true;
            } else failed = true;
        }
        if (count > 19) res += game;
        parsedIDs.add(id);
        return last;
    }

    private static final boolean blamedContains(String id) {
        for (int i : blamedIds) {
            if (id.equals("" + i)) return true;
        }
        return false;
    }

}
