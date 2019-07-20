package b.b.site;

import java.io.*;
import b.util.*;

public class Comment implements Serializable {
  public long time;
  public String author;
  public String msg;

  public Comment(long time, String author, String msg) {
    this.time=time;
    this.author=check(author, "anonymous", 15*7);
    this.msg=check(msg, null, 300*7);
  }

  public String getTime() {
    return Date77.datetime(time);
  }

  private final static String check(String s, String defaultVal, int length) {
    String res="";
    if (s.length()>length) {
      return defaultVal;
    }
    for (int i=0; i<s.length(); i++) {
      char c=s.charAt(i);
      if (c=='\n' || (int)c==13) {
        res+="<br>\n";
        continue;
      } else if (c=='<' || c=='>') {
        if (defaultVal==null) {
          throw new RuntimeException("The message contains forbidden character code("+(int)s.charAt(i)+")");
        } else {
          return defaultVal;
        }
      }
      res+=c;
    }
    return res;
  }
}
