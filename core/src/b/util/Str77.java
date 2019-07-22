package b.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Str77 {
    public static final String replaceAllRepeating(String where, char c, String with) {
        String rep = "" + c + c;
        while (where.indexOf(rep) != -1) {
            int i = where.indexOf(rep);
            where = where.substring(0, i) + with + where.substring(i + 2);
        }
        return where;
    }

    public static void main(String[] args) {
        P.log(replaceAllRepeating("<a href=\"\"\"\"\"\"\"\"\"\"\"\"\"\"" +
                "\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"" +
                "\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"" +
                "\"\"\"\"\"\"\"http://searchportal.information.com/?epl=01540021R1\">" +
                "long...</a>", '\"', "\""));
    }

    /**
     * @return strings separated with ' ' and ':'
     */
    public static final int strings(String s) {
        int res = 0;
        boolean was = false;
        final int length = s.length();
        for (int i = 0; i < length; i++) {
            char c = s.charAt(i);
            if (c == ' ' || c == ':') {
                was = false;
            } else {
                if (!was) {
                    was = true;
                    res++;
                }
            }
        }
        return res;
    }

    public static final String param(String s, int index) {
        for (int i = 0; i < index; i++) {
            s = s.substring(s.indexOf(' ') + 1);
        }
        try {
            return s.substring(0, s.indexOf(' '));
        } catch (Exception e) {
            return s.substring(0);
        }
    }

    public static final int iparam(String s, int index) {
        return Integer.parseInt(param(s, index));
    }

    /**
     * Aeparated with ' ' or ':' or '\n'
     */
    public static final boolean startsWithNumber(String s) {
        try {
            Double.parseDouble(s.substring(0, end(s)));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Till first spec char.
     */
    public static final String first(String s) {
        return s.substring(0, end(s));
    }

    /**
     * @return "" if had not found
     */
    public static final String after(String s, String before) {
        try {
            int start = s.indexOf(before) + before.length();
            int end = s.indexOf(' ', start + 1);
            return s.substring(start, end);
        } catch (Exception e) {
            return "";
        }
    }

    public static final String toString(Object[] objs) {
        return toString(Arrays.asList(objs));
    }

    public static final String toString(double[] arr) {
        List list = new ArrayList();
        for (int i = 0; i < arr.length; i++) {
            list.add(new Double(arr[i]));
        }
        return toString(list);
    }

    /**
     * @param s example: "0-1-23-666-4"
     */
    public static final Integer[] integerArr(String s) {
        List list = new ArrayList();
        int index = s.indexOf("-");
        while ((index > -1) && !s.equals("")) {
            list.add(s.substring(0, index));
            if (index + 1 >= s.length()) break;
            s = s.substring(index + 1);
            index = s.indexOf("-");
            if (index == -1 || index >= s.length()) index = s.length();
            if (index == 0) break;
        }
        Integer[] res = new Integer[list.size()];
        for (int i = 0; i < list.size(); i++) {
            res[i] = new Integer((String) list.get(i));
        }
        return res;
    }

    public static final String toString(List list) {
        int size = list.size();
        if (size == 1) return "" + list.get(0);
        String res = "";
        for (int i = 0; i < size - 1; i++) {
            res += list.get(i) + "-";
        }
        return res + list.get(size - 1);
    }

    private static final int end(String s) {
        int end = s.length();
        int e = (s.indexOf(' ') == -1) ? end : (s.indexOf(' '));
        end = Math.min(e, end);
        e = (s.indexOf(':') == -1) ? end : (s.indexOf(':'));
        end = Math.min(e, end);
        e = (s.indexOf('\n') == -1) ? end : (s.indexOf('\n'));
        return Math.min(e, end);
    }
}
