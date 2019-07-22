package b.tools.sitestats77;

import b.b.stats.BannedIPs;
import b.util.P;
import b.util.Str77;
import b.util.U77;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SiteStatsUtil {
    public synchronized static final String getFirstReferer(String ip) {
        try {
            if (SiteStats.stats == null) {
                SiteStats.init();
            }
            String res = SiteStats.stats.users.get(ip).hits.get(0).referer;
            if (res == null) return "";
            if (res.length() > 80 && res.indexOf("<a href=") == -1) {
                return "<a href=\"" + res + "\">long...</a>";
            } else return res;
        } catch (Exception e) {
            return "";
        }
    }

    public static final String ip(UserStats usr) {
        return usr.ip;
    }

    public static final List<Hit> hits(UserStats usr) {
        return usr.hits;
    }

    public static final String referer(Hit hit) {
        return hit.referer;
    }

    public static final String getFirstReferer(String ip,
                                               Map<String, UserStats> users) {
        try {
            String res = users.get(ip).hits.get(0).referer;
            if (res == null) return "";
            if (res.length() > 80 && res.indexOf("<a href=") == -1) {
                return "<a href=\"" + res + "\">long...</a>";
            } else return res;
        } catch (Exception e) {
            return "";
        }
    }

    public static List<UserStats> getUsers() {
        if (SiteStats.stats == null) SiteStats.init();
        return new ArrayList<UserStats>(SiteStats.stats.users.values());
    }

    public synchronized static final String removeMyIP() {
        SiteStats.stats.users.get(BannedIPs.myIP).hits.clear();
        for (URLStats urlStats : SiteStats.stats.urls.values()) {
            List<Hit> hitsToRemove = new ArrayList<Hit>();
            for (Hit hit : urlStats.hits) {
                if (hit.user.ip.equals(BannedIPs.myIP)) {
                    hitsToRemove.add(hit);
                }
            }
            urlStats.hits.removeAll(hitsToRemove);
        }
        return "my IP removed";
    }

    public synchronized static final String serveReferers() {
        try {
            List<Hit> hits = SiteStats.stats.allHits();
            for (Hit hit : hits) serveReferer(hit);
            return "SiteStatsUtil.serverReferers(): ok<br>\n";
        } catch (Exception e) {
            P.log("SiteStatsUtil.serveReferers():\n" + U77.toString(e));
            return "SiteStatsUtil.serverReferers(): error<br>\n" + U77.toString(e);
        }
    }

    private static void serveReferer(Hit hit) {
        String referer = hit.referer;
        if (referer == null) referer = "";
        if (referer.endsWith("long...</a>")) {
            referer = referer.substring("<a href=\"".length(), referer.lastIndexOf("\">long..."));
        }
        if (referer.indexOf("\"\"") != -1) {
            referer = Str77.replaceAllRepeating(referer, '\"', "\"");
        }
        if (referer.startsWith("ref=\"")) referer = referer.substring("ref=\"".length());
        if (referer.startsWith("http:" + P.bs + "javastats77.btr")) {
            referer = "javastats77 " + referer.substring(("http:" + P.bs + "javastats77.btrgame.com").length());
        } else if (referer.startsWith("http:" + P.bs + "www.linuxgames.com/")) {
            referer = "<a href=\"" + referer + "\">linuxgames.com</a>";
        } else if (referer.equals("http:" + P.bs + "freshmeat.net/")) {
            referer = "<a href=\"" + referer + "\">FM</a>";
        } else if (referer.startsWith("http:" + P.bs + "www.javagaming.org/forums/index.php")) {
            referer = "<a href=\"" + referer + "\">javagaming.org forums</a>";
        } else if (referer.startsWith("http:" + P.bs + "www.javagaming.org")) {
            referer = "<a href=\"" + referer + "\">www.javagaming.org page</a>";
        } else if (referer.startsWith("http:" + P.bs + "freshmeat.net/projects/javastats77/")) {
            referer = "<a href=\"" + referer + "\">FM JavaStats77</a>";
        } else if (referer.startsWith("http:" + P.bs + "freshmeat.net/projects/btrgame/")) {
            referer = "<a href=\"" + referer + "\">FM Battery</a>";
        } else if (referer.equals("http:" + P.bs + "freshmeat.net/lounge/projects/")) {
            referer = "<a href=\"" + referer + "\">FM lounge</a>";
        } else if (referer.equals("http:" + P.bs + "freshmeat.net/~M77/")) {
            referer = "<a href=\"" + referer + "\">FM M77</a>";
        } else if (referer.startsWith("http:" + P.bs + "freshmeat.net/")) {
            referer = "<a href=\"" + referer + "\">FM page</a>";
        } else if (referer.startsWith("http:" + P.bs + "sourceforge.net/projects/javastats77")) {
            referer = "<a href=\"" + referer + "\">SF JavaStats77</a>";
        } else if (referer.startsWith("http:" + P.bs + "sourceforge.net/projects/battery")) {
            referer = "<a href=\"" + referer + "\">SF Battery</a>";
        } else if (referer.startsWith("http:" + P.bs + "btanks.sourceforge.net")) {
            referer = "<a href=\"" + referer + "\">Battle Tanks</a>";
        } else if (referer.startsWith("http:" + P.bs + "gamin.ru")) {
            referer = "<a href=\"" + referer + "\">gamin.ru</a>";
        } else if (referer.startsWith("http:" + P.bs + "www.google.") ||
                referer.startsWith("http:" + P.bs + "google.")) {
            referer = "<a href=\"" + referer + "\">Google page</a>";
        } else if (referer.startsWith("http:" + P.bs + "pagead2.googlesyndication.com")) {
            referer = "<a href=\"" + referer + "\">Google add</a>";
        } else if (referer.startsWith("http:" + P.bs + "www.java-forums.org")) {
            referer = "<a href=\"" + referer + "\">java-forums page</a>";
        } else if (referer.startsWith("<a href \"")) {
            referer = "<a href=" + referer.substring("<a href= ".length());
        } else if (referer.startsWith("<a href=http")) {
            referer = "<a href=\"" + referer.substring(referer.indexOf("http:" + P.bs + ""));
        } else if (referer.startsWith("< href")) {
            referer = "<a " + referer.substring(referer.indexOf("href=\""));
        } else if (referer.length() > 60 && referer.indexOf("<a href=") == -1) {
            referer = "<a href=\"" + referer + "\">long...</a>";
        } else if (referer.indexOf("http%3A%2F%2Fwww.bubblebox.com%2Fgame%2FAction%2F954.htm&frm=0&cc=100&ga_vid=945723757.1211038971&ga_sid=1211038971&ga_hid=1502287258&ga_fc=true&flash=9.0.124&u_h=600&u_w=800&u_ah=570&u_aw=800&u_cd=16&u_tz=180&u_his=3&u_java=true&u_nplug=20&u_nmime=77") != -1) {
            referer = "<a href=\"http%3A%2F%2Fwww.bubblebox.com%2Fgame%2FAction%2F954.htm&frm=0&cc=100&ga_vid=945723757.1211038971&ga_sid=1211038971&ga_hid=1502287258&ga_fc=true&flash=9.0.124&u_h=600&u_w=800&u_ah=570&u_aw=800&u_cd=16&u_tz=180&u_his=3&u_java=true&u_nplug=20&u_nmime=77\">long...</a>";
        }
        if (referer.indexOf("\"\"") != -1) {
            referer = Str77.replaceAllRepeating(referer, '\"', "\"");
        }
        hit.referer = referer;
    }
}
