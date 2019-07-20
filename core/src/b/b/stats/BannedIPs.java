package b.b.stats;

import java.util.*;

public class BannedIPs {
  public static final String myIP="89.254.138.114";

  public static final Set<String> get() {
    Set<String> res=new HashSet<String>();
    res.add(myIP);
    return res;
  }
}
