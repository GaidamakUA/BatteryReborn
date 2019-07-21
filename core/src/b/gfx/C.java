package b.gfx;

public class C {

    public static final int mix(int base, int col, double solid) {
        double[] b = drgb(base);
        double[] c = drgb(col);
        double perc = (1 - solid);
        return c((int) (b[0] * perc + (c[0] * solid)),
                (int) (b[1] * perc + (c[1] * solid)),
                (int) (b[2] * perc + (c[2] * solid)));
    }

    /**
     * @param k 0 < k < 1: 0 - the darkest, 1 - the same
     * @return color not brighter then @param c
     */
    public static final int dark(int c, double k) {
        return (0xff000000 | ((int) (k * ((c >> 16) & 0xFF)) & 0xFF) << 16) |
                (((int) (k * ((c >> 8) & 0xFF)) & 0xFF) << 8) |
                ((int) (k * (c & 0xFF)) & 0xFF);
    }

    public static final int c(int r, int g, int b) {
        return (0xff000000 | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF));
    }

    /**
     * @param k 0 < k < 1: 0 - 128, 1 - the same
     */
    public static final int gray(int r, int g, int b, double k) {
        return (0xff000000 | (((128 + (int) (k * (r - 128))) & 0xFF) << 16) |
                (((128 + (int) (k * (g - 128))) & 0xFF) << 8) |
                (((128 + (int) (k * (b - 128)))) & 0xFF));
    }

    public static final int[] rgb(int c) {
        int[] r = new int[3];
        r[0] = (c >> 16) & 0xFF;
        r[1] = (c >> 8) & 0xFF;
        r[2] = c & 0xFF;
        return r;
    }

    public static final double[] drgb(int c) {
        double[] r = new double[3];
        r[0] = (c >> 16) & 0xFF;
        r[1] = (c >> 8) & 0xFF;
        r[2] = c & 0xFF;
        return r;
    }

    /**
     * @param k 0 - the same, 1 - white
     */
    public static final int light(int c, double k) {
        int r = (c >> 16) & 0xFF;
        int g = (c >> 8) & 0xFF;
        int b = c & 0xFF;
        return (0xff000000 | (((r + (int) (k * (255 - r))) & 0xFF) << 16) |
                (((g + (int) (k * (255 - g))) & 0xFF) << 8) | ((b + (int) (k * (255 - b))) & 0xFF));
    }

    /**
     * Inversion
     */
    public static final int inv(int c) {
        int r = (255 - ((c >> 16) & 0xFF));
        int g = (255 - ((c >> 8) & 0xFF));
        int b = (c & 0xFF);
        return (0xff000000 | (((255 - ((c >> 16) & 0xFF)) & 0xFF) << 16) |
                (((255 - ((c >> 8) & 0xFF)) & 0xFF) << 8) | ((c & 0xFF) & 0xFF));
    }

    public static final String string(int c) {
        return "[" + Integer.toHexString((c >> 24) & 0xFF)
                + " " + Integer.toHexString((c >> 16) & 0xFF)
                + " " + Integer.toHexString((c >> 8) & 0xFF)
                + " " + Integer.toHexString(c & 0xFF) + "]";
    }
}
