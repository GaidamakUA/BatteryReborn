package b.b.stats;

import java.util.*;

public class User {
  protected String ip;
  protected long timeFirst;
  protected long timeLast;
  protected String referer;
  protected List<Game> games;

  protected User(String ip, long time) {
    this.ip=ip;
    timeFirst=time;
    timeLast=time;
    games=new ArrayList<Game>();
    referer="";
  }

  protected void setReferer(String r) {
    if (r.indexOf("<a href")!=-1) {
      int index=r.indexOf("\"")+1;
      referer=r.substring(index, r.indexOf("\"", index+1));
    } else {
      referer=r;
    }
    if (referer.length()>55) referer=referer.substring(0, 45);
  }
}
