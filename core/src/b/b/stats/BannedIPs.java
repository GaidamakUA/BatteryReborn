package b.b.stats;

import java.util.HashSet;
import java.util.Set;

public class BannedIPs {
    public static final String myIP = "89.254.138.114";

    public static final Set<String> get() {
        Set<String> res = new HashSet<String>();
        res.add(myIP);
        return res;
    }
}
