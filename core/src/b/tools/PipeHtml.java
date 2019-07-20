package b.tools;

public class PipeHtml {
    /**
     * @param 0 <= part <= 1
     */
    public static final String inTd(double part) {
        int size = (int) (299 * part + 0.5);
        if (size > 299) size = 299;
        return " background=\"pipes/g" + size + ".png\"";
    }
}
