package b.tools.sitestats77;

import b.b.core.*;

public class OfflineGenerator {
  public static void main(String[] args) {
    SiteStats.init();
    SiteStats.stats.generate(Config.Pathes.statsDir+"stats");
  }
}
