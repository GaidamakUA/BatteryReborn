package b.util;

import java.util.List;

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


    public static String toString(List list) {
        int size = list.size();
        if (size == 1) {
            return "" + list.get(0);
        }
        String res = "";
        for (int i = 0; i < size - 1; i++) {
            res += list.get(i) + "-";
        }
        return res + list.get(size - 1);
    }
}
