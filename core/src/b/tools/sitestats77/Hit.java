package b.tools.sitestats77;

import java.io.Serializable;

public class Hit implements Serializable, Comparable {
    protected URLStats url;
    protected UserStats user;
    protected long time;
    protected String referer;
    protected String params;
    protected String locale;
    protected String browser;

    protected Hit(URLStats url, UserStats user) {
        referer = "";
        browser = "";
        this.url = url;
        this.user = user;
        time = System.currentTimeMillis();
//    params=r.getQueryString();
//    locale=r.getLocale().toString().toLowerCase();
//    for (Enumeration e=r.getHeaderNames(); e.hasMoreElements();) {
//      String name=e.nextElement().toString();
//      if (name.equals("referer")) {
//        for (Enumeration ee=r.getHeaders(name); ee.hasMoreElements();) {
//          referer += ee.nextElement().toString();
//        }
//      }
//      if (name.equals("user-agent")) {
//        for (Enumeration ee=r.getHeaders(name); ee.hasMoreElements();) {
//          browser += ee.nextElement().toString();
//        }
//      }
//    }
    }

    public int compareTo(Object o) throws ClassCastException {
        return (int) ((time - ((Hit) o).time) / 100);
    }
}
