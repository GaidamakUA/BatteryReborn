package b.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Date77 {
    public static String datetime() {
        return datetime(System.currentTimeMillis());
    }

    public static String datetime(long millis) {
        return date(millis) + " " + time(millis);
    }

    public static String date(long millis) {
        SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd");
        return format.format(new Date(millis));
    }

    public static String time(long millis) {
        SimpleDateFormat format = new SimpleDateFormat("HH.mm.ss");
        return format.format(new Date(millis));
    }

    public static int dayOfYear(long millis) {
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(millis);
        return c.get(Calendar.DAY_OF_YEAR);
    }
}
