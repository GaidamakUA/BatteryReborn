package b.util;

import java.util.Random;

public class U77 {
    public static final double hpi = Math.PI / 2;
    public static final double pi34 = Math.PI * 3 / 2;
    public static final double dpi = Math.PI * 2;

    private static Random rnd = new Random(77);

    public static final void dropRandom() {
        rnd = new Random(77);
    }

    /**
     * ^k
     * \       |       /
     * \      |      /
     * \     |     /
     * \____|____/
     * ----------------> kk
     * 0   |>center<|   1
     */
    public static final double k(double kk, double center) {
        double k = (kk <= 0.5) ? kk : 1 - kk;
        center /= 2;
        return (k > 0.5 - center) ? 0 : (0.5 - center - k) / (0.5 - center);
    }

    /**
     * ^k
     * _|1
     * _/ |
     * _/   |
     * _/     |
     * -----/----------> kk
     * 0   |>center<|   1
     */
    public static double k2(double kk, double center) {
        kk -= (0.5 - (center / 2));
        return (kk < 0 || kk > center) ? -1 : kk / center;
    }

    /**
     * @return normailized angle
     */
    public static double angle(double a) {
        return rem(a, dpi);
    }

    /**
     * (-1.1, 5)=3.9
     * (1.1, 5)=1.1
     */
    private static double rem(double a, double b) {
        double r = a % b;
        if (r < 0) r += b;
        return (r == b) ? 0 : r;
    }

    /**
     * (-1, 5)=4
     * (4, 5)=4
     * (5, 5)=0
     * (101, 5)=1
     */
    public static int rem(int a, int b) {
        int r = a % b;
        if (r < 0) r += b;
        return (r == b) ? 0 : r;
    }

    public static boolean brem(int a, int b) {
        return rem(a, b) == 0;
    }

    public static double rnd() {
        return rnd.nextDouble();
    }

    public static boolean rndBool() {
        return rnd.nextBoolean();
    }

    public static double rnd(double max) {
        return rnd.nextDouble() * max;
    }

    public static double rnd(double min, double max) {
        return rnd.nextDouble() * (max - min) + min;
    }

    public static String leadZeros(String s, int length) {
        if (s.startsWith("-")) return "-" + repeat("0", length - s.length()) +
                s.substring(1);
        return repeat("0", length - s.length()) + s;
    }

    public static String sprecision(double d) {
        return "" + (d == (long) d ? "" + (long) d : d);
    }

    public static String sprecision(double d, int afterDot) {
        int k = 1;
        for (int i = 0; i < afterDot; i++) k *= 10;
        d *= k;
        d = ((double) (int) d) / k;
        return d == (int) d ? "" + (int) d : "" + d;
    }

    /**
     * @param afterDot - obligatory digit count after dot
     */
    public static String ssprecision(double d, int afterDot) {
        if (afterDot == 0) return "" + (int) (d + 0.5);
        String res = sprecision(d, afterDot);
        if (res.indexOf(".") == -1) {
            return res + "." + repeat("0", afterDot);
        } else {
            int aftDot = res.length() - 1 - res.indexOf(".");
            if (aftDot < afterDot) {
                return res + repeat("0", afterDot - aftDot);
            } else if (aftDot > afterDot) {
                return res.substring(0, res.length() + afterDot - aftDot);
            } else {
                return res;
            }
        }
    }

    private static String repeat(String s, int count) {
        String res = "";
        for (int i = 0; i < count; i++) res += s;
        return res;
    }

    public static void throwException(Exception e) {
        if (e instanceof RuntimeException) throw (RuntimeException) e;
        throw new RuntimeException(toString(e));
    }

    public static void throwException(Exception e, String msg) {
        throw new RuntimeException(msg + " " + toString(e));
    }

    public static String toString(Exception e) {
        String res = "" + e.getClass().getName() + " " + e.getMessage();
        StackTraceElement[] stack = e.getStackTrace();
        for (int i = 0; i < stack.length; i++) {
            res += " | " + stack[i];
        }
        return res + "\n";
    }

    public static int maxIndex(int[] arr) {
        int max = 0;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > arr[max]) {
                max = i;
            }
        }
        return max;
    }
}
