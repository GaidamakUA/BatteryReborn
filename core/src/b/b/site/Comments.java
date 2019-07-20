package b.b.site;

import b.b.core.*;
import b.util.*;
import java.io.*;
import java.util.*;

public class Comments implements Serializable {
  private static Comments comments = null;
  private List<Comment> comms = null;

  protected Comments() {
    comms = new ArrayList<Comment>();
    comments = this;
  }

  public static final Comments comments() {
    if (comments != null) {
      return comments;
    } else {
      init();
      return comments;
    }
  }

  public static final List<Comment> list() {
    return comments().comms;
  }

  private static final void init() {
    try {
      if (comments == null) {
        comments = new Comments();
      }
      comments.comms = (ArrayList<Comment>)Serialization.deserialize(
          Config.Pathes.statsDir+"guestbook.ser");

      P.p("Comments loaded successfully");
    } catch(Exception e) {
      comments = new Comments();
      P.p("new Comments created");
    }
  }

  public static String submitComment(String author, String msg) {
    Comment comment;
    try {
      comment=new Comment(System.currentTimeMillis(), author, msg);
      comments().comms.add(comment);
    } catch(Exception e) {
      e.printStackTrace();
      return e.getMessage();
    }
    save();
    return null;
  }

  public static void removeComment(int index) {
    comments().comms.remove(index);
    save();
  }

  private static void save() {
    Serialization.serialize((ArrayList)comments().comms,
        Config.Pathes.statsDir+"guestbook.ser");
  }
}
