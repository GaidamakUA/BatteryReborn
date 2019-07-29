package b.util;

public class Str77 {

    private static String param(String s, int index) {
        for (int i = 0; i < index; i++) {
            s = s.substring(s.indexOf(' ') + 1);
        }
        try {
            return s.substring(0, s.indexOf(' '));
        } catch (Exception e) {
            return s;
        }
    }

    public static int iparam(String s, int index) {
        return Integer.parseInt(param(s, index));
    }
}
