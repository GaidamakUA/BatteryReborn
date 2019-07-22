package b.b.site;

import b.b.core.Config;
import b.util.File77;
import b.util.P;
import b.util.Serialization;
import b.util.Tripple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TopPlayers implements Serializable {
    private static List<Tripple> players = null;
    private static TopPlayers topPlayers = null;
    private static int hitsTillSave = 50;

    protected TopPlayers() {
        players = new ArrayList<Tripple>();
        topPlayers = this;
    }

    public static synchronized final TopPlayers players() {
        init();
        return topPlayers;
    }

    private static synchronized final void init() {
        try {
            if (topPlayers == null) {
                topPlayers = new TopPlayers();
                players = (ArrayList<Tripple>) Serialization.
                        deserialize(Config.Pathes.statsDir + "top_players.ser");
                P.log("top_players.ser loaded successfully");
            }
        } catch (Exception e) {
            topPlayers = new TopPlayers();
            P.log("new TopPlayers created");
        }
    }

    public synchronized void save() {
        for (int i = 0; i < players.size(); i++) {
            if (((Integer) ((Tripple) players.get(i)).o2) < 50) {
                players.remove(i--);
            }
        }
        Collections.sort(players, new Comparator() {
            public int compare(Object o1, Object o2) {
                Tripple obj1 = (Tripple) o1;
                Tripple obj2 = (Tripple) o2;
                int res = ((Integer) obj2.o2).intValue() - ((Integer) obj1.o2).intValue();
                if (res != 0) return res;
                return (int) (((Long) obj1.o3).longValue() - ((Long) obj2.o3).longValue());
            }
        });
        Serialization.serialize((ArrayList<Tripple>) players,
                Config.Pathes.statsDir + "top_players.ser");
    }

    public synchronized void add(String name, int scores, long id) {
        boolean found = false;
        for (Tripple rec : players) {
            if (((Long) rec.o3).longValue() == id) {
                found = true;
                if (((Integer) rec.o2).intValue() < scores) {
                    rec.o2 = new Integer(scores);
                }
            }
        }
        if (!found) {
            players.add(new Tripple(name, new Integer(scores), new Long(id)));
        }
        if (--hitsTillSave == 0) {
            hitsTillSave = 50;
            save();
            createJSPs();
        }
    }

    public synchronized void createJSPs() {
        String res =
                "<table border=\"0\" cellspacing=\"1\" cellpadding=\"1\" width=\"100%\" bgcolor=\"b9b99a\">\n" +
                        "  <tr><td colspan=\"3\" align=\"center\" bgcolor=\"3c3c3c\"><b>Top Players</b></td></tr>\n" +
                        "  <tr><td bgcolor=\"3c3c3c\" align=\"left\"><b>Rank</b></td><td bgcolor=\"3c3c3c\" align=\"center\"><b>Name</b></td><td bgcolor=\"3c3c3c\" align=\"right\"><img src=\"browser_online_game_2d_top_scroller/browser_online_game_star_icon.png\" border=\"0\" alt=\"Browser Online Game\" title=\"Browser Online Game\"></td></tr>\n";
        for (int i = 0; (i < players.size() && i < 10); i++) {
            res += "  <tr><td bgcolor=\"3c3c3c\" align=\"left\">" + (i + 1) + "</td><td bgcolor=\"3c3c3c\" align=\"left\">" + ((Tripple) (players.get(i))).o1 + "</td><td bgcolor=\"3c3c3c\" align=\"right\">" + ((Tripple) (players.get(i))).o2 + "</td></tr>\n";
        }
        File77.create(Config.Pathes.rootDir + "top10.jsp", res + "\n</table>\n");
        res =
                "<table border=\"0\" cellspacing=\"1\" cellpadding=\"1\" bgcolor=\"b9b99a\" width=\"10\">\n" +
                        "  <tr><td colspan=\"3\" align=\"center\" bgcolor=\"3c3c3c\"><b>Top Players</b></td></tr>\n" +
                        "  <tr><td bgcolor=\"3c3c3c\" align=\"left\"><b>Rank&nbsp;&nbsp;</b></td><td bgcolor=\"3c3c3c\" align=\"center\"><b>Name</b></td><td bgcolor=\"3c3c3c\" align=\"right\"><img src=\"star_icon.png\" border=\"0\" alt=\"Scores\" title=\"Scores\"></td></tr>\n";
        for (int i = 0; i < players.size(); i++) {
            res += "  <tr><td bgcolor=\"3c3c3c\" align=\"left\">" + (i + 1) + "</td><td bgcolor=\"3c3c3c\" align=\"left\">" + ((Tripple) (players.get(i))).o1 + "&nbsp;&nbsp;</td><td bgcolor=\"3c3c3c\" align=\"right\">" + ((Tripple) (players.get(i))).o2 + "&nbsp;&nbsp;</td></tr>\n";
        }
        File77.create(Config.Pathes.rootDir + "top_players.jsp", res + "\n</table>\n");
    }
}