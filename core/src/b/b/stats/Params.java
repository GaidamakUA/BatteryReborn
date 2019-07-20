package b.b.stats;

import b.b.core.*;
import b.util.*;
import java.io.*;
import java.util.*;

public class Params implements Serializable {
  private static Params params = null;
  private static Random r = new Random(System.currentTimeMillis());

  private Map<String, List<Pair>> users = null;

  protected Params() {
    users = new HashMap<String, List<Pair>>();
    params = this;
  }

  public static final Params params() {
    if (params != null) {
      return params;
    } else {
      init();
      return params;
    }
  }

  private static final void init() {
    try {
      if (params == null) {
        params = new Params();
        params.users = (HashMap<String, List<Pair>>)
            Serialization.deserialize(Config.Pathes.statsDir+"params.ser");
        P.p("Params loaded successfully");
      }
    } catch(Exception e) {
      params = new Params();
      P.p("new Params created");
    }
  }

  public synchronized void save() {
    Serialization.serialize((HashMap)users,
        Config.Pathes.statsDir+"params.ser");
  }

  public synchronized void add(String ip, String param, Object value) {
    List<Pair> parms = users.get(ip);
    for (Pair p: parms) {
      if (p.o1.equals(param)) {
        p.o2 = value;
        return;
      }
    }
    parms.add(new Pair(param, value));
  }

  public synchronized List<Pair> get(String ip) {
    if (users.containsKey(ip)) {
      return users.get(ip);
    } else {
      generateParams(ip);
      save();
      return users.get(ip);
    }
  }

  public synchronized String getStringParams(String ip) {
    List<Pair> list = get(ip);
    String res = "";
    for (Pair p: list) {
      res += "<param name=\""+p.o1+"\" value=\""+p.o2+"\" />\n";
    }
    return res;
  }

  private synchronized void generateParams(String ip) {
    List<Pair> ps = new ArrayList<Pair>();
    users.put(ip, ps);
    p(ps, "speed", rnd0_5(Config.speed));
    p(ps, "levels", getLevels());
    p(ps, "maxSpeed", rnd0_5(Config.Monsters.Player.maxSpeed));
    p(ps, "maxStrafeSpeed", rnd0_5(Config.Monsters.Player.maxStrafeSpeed));
    p(ps, "minSpeed", rnd0_5(Config.Monsters.Player.minSpeed));
    p(ps, "playerLife", minusPlus(Config.Monsters.Player.life));
    p(ps, "heliMaxSpeed", rnd5(Config.Monsters.Heli.maxSpeed));
    p(ps, "heliLife", minusPlus(Config.Monsters.Heli.life));
    p(ps, "cameraSpeed", rnd1(Config.cameraSpeed));
    p(ps, "boss1Life", minusPlus(Config.Monsters.Boss1.life));
    p(ps, "dirtK", rnd1one());
    p(ps, "bulletSpeed", rnd1(Config.Monsters.Bullet.speed));
    p(ps, "defaultA", rnd0(Config.Monsters.defaultA));
    p(ps, "startBullets", irnd2(Config.Monsters.Player.bullets));
    p(ps, "b1shotInterval", rnd5(Config.Monsters.Boss1.shotInterval));
    p(ps, "b1bulletSpeed", rnd1(Config.Monsters.Boss1.bulletSpeed));
    p(ps, "b2paneLife", minusPlus(Config.Monsters.Boss2.paneLife));
    p(ps, "b2headYSpeed", rnd2(Config.Monsters.Boss2.headYSpeed));
    p(ps, "b2headXSpeed", rnd0(Config.Monsters.Boss2.headXSpeed));
    p(ps, "b2gunLife", oneTwoThreeFore());
    p(ps, "b2wellMaxRadius", rnd0(Config.Monsters.Boss2.wellMaxRadius));
    p(ps, "b2wellYShift", rnd2(Config.Monsters.Boss2.wellYShift));
    p(ps, "canShotInterval", rnd1(Config.Monsters.Cannon.shotInterval));
    p(ps, "canBallSpeed", rnd1(Config.Monsters.Cannon.ballSpeed));
    p(ps, "dmgCannonBall", rnd2(Config.Damages.cannonBall));
  }

  private static synchronized final String getLevels() {
    List<Integer> levels = new ArrayList<Integer>();
    for (int i=0; i<6; i++) {
      levels.add(new Integer(i+1));
    }
    for (int i=6; i<Config.getLevels().size(); i++) {
      while (true) {
        int lvl = 6+r.nextInt(5);
        Integer level = new Integer(lvl);
        if (!levels.contains(level)) {
          levels.add(level);
          break;
        }
      }
    }
    return Str77.toString(levels);
  }

  private static synchronized final void p(List<Pair> list, String name,
      Object v) {
    list.add(new Pair(name, v));
  }

  private static synchronized final void p(List<Pair> list, String name,
      double v) {
    list.add(new Pair(name, new Double(v)));
  }

  private static synchronized final void p(List<Pair> list, String name,
      int v) {
    list.add(new Pair(name, new Integer(v)));
  }

  private static synchronized final void p(List<Pair> list, String name,
      boolean v) {
    list.add(new Pair(name, new Boolean(v)));
  }

  private static synchronized final double rnd0_5(double v) {
    return rnd(v, 0.025);
  }

  private static synchronized final double rnd0(double v) {
    double ampl = 0.05;
    if (r.nextDouble()<0.1) {
      ampl = 0.1;
    }
    return rnd(v, ampl);
  }

  private static synchronized final double rnd1(double v) {
    double ampl = 0.1;
    if (r.nextDouble()<0.1) {
      ampl = 0.2;
    }
    return rnd(v, ampl);
  }

  private static synchronized final double rnd2(double v) {
    double ampl = 0.2;
    if (r.nextDouble()<0.1) {
      ampl = 0.4;
    }
    return rnd(v, ampl);
  }

  private static synchronized final double rnd3(double v) {
    double ampl = 0.4;
    if (r.nextDouble()<0.1) {
      ampl = 0.8;
    }
    return rnd(v, ampl);
  }

  private static synchronized final double rnd4(double v) {
    double ampl = 0.8;
    if (r.nextDouble()<0.1) {
      ampl = 0.9999;
    }
    return rnd(v, ampl);
  }

  private static synchronized final double rnd5(double v) {
    double ampl = 0.8;
    if (r.nextDouble()<0.1) {
      ampl = 1.6;
    }
    return rnd(v, ampl);
  }

  private static synchronized final int minusPlus(int v) {
    if (r.nextBoolean()) {
      return v;
    } else if (r.nextBoolean()) {
      return v-1;
    } else {
      return v+1;
    }
  }

  private static synchronized final int irnd2(int v) {
    return (int)(rnd2(v)+0.5);
  }

  private static synchronized final int irnd3(int v) {
    return (int)(rnd3(v)+0.5);
  }

  private static synchronized final int irnd4(int v) {
    return (int)(rnd4(v)+0.5);
  }

  private static synchronized final int irnd5(int v) {
    return (int)(rnd5(v)+0.5);
  }

  private static synchronized final int onetwo() {
    if (r.nextBoolean()) {
      return 1;
    } else {
      return 2;
    }
  }

  private static synchronized final int oneTwoThree() {
    if (r.nextDouble()<0.33333333333333) {
      return 1;
    } else if (r.nextDouble()<0.5) {
      return 2;
    } else {
      return 3;
    }
  }

  private static synchronized final int oneTwoThreeFore() {
    if (r.nextDouble()<0.25) {
      return 4;
    } else {
      return oneTwoThree();
    }
  }

  private static synchronized final double rnd1one() {
    return r.nextDouble();
  }

  private static synchronized final double rnd2one(double v) {
    double ampl = 0.4;
    if (r.nextDouble()<0.1) {
      ampl = 0.8;
    }
    double res = rnd(v, ampl);
    if (res > 1) {
      return 1;
    } else if (res < 0) {
      return 0;
    } else {
      return res;
    }
  }

  private static synchronized final boolean rndBool(boolean v) {
    if (r.nextDouble()<0.3) {
      return v;
    } else {
      return r.nextBoolean();
    }
  }

  private static synchronized final boolean rndBool2(boolean v) {
    if (r.nextDouble()<0.95) {
      return v;
    } else {
      return r.nextBoolean();
    }
  }

  private static synchronized final double rnd(double v, double ampl) {
    if (v>=0) {
      if (r.nextBoolean()) {
        return r.nextDouble()*ampl*v+v;
      } else {
        double res = -r.nextDouble()*ampl*v+v;
        if (res < 0) {
          return 0;
        } else {
          return res;
        }
      }
    } else {
      if (r.nextBoolean()) {
        return r.nextDouble()*ampl*v+v;
      } else {
        double res = -r.nextDouble()*ampl*v+v;
        if (res > 0) {
          return 0;
        } else {
          return res;
        }
      }
    }
  }
}