package b.tools.sitestats77;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class URLStats implements Serializable {
    /**
     * Example: /dev/index.jsp
     * And not: /dev/
     */
    protected String url;
    protected List<Hit> hits;
    protected Set<UserStats> users;

    protected URLStats(String url) {
        this.url = url;
        hits = new ArrayList<Hit>();
        users = new HashSet<UserStats>();
    }
}
