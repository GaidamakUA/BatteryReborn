package b;

import b.b.core.*;
import b.tools.sitestats77.*;

import b.util.*;

public class PageManager {
    private static final int DEFAULT_MAIN_WIDTH = 544;

    public static final String top(String title) {
        return
                "<!DOCTYPE HTML SYSTEM>\n" +
                        "<html>\n" +
                        "  <head>\n" +
                        "    <title>" + title + "</title>\n" +
                        "    <link rel=\"stylesheet\" href=\"" + Config.siteUrl + "style.css\" type=\"text/css\">\n" +
                        "    <link rel=\"shortcut icon\" href=\"" + Config.siteUrl + "battery.ico\">\n" +
                        ((!title.equals("Comments")) ? "    <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">\n" :
                                "    <meta http-equiv=\"content-type\" content=\"text/html; charset=windows-1252\">\n") +
                        "    <meta http-equiv=\"expires\" content=\"" + new java.util.Date() + "\">\n" +
                        "    <meta http-equiv=\"no-cache\" content=\"no-store\">\n" +
                        "  </head>\n" +
                        "  <body>\n" +
                        "    <center>\n" +
                        "      <table width=\"" + 700 + "\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" height=\"100%\">\n" +
                        "        <tr>\n" +
                        "          <td align=\"left\" valign=\"top\" width=\"" + 60 + "\" background=\"" + Config.siteUrl + "/browser_online_game_2d_top_scroller/browser_online_game_right_brick.png\"></td>\n" +
                        "          <td width=\"" + 520 + "\" valign=\"top\" align=\"left\">\n";
    }

    public static final String top(boolean toplvl, String title) {
        return top(toplvl, title, "", 60, DEFAULT_MAIN_WIDTH);
    }

    public static final String top(boolean toplvl, String title, int mainWidth) {
        return top(toplvl, title, "", 60, mainWidth);
    }

    public static final String top2(boolean toplvl, String title) {
        return top(toplvl, title, (toplvl ? "" : "../"), 90, DEFAULT_MAIN_WIDTH);
    }

    private static final String top(boolean toplvl, String title, String pathToCss,
                                    int brickWidth, int mainWidth) {
        return
                "<!DOCTYPE HTML SYSTEM>\n" +
                        "<html>\n" +
                        "  <head>\n" +
                        "    <title>" + title + "</title>\n" +
                        "    <link rel=\"stylesheet\" href=\"" + Config.siteUrl + "style.css\" type=\"text/css\">\n" +
                        "    <link rel=\"shortcut icon\" href=\"" + (toplvl ? "" : "../") + pathToCss + "battery.ico\">\n" +
                        ((!title.equals("Comments")) ? "    <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">\n" :
                                "    <meta http-equiv=\"content-type\" content=\"text/html; charset=windows-1252\">\n") +
                        "    <meta http-equiv=\"expires\" content=\"" + new java.util.Date() + "\">\n" +
                        "    <meta http-equiv=\"no-cache\" content=\"no-store\">\n" +
                        "  </head>\n" +
                        "  <body>\n" +
                        "    <center>\n" +
                        "      <table width=\"" + (brickWidth * 2 + mainWidth) + "\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" height=\"100%\">\n" +
                        "        <tr>\n" +
                        "          <td align=\"left\" valign=\"top\" width=\"" + brickWidth + "\" background=\"" + Config.siteUrl + "/browser_online_game_2d_top_scroller/browser_online_game_right_brick.png\"></td>\n" +
                        "          <td width=\"" + mainWidth + "\" valign=\"top\" align=\"left\">\n";
    }

    public static final String logoAndMenu(String menuElem) {
        boolean main = menuElem.equals("Main");
        boolean news = menuElem.equals("News");
        boolean game = menuElem.equals("Game");
        boolean screens = menuElem.equals("Screens");
        boolean top = menuElem.endsWith("Top");
        boolean comments = menuElem.endsWith("Comments");
        return
                "<center>\n" +
                        (game ? "" : "  <a href=\"http:" + P.bs + "btrgame.com\"><img src=\"" +
                                Config.siteUrl + "/browser_online_game_2d_top_scroller/browser_online_game_2d_top_scroller.png\" " +
                                "alt=\"Browser Online Game. 2D Top Scroller.\" " +
                                "title=\"Browser Online Game. 2D Top Scroller.\" border=\"0\">" +
                                "</a><p>\n") +
                        (main ? "  <b>Main</b>" : "  <a href=\"" + Config.siteUrl + "\">Main</a>") + " |\n" +
                        (news ? "  <b>News</b>" : "  <a href=\"" + Config.siteUrl + "news/\">News</a>") + " |\n" +
                        (screens ? "  <b>Screenshots</b>" : "  <a href=\"" + Config.siteUrl + "screens/\">Screenshots</a>") + " |\n" +
                        (comments ? "  <b>Comments</b>" : "  <a href=\"" + Config.siteUrl + "comments.jsp\">Comments</a>") + " |\n" +
                        (top ? "  <b>Top Players</b>" : "  <a href=\"" + Config.siteUrl + "topplayers.jsp\">Top Players</a>") +
                        "</center><p>\n";
    }

    public static final String bottom() {
        if (SiteStats.stats == null) {
            SiteStats.init();
        }
        return
                "<table width=\"100%\"><tr><td width=\"12\"></td><td>\n" +
                        "<table border=\"0\" cellspacing=\"1\" cellpadding=\"0\" width=\"100%\"><tr>\n" +
                        "  <td width=\"25%\" algin=\"left\" valign=\"bottom\">\n" +
                        "  </td><td align=\"center\" valign=\"bottom\">\n" +
                        "<img src=\"http://sflogo.sourceforge.net/sflogo.php?group_id=" +
                        "174655&amp;type=6\" width=\"210\" height=\"62\" border=\"0\" " +
                        "alt=\"SourceForge.net Logo\"/><br>\n" +
                        "&copy; 2009 M77 & enter.dreams\n" +
                        "  </td>\n" +
                        "  <td width=\"25%\" align=\"right\" valign=\"bottom\">\n" +
                        "<table border=\"0\" cellspacing=\"1\" cellpadding=\"0\" bgcolor=\"b9b99a\"><tr><td bgcolor=\"3c3c3c\">\n" +
                        "<table bgcolor=\"3c3c3c\" cellspacing=\"7\" cellpadding=\"0\"><tr><td>\n" +
                        "    <table border=\"0\" cellspacing=\"3\" cellpadding=\"0\" bgcolor=\"3c3c3c\">\n" +
                        "      <tr>\n" +
                        "        <td align=\"left\">hosts:</td>\n" +
                        "        <td align=\"right\">" + 14 + "</td>\n" +
                        "      </tr><tr>\n" +
                        "        <td align=\"left\">hits:</td>\n" +
                        "        <td align=\"right\">" + 88 + "</td>\n" +
                        "      </tr>\n" +
                        "    </table>\n" +
                        "</td></tr></table>" +
                        "</td></tr></table>" +
                        "  </td>\n" +
                        "</tr></table>\n" +
                        "</td><td width=\"12\"></td></tr></table>\n" +
                        "          </td>\n" +
                        "          <td align=\"right\" valign=\"top\" width=\"60\" background=\"" +
                        Config.siteUrl + "/browser_online_game_2d_top_scroller/browser_online_game_left_brick.png\"></td>\n" +
                        "        </tr>\n" +
                        "      </table>\n" +
                        "    </center>\n" +
                        "  </body>\n" +
                        "</html>\n";
    }
}
