package b.tools.sitestats77;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserStats implements Serializable {
    protected String ip;
    protected List<Hit> hits;

    protected UserStats(String ip) {
        this.ip = ip;
        hits = new ArrayList<Hit>();
    }
}
