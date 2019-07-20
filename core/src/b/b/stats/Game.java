package b.b.stats;

import java.util.*;
import b.tools.sitestats77.*;
import b.util.*;

public class Game implements Comparable {
  protected String id;
  protected String ip;
  protected String name;
  protected long hisTime;
  /*connection start: my time*/
  protected long time;
  protected double version;
  protected double speed;
  protected double maxSpeed;
  protected double maxStrafeSpeed;
  protected double minSpeed;
  protected int playerLife;
  protected double heliMaxSpeed;
  protected double bladesK;
  protected double heliLife;
  protected String levels;
  protected double tankTurn;
  protected double tankStays;
  protected double tankMoves;
  /*the last loaded lvl for example 201*/
  protected int truelvl;
  protected double bulletSpeed;
  protected double shotInterval;
  protected double nextLvlDelay;
  protected int length;
  /*for example 3 and not 103*/
  protected int maxLvl;
  protected double cameraSpeed;
  protected double tankSpeed;
  protected double tankBulletSpeed;
  protected double tankLife;
  protected double tankForPlayer;
  protected double boss1Life;
  protected double bulletGrowth;
  protected double defaultA;
  protected double bulletDmg;
  protected double monsterDmg;
  protected double squareDmg;
  protected double dirtK;
  protected List<Integer> lvls;
  protected List<Integer> hits;
  protected double afterDmg;
  protected double waterTimeK;
  protected double coinDuration;
  protected double coinLineDur;
  protected double activationDistance;
  protected double afterBossPeriod;
  protected double monAfterDmg;
  protected double wrongDmgTime;
  protected double FAbigRotRadius;
  protected double FAsmallRotRadius;
  protected double FAbigRotSpeed;
  protected double FAsmallRotSpeed;
  protected double FAeffect;
  protected int startBullets;
  protected double tankTurretTime;
  protected double b1maxSpeed;
  protected double b1maxStrafeSpeed;
  protected double b1minSpeed;
  protected double b1shotInterval;
  protected double b1bulletSpeed;
  protected int b1reward;
  protected double b2maxSpeed;
  protected double b2StrafeSpeed;
  protected double b2minSpeed;
  protected double b2bulletSpeed;
  protected int b2paneLife;
  protected double b2headYSpeed;
  protected double b2headXSpeed;
  protected int b2gunLife;
  protected int b2wellLife;
  protected double b2gunSpeed;
  protected double b2wellChaoticSpeed;
  protected double b2wellChaoticASpeed;
  protected double b2wellMaxRadius;
  protected double b2wellYShift;
  protected int b2reward;
  protected double canShotInterval;
  protected double canBallSpeed;
  protected double waterWave;
  protected double waterXWave;
  protected boolean yellowBck4;
  protected boolean yellowWarfloor;
  protected double dmgCannonBall;
  protected boolean dirtOn;
  protected boolean dirtGrass;
  protected boolean dirtGround;
  protected double groundDirtK;
  protected boolean soundOn;

  protected void join(Game g) {
    truelvl = g.truelvl;
    maxLvl = g.maxLvl;
    length += g.length;
    lvls.addAll(g.lvls);
  }

  protected Game(String id) {
    this.id=id;
    ip=null;
    hisTime=-1;
    time=-1;
    version=-1;
    speed=-1;
    maxSpeed=-1;
    maxStrafeSpeed=-1;
    minSpeed=-1;
    playerLife=-1;
    heliMaxSpeed=-1;
    bladesK=-1;
    heliLife=-1;
    levels="-";
    tankTurn=-1;
    tankStays=-1;
    tankMoves=-1;
    bulletSpeed=-1;
    shotInterval=-1;
    nextLvlDelay=-1;
    length=0;
    maxLvl=-1;
    cameraSpeed=-1;
    tankSpeed=-1;
    tankBulletSpeed=-1;
    tankLife=-1;
    tankForPlayer=-1;
    boss1Life=-1;
    name="anonymous";
    bulletGrowth=-1;
    defaultA=-1;
    bulletDmg=-1;
    monsterDmg=-1;
    squareDmg=-1;
    dirtK=-1;
    truelvl=-1;
    lvls=new ArrayList<Integer>();
    hits=new ArrayList<Integer>();
    afterDmg=-1;
    waterTimeK=-1;
    coinDuration=-1;
    coinLineDur=-1;
    activationDistance=-1;
    afterBossPeriod=-1;
    monAfterDmg=-1;
    wrongDmgTime=-1;
    FAbigRotRadius=-1;
    FAsmallRotRadius=-1;
    FAbigRotSpeed=-1;
    FAsmallRotSpeed=-1;
    FAeffect=-1;
    startBullets=-1;
    tankTurretTime=-1;
    b1maxSpeed=-1;
    b1maxStrafeSpeed=-1;
    b1minSpeed=-1;
    b1shotInterval=-1;
    b1bulletSpeed=-1;
    b1reward=-1;
    b2maxSpeed=-1;
    b2StrafeSpeed=-1;
    b2minSpeed=-1;
    b2bulletSpeed=-1;
    b2paneLife=-1;
    b2headYSpeed=-1;
    b2headXSpeed=-1;
    b2gunLife=-1;
    b2wellLife=-1;
    b2gunSpeed=-1;
    b2wellChaoticSpeed=-1;
    b2wellChaoticASpeed=-1;
    b2wellMaxRadius=-1;
    b2wellYShift=-1;
    b2reward=-1;
    canShotInterval=-1;
    canBallSpeed=-1;
    waterWave=-1;
    waterXWave=-1;
    yellowBck4=false;
    yellowWarfloor=false;
    dmgCannonBall=-1;
    dirtOn=false;
    dirtGrass=false;
    dirtGround=false;
    groundDirtK=-1;
    soundOn=false;
  }

  public void lvl(int lvl) {
    truelvl = lvl;
    if (lvl>200) {
      lvl-=200;
    } else if (lvl>100) {
      lvl-=100;
    }
    if (lvl>maxLvl) {
      maxLvl = lvl;
    }
  }

  public int compareTo(Object o) {
    return (int)(time-((Game)o).time);
  }

  protected void finish() {
    int prevTime = 0;
    for (int i: hits) {
      if (i-prevTime > 5000) {
        length += 5000;
      } else {
        length += i-prevTime;
      }
      prevTime = i;
    }
  }

  protected final boolean invalid() {
    return (length<5000) || BannedIPs.get().contains(ip);
  }

  public String toString() {
    String t="</td><td>";
    String lngth=U77.leadZeros(U77.ssprecision((((double)length)/1000/60),2), 5);
    String res="<tr><td>"+Date77.datetime(time)+t+
        ip+t+
        SiteStatsUtil.getFirstReferer(ip)+t+
        name+t+
        U77.ssprecision(version, 2)+t+
        U77.ssprecision(speed, 4)+t+
        U77.ssprecision(maxSpeed, 4)+t+
        U77.ssprecision(maxStrafeSpeed, 4)+t+
        U77.ssprecision(minSpeed, 4)+t+
        U77.ssprecision(playerLife, 4)+t+
        U77.ssprecision(heliMaxSpeed, 4)+t+
        U77.ssprecision(bladesK, 4)+t+
        U77.ssprecision(heliLife, 4)+t+
        levels+t+
        U77.ssprecision(tankTurn, 4)+t+
        U77.ssprecision(tankStays, 4)+t+
        U77.ssprecision(tankMoves, 4)+t+
        truelvl+t+
        U77.ssprecision(bulletSpeed, 4)+t+
        U77.ssprecision(shotInterval, 4)+t+
        U77.ssprecision(nextLvlDelay, 4)+t+
        U77.ssprecision(maxLvl, 4)+t+
        U77.ssprecision(cameraSpeed, 4)+t+
        U77.ssprecision(tankSpeed, 4)+t+
        U77.ssprecision(tankBulletSpeed, 4)+t+
        U77.ssprecision(tankLife, 4)+t+
        U77.ssprecision(tankForPlayer, 4)+t+
        U77.ssprecision(boss1Life, 4)+t+
        U77.ssprecision(bulletGrowth, 4)+t+
        U77.ssprecision(defaultA, 4)+t+
        U77.ssprecision(bulletDmg, 4)+t+
        U77.ssprecision(monsterDmg, 4)+t+
        U77.ssprecision(squareDmg, 4)+t+
        U77.ssprecision(dirtK, 4)+t+
        Str77.toString(lvls)+t+
        U77.ssprecision(afterDmg, 4)+t+
        U77.ssprecision(waterTimeK, 4)+t+
        U77.ssprecision(coinDuration, 4)+t+
        U77.ssprecision(coinLineDur, 4)+t+
        U77.ssprecision(activationDistance, 4)+t+
        U77.ssprecision(afterBossPeriod, 4)+t+
        U77.ssprecision(monAfterDmg, 4)+t+
        U77.ssprecision(wrongDmgTime, 4)+t+
        U77.ssprecision(FAbigRotRadius, 4)+t+
        U77.ssprecision(FAsmallRotRadius, 4)+t+
        U77.ssprecision(FAbigRotSpeed, 4)+t+
        U77.ssprecision(FAsmallRotSpeed, 4)+t+
        U77.ssprecision(FAeffect, 4)+t+
        U77.ssprecision(startBullets, 4)+t+
        U77.ssprecision(tankTurretTime, 4)+t+
        U77.ssprecision(b1maxSpeed, 4)+t+
        U77.ssprecision(b1maxStrafeSpeed, 4)+t+
        U77.ssprecision(b1minSpeed, 4)+t+
        U77.ssprecision(b1shotInterval, 4)+t+
        U77.ssprecision(b1bulletSpeed, 4)+t+
        U77.ssprecision(b1reward, 4)+t+
        U77.ssprecision(b2maxSpeed, 4)+t+
        U77.ssprecision(b2StrafeSpeed, 4)+t+
        U77.ssprecision(b2minSpeed, 4)+t+
        U77.ssprecision(b2bulletSpeed, 4)+t+
        U77.ssprecision(b2paneLife, 4)+t+
        U77.ssprecision(b2headYSpeed, 4)+t+
        U77.ssprecision(b2headXSpeed, 4)+t+
        U77.ssprecision(b2gunLife, 4)+t+
        U77.ssprecision(b2wellLife, 4)+t+
        U77.ssprecision(b2gunSpeed, 4)+t+
        U77.ssprecision(b2wellChaoticSpeed, 4)+t+
        U77.ssprecision(b2wellChaoticASpeed, 4)+t+
        U77.ssprecision(b2wellMaxRadius, 4)+t+
        U77.ssprecision(b2wellYShift, 4)+t+
        U77.ssprecision(b2reward, 4)+t+
        U77.ssprecision(canShotInterval, 4)+t+
        U77.ssprecision(canBallSpeed, 4)+t+
        U77.ssprecision(waterWave, 4)+t+
        U77.ssprecision(waterXWave, 4)+t+
        yellowBck4+t+
        yellowWarfloor+t+
        U77.ssprecision(dmgCannonBall, 4)+t+
        dirtOn+t+
        dirtGrass+t+
        dirtGround+t+
        U77.ssprecision(groundDirtK, 4)+t+
        soundOn+t+
        lngth+"</td></tr>\n";
    return res;
  }
}
