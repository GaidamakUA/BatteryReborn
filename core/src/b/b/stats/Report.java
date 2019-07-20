package b.b.stats;

import java.util.*;
import b.b.core.*;
import b.util.*;

public class Report {
  public static String logfile=Config.Pathes.logFile;
  private static String statsDir=Config.Pathes.statsDir+"stats\\";

  private static FileReader77 fr;
  private static Map<String, Game> parsed;
  private static String id;
  private static Set<String> bannedIps;  

  public static void main(String[] args) {
    Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
    generate();
  }

  public static synchronized final void generate() {
    try {
      P.p("Report.generate start");
      parsed=new HashMap<String, Game>();
      bannedIps=new HashSet<String>();
      bannedIps.addAll(BannedIPs.get());
      fr=new FileReader77(logfile);
      boolean somethingNew=true;
      int debugCounter=0;
      while (somethingNew) {
        debugCounter++;
        if (U77.brem(debugCounter, 100)) {
          P.p("hop");
        }
        somethingNew=false;
        fr.drop();
        for (int i=0; i<fr.size(); i++) {
          String s = fr.read();
          id = id(s);
          if (id!=null && !parsed.containsKey(id(s))) {
            parse(s, i);
            somethingNew=true;
            break;
          }
        }
      }
      int gameIndex = 0;
      while (somethingNew) {
        somethingNew = false;
        for (int i=gameIndex+1; i<parsed.size(); i++) {
          if (parsed.get(i).ip.equals(parsed.get(gameIndex).ip)) {
            parsed.get(gameIndex).join(parsed.get(i));
            parsed.remove(i);
            somethingNew = true;
            break;
          }
        }
        if (gameIndex<parsed.size()-1) {
          gameIndex++;
          somethingNew = true;
        }
      }
      P.p("removeInvalids");
      removeInvalids();
      String t="</td><td>";
      String table=
"<table border=\"1\" width=\"2000\">\n"+
"<tr><td>time"+t+
"ip"+t+
"referer"+t+
"name"+t+
"v"+t+
"speed"+t+
"levels"+t+
"maxSpeed"+t+
"maxStrafeSpeed"+t+
"minSpeed"+t+
"playerLife"+t+
"heliMaxSpeed"+t+
"heliLife"+t+
"cameraSpeed"+t+
"boss1Life"+t+
"dirtK"+t+
"bulletSpeed"+t+
"deafultA"+t+
"startBullets"+t+
"b1shotInterval"+t+
"b1bulletSpeed"+t+
"b2bulletSpeed"+t+
"b2paneLife"+t+
"b2headYSpeed"+t+
"b2headXSpeed"+t+//here
"b2gunLife"+t+
"b2wellLife"+t+
"b2gunSpeed"+t+
"b2wellChaoticSpeed"+t+
"b2wellChaoticASpeed"+t+
"b2wellMaxRadius"+t+
"b2wellYShift"+t+
"b2reward"+t+
"canShotInterval"+t+
"canBallSpeed"+t+
"waterWave"+t+
"waterXWave"+t+
"yellowBck4"+t+
"yellowWarfloor"+t+
"dmgCannonBall"+t+
"dirtOn"+t+
"dirtGrass"+t+
"dirtGround"+t+
"groundDirtK"+t+
"soundOn"+t+
"length</td></tr>\n";
      for (Game game: parsed.values()) {
        table += game.toString();
      }
      table += "</table>\n";
      P.p("main table finished");
      List<String> fields=new ArrayList<String>();
      fields.add("speed");
      SuperTable.generate(table, statsDir, "length", fields);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  private static final void removeInvalids() {
    boolean removed=true;
    while(removed) {
      removed=false;
      for (Game game: parsed.values()) {
        if (game.invalid()) {
          parsed.remove(game.id);
          removed=true;
          break;
        }
      }
    }
  }

  private static final void parse(String s, int start) {
    Game game=new Game(id);
    String ip=get(s, "addr ");
    game.ip=ip;
    game.time=getLong(s, "time ");
    fr.drop();
    for (int i=start; i<fr.size(); i++) {
      s=fr.read();
      if (s.startsWith(id)) {
        /*-192120942 start clienttime 1210001243675*/
        if (starts(s, "start clienttime ")) {
          game.hisTime=getLong(s, "start clienttime ");
        /*-192120942 name chrh*/
        } else if (starts(s, "name ")) {
          game.name=get(s, "name ");
        /*138497150 version 1.0*/
        } else if (starts(s, "version ")) {
          game.version=getDouble(s, "version ");
        /*138497150 speed 1.0*/
        } else if (starts(s, "speed ")) {
          game.speed=getDouble(s, "speed ");
        /*138497150 maxSpeed 0.1*/
        } else if (starts(s, "maxSpeed ")) {
          game.maxSpeed=getDouble(s, "maxSpeed ");
        /*138497150 maxStrafeSpeed 0.05*/
        } else if (starts(s, "maxStrafeSpeed ")) {
          game.maxStrafeSpeed=getDouble(s, "maxStrafeSpeed ");
        /*138497150 minSpeed 0.025*/
        } else if (starts(s, "minSpeed ")) {
          game.minSpeed=getDouble(s, "minSpeed ");
        /*138497150 playerLife 5*/
        } else if (starts(s, "playerLife ")) {
          game.playerLife=getInt(s, "playerLife ");
        /*138497150 heliMaxSpeed 0.07*/
        } else if (starts(s, "heliMaxSpeed ")) {
          game.heliMaxSpeed=getDouble(s, "heliMaxSpeed ");
        /*138497150 bladesK 0.07*/
        } else if (starts(s, "bladesK ")) {
          game.bladesK=getDouble(s, "bladesK ");
        /*138497150 heliLife 0.07*/
        } else if (starts(s, "heliLife ")) {
          game.heliLife=getInt(s, "heliLife ");
        /*138497150 leveles 4-2-1-3*/
        } else if (starts(s, "levels ")) {
          game.levels=get(s, "levels ");
        /*138497150 cameraSpeed 0.07*/
        } else if (starts(s, "cameraSpeed ")) {
          game.cameraSpeed=getDouble(s, "cameraSpeed ");
        /*138497150 boss1Life 0.07*/
        } else if (starts(s, "boss1Life ")) {
          game.boss1Life=getInt(s, "boss1Life ");
        /*138497150 name M77*/
        } else if (starts(s, "name ")) {
          game.name=get(s, "name ");
        /*138497150 defaultA 0.07*/
        } else if (starts(s, "defaultA ")) {
          game.defaultA=getDouble(s, "defaultA ");
        /*138497150 bulletDmg 0.07*/
        } else if (starts(s, "bulletDmg ")) {
          game.bulletDmg=getDouble(s, "bulletDmg ");
        /*138497150 monsterDmg 0.07*/
        } else if (starts(s, "monsterDmg ")) {
          game.monsterDmg=getDouble(s, "monsterDmg ");
        /*138497150 squareDmg 0.07*/
        } else if (starts(s, "squareDmg ")) {
          game.squareDmg=getDouble(s, "squareDmg ");
        /*138497150 dirtK 0.07*/
        } else if (starts(s, "dirtK ")) {
          game.dirtK=getDouble(s, "dirtK ");
        /*138497150 kp 4563 mouse*/
        } else if (starts(s, "key ")) {
          game.hits.add(new Integer(getInt(s, "key ")));
        /*138497150 lvl 1 7002*/
        } else if (starts(s, "lvl ")) {
          int lvl = getInt(s, "lvl ");
          game.lvls.add(new Integer(lvl));
          game.lvl(lvl);
        /*138497150 lvlcompl 58701*/
        } else if (starts(s, "lvlcompl ")) {
          game.hits.add(new Integer(getInt(s, "lvlcompl ")));
        /*-192120926 gameover 81345*/
        } else if (starts(s, "gameover ")) {
          game.hits.add(new Integer(getInt(s, "gameover ")));
        /*-2127776210 tankTurn 515.141982613915*/
        } else if (starts(s, "tankTurn")) {
          game.tankTurn=getDouble(s, "tankTurn ");
        /*-2127776210 tankStays 515.141982613915*/
        } else if (starts(s, "tankStays")) {
          game.tankStays=getDouble(s, "tankStays ");
        /*-2127776210 tankMoves 515.141982613915*/
        } else if (starts(s, "tankMoves")) {
          game.tankMoves=getDouble(s, "tankMoves ");
        /*-2127776210 bulletSpeed 0.41032491769717605*/
        } else if (starts(s, "bulletSpeed")) {
          game.bulletSpeed=getDouble(s, "bulletSpeed ");
        /*-2127776210 shotInterval 403.468556330883*/
        } else if (starts(s, "shotInterval")) {
          game.shotInterval=getDouble(s, "shotInterval ");
        /*-2127776210 nextLevelDelay 403.468556330883*/
        } else if (starts(s, "nextLvlDelay ")) {
          game.nextLvlDelay=getDouble(s, "nextLvlDelay ");
        } else if (starts(s, "afterDmg")) {
          game.afterDmg = getDouble(s, "afterDmg ");
        } else if (starts(s, "waterTimeK")) {
          game.waterTimeK = getDouble(s, "waterTimeK ");
        } else if (starts(s, "coinDuration")) {
          game.coinDuration = getDouble(s, "coinDuration ");
        } else if (starts(s, "coinLineDur")) {
          game.coinLineDur = getDouble(s, "coinLineDur ");
        } else if (starts(s, "activationDistance")) {
          game.activationDistance = getDouble(s, "activationDistance ");
        } else if (starts(s, "afterBossPeriod")) {
          game.afterBossPeriod = getDouble(s, "afterBossPeriod ");
        } else if (starts(s, "monAfterDmg")) {
          game.monAfterDmg = getDouble(s, "monAfterDmg ");
        } else if (starts(s, "wrongDmgTime")) {
          game.wrongDmgTime = getDouble(s, "wrongDmgTime ");
        } else if (starts(s, "FAbigRotRadius")) {
          game.FAbigRotRadius = getDouble(s, "FAbigRotRadius ");
        } else if (starts(s, "FAsmallRotRadius")) {
          game.FAsmallRotRadius = getDouble(s, "FAsmallRotRadius ");
        } else if (starts(s, "FAbigRotSpeed")) {
          game.FAbigRotSpeed = getDouble(s, "FAbigRotSpeed ");
        } else if (starts(s, "FAsmallRotSpeed")) {
          game.FAsmallRotSpeed = getDouble(s, "FAsmallRotSpeed ");
        } else if (starts(s, "FAeffect")) {
          game.FAeffect = getDouble(s, "FAeffect ");
        } else if (starts(s, "startBullets")) {
          game.startBullets = getInt(s, "startBullets ");
        } else if (starts(s, "tankTurretTime")) {
          game.tankTurretTime = getDouble(s, "tankTurretTime ");
        } else if (starts(s, "b1maxSpeed")) {
          game.b1maxSpeed = getDouble(s, "b1maxSpeed ");
        } else if (starts(s, "b1maxStrafeSpeed")) {
          game.b1maxStrafeSpeed = getDouble(s, "b1maxStrafeSpeed ");
        } else if (starts(s, "b1minSpeed")) {
          game.b1minSpeed = getDouble(s, "b1minSpeed ");
        } else if (starts(s, "b1shotInterval")) {
          game.b1shotInterval = getDouble(s, "b1shotInterval ");
        } else if (starts(s, "b1bulletSpeed")) {
          game.b1bulletSpeed = getDouble(s, "b1bulletSpeed ");
        } else if (starts(s, "b1reward")) {
          game.b1reward = getInt(s, "b1reward ");
        } else if (starts(s, "b2maxSpeed")) {
          game.b2maxSpeed = getDouble(s, "b2maxSpeed ");
        } else if (starts(s, "b2StrafeSpeed")) {
          game.b2StrafeSpeed = getDouble(s, "b2StrafeSpeed ");
        } else if (starts(s, "b2minSpeed")) {
          game.b2minSpeed = getDouble(s, "b2minSpeed ");
        } else if (starts(s, "b2bulletSpeed")) {
          game.b2bulletSpeed = getDouble(s, "b2bulletSpeed ");
        } else if (starts(s, "b2paneLife")) {
          game.b2paneLife = getInt(s, "b2paneLife ");
        } else if (starts(s, "b2headYSpeed")) {
          game.b2headYSpeed = getDouble(s, "b2headYSpeed ");
        } else if (starts(s, "b2headXSpeed")) {
          game.b2headXSpeed = getDouble(s, "b2headXSpeed ");
        } else if (starts(s, "b2gunLife")) {
          game.b2gunLife = getInt(s, "b2gunLife ");
        } else if (starts(s, "b2wellLife")) {
          game.b2wellLife = getInt(s, "b2wellLife ");
        } else if (starts(s, "b2gunSpeed")) {
          game.b2gunSpeed = getDouble(s, "b2gunSpeed ");
        } else if (starts(s, "b2wellChaoticSpeed")) {
          game.b2wellChaoticSpeed = getDouble(s, "b2wellChaoticSpeed ");
        } else if (starts(s, "b2wellChaoticASpeed")) {
          game.b2wellChaoticASpeed = getDouble(s, "b2wellChaoticASpeed ");
        } else if (starts(s, "b2wellMaxRadius")) {
          game.b2wellMaxRadius = getDouble(s, "b2wellMaxRadius ");
        } else if (starts(s, "b2wellYShift")) {
          game.b2wellYShift = getDouble(s, "b2wellYShift ");
        } else if (starts(s, "b2reward")) {
          game.b2reward = getInt(s, "b2reward ");
        } else if (starts(s, "canShotInterval")) {
          game.canShotInterval = getDouble(s, "canShotInterval ");
        } else if (starts(s, "canBallSpeed")) {
          game.canBallSpeed = getDouble(s, "canBallSpeed ");
        } else if (starts(s, "waterWave")) {
          game.waterWave = getDouble(s, "waterWave ");
        } else if (starts(s, "waterXWave")) {
          game.waterXWave = getDouble(s, "waterXWave ");
        } else if (starts(s, "YellowBck4")) {
          game.yellowBck4 = getBool(s, "YellowBck4 ");
        } else if (starts(s, "YellowWarfloor")) {
          game.yellowWarfloor = getBool(s, "YellowWarfloor ");
        } else if (starts(s, "dmgCannonBall")) {
          game.dmgCannonBall = getDouble(s, "dmgCannonBall ");
        } else if (starts(s, "dirtOn")) {
          game.dirtOn = getBool(s, "dirtOn ");
        } else if (starts(s, "dirtGrass")) {
          game.dirtGrass = getBool(s, "dirtGrass ");
        } else if (starts(s, "dirtGround")) {
          game.dirtGround = getBool(s, "dirtGround ");
        } else if (starts(s, "groundDirtK")) {
          game.groundDirtK = getDouble(s, "groundDirtK ");
        } else if (starts(s, "soundOn")) {
          game.soundOn = getBool(s, "soundOn ");
        }
      }
    }
    game.finish();
    parsed.put(id, game);
  }

  private static final String get(String s, String after) {
    int from=s.indexOf(after)+after.length();
    int to=s.indexOf(" ", from);
    if (to==-1) to=s.length();
    return s.substring(from, to);
  }

  private static final int getInt(String s, String after) {
    return Integer.parseInt(get(s, after));
  }

  private static final boolean getBool(String s, String after) {
    return get(s, after).equals("true");
  }

  private static final long getLong(String s, String after) {
    return Long.parseLong(get(s, after));
  }

  private static final double getDouble(String s, String after) {
    return Double.parseDouble(get(s, after));
  }

  private static final String last(String s) {
    return s.substring(s.lastIndexOf(" ")+1);
  }

  private static final int lastInt(String s) {
    return Integer.parseInt(last(s));
  }

  private static final boolean starts(String s, String with) {
    try {
      int from=s.indexOf(" ")+1;
      return s.substring(from).startsWith(with);
    } catch(Exception e) {
      return false;
    }
  }

  private static final String id(String s) {
    try {
      return ""+Long.parseLong(s.substring(0, s.indexOf(" ")));
    } catch(Exception e) {
      return null;
    }
  }
}
