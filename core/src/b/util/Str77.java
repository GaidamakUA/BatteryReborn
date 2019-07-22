package b.util;

import java.util.ArrayList;
import java.util.List;

public class Str77 {
    public static final String replaceAllRepeating(String where, char c, String with) {
        String rep = "" + c + c;
        while (where.contains(rep)) {
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

    public static final String param(String s, int index) {
        for (int i = 0; i < index; i++) {
            s = s.substring(s.indexOf(' ') + 1);
        }
        try {
            return s.substring(0, s.indexOf(' '));
        } catch (Exception e) {
            return s;
        }
    }

    public static final int iparam(String s, int index) {
        return Integer.parseInt(param(s, index));
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
}
