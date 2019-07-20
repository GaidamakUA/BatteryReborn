package b.util;

import java.text.*;
import java.util.*;

public class Date77 {
  public static final String datetime() {
    return datetime(System.currentTimeMillis());
  }
  
  public static final String datetime(long millis) {
    return date(millis)+" "+time(millis);
  }
  
  public static final String date(long millis) {
    SimpleDateFormat format=new SimpleDateFormat("yy/MM/dd");
    return format.format(new Date(millis));
  }

  public static final String time(long millis) {
    SimpleDateFormat format=new SimpleDateFormat("HH.mm.ss");
    return format.format(new Date(millis));
  }

  public static final int dayOfYear(long millis) {
    Calendar c=new GregorianCalendar();
    c.setTimeInMillis(millis);
    return c.get(Calendar.DAY_OF_YEAR);
  }
}
