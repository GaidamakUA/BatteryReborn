package b.gfx;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Font77 {
    public static final int CHAR_WIDTH = 9;
    public static final int CHAR_HEIGHT = 17;
    public static final int CHAR_WIDTH_MIN = CHAR_WIDTH - 1;

    protected BufGfx bufGfx;

    private Map<String, int[][]> font;

    /*
     * Default transp color
     */
    private int transparent;

    private int color;
    private int outlineColor;

    private static int[][] m;

    public Font77(int color, int outlineColor, BufGfx bufGfx) {
        this.color = color;
        this.outlineColor = outlineColor;
        transparent = this.color + 1;
        if (transparent == this.outlineColor) transparent++;
        this.bufGfx = bufGfx;
        font = new HashMap<String, int[][]>();

        long[] data = {0L, 0L, 0L, 0L, 0L, 0L, 0L, 14336L, 103080787968L, 0L, 0L, 0L, 0L, 0L,
                1732759956630798336L, 0L, 0L, 0L, 0L, 402653184L, 6878802441404416L, 0L, 0L,
                504118332L, 3494261689388105824L, 1688978715580428L, 6951306024846360576L,
                13510798882111488L, 1764351642238336102L, 4325257056306281596L,
                4358425052969329510L, 7385354118815954492L, -2853531741037002752L,
                105657202916400L, 26826366124128L, 449290259900280928L,
                1731183695049523206L, 13511211198996504L, 48L, 15462L, 7380379856504494086L,
                7377005468203771494L, 7356742581250057734L, 61709829028633088L,
                7782246647454498816L, 6924337306922385408L, 1758092756077512294L,
                3998183158383128588L, 7378651543777459248L, 4502605928269905532L,
                4358427148243854950L, 7162524950449251942L, 6944663214081076320L,
                8607335902825832472L, 7378694116194254990L, 4378483315007581238L,
                1736137656352911384L, 3465533210951948390L, 6755502728296204L,
                434094202113783398L, 461168601839199846L, 1732872211697919590L,
                7380942600299506534L, 7351676031618932832L, 6946265891132697467L,
                7378697628572739174L, 7717030163225001167L, 3904634072011124748L,
                138642351206424L, 871477434480853040L, 3390946256096284L,
                3926125229438797414L, 6946352168335448076L, 7789093643086095984L,
                6931152416595011084L, 7384883482272227454L, 1731203462285190780L,
                7384803982057040664L, 4330211042133751320L, 1729488356293508608L,
                35492339232279564L, 6291462L, 1736202579480045062L, 7356751204923629670L,
                9092880236337199128L, 7738985599673056304L, 7378702792029404774L,
                6946345442550880262L, 7809359845703837292L, 871559117966940208L,
                27329783672832L, 8006327065655672344L, 1736190845428039680L, 206159222808L,
                4254828964734199344L, 7353364881479196720L, 7378611481125350246L,
                7378697602099865190L, 7727163418723378790L, 6944663214087367776L,
                7161680396670076440L, 7378644851616645120L, 58404985771157046L,
                3462142213542644760L, 3472288558381793280L, 6755503325590284L,
                6946246125589390872L, 7378697629379618406L, 1732872211697919590L,
                7376902994576291430L, 7377008779623948384L, 7378611867576460131L,
                7377008779618444860L, 3919847326268528832L, 3919920582942011404L,
                202911768L, 871477434480860172L, 2031176312085577276L, 466188739299262076L,
                4340973268734737932L, 7385449729686781536L, 8943654183717190782L,
                7384843977194749542L, 4340456833897675872L, 4352232164685264486L,
                1764848104386803260L, 1945619910614319104L, 6700955864088L,
                2025493932880043104L, 6924284427082137600L, 0L, 6597070553088L,
                412417523712L, 201326592L, 0L, 3072L, 0L, 1576448L, 26491358281728L,
                439153843508874240L, 13194139557888L, 0L, 0L, 432345615767175168L,
                27028194833989632L, 26388279066624L, 0L, 100663296L, 0L, 103079215104L,
                878201927337246744L, 3462155614241685504L, 1729382256910270464L, 0L, 31744L,
                33776997205278816L, 432345564227567616L, -1152921504606846976L, 0L, 0L, 0L,
                0L, 71776119062805504L, 4323455642275676160L, 0L, 0L, 2080374904L, 6292992L,
                61440L, 0L, 0L, 0L, 0L, 255L, 104085863424L, 0L, 0L};

        String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTU" +
                "VWXYZ`~!@#$%^&*()-_=+\\|[{]};:'\",<.>/?";

        for (int i = 0; i < chars.length(); i++) {
            int[][] charMap = new int[CHAR_WIDTH][CHAR_HEIGHT];
            for (int j = 0; j < CHAR_WIDTH; j++) Arrays.fill(charMap[j], transparent);
            font.put("" + chars.charAt(i), charMap);
        }

        int[][] charMap = new int[CHAR_WIDTH][CHAR_HEIGHT];
        for (int j = 0; j < CHAR_WIDTH; j++) Arrays.fill(charMap[j], transparent);
        font.put(" ", charMap);

        charMap = font.get("y");
        for (int y = 0; y < CHAR_HEIGHT; y++) {
            if (charMap[0][y] == this.color) charMap[0][y] = this.outlineColor;
        }

        int strLength = chars.length() * CHAR_WIDTH_MIN;
        for (int y = 0; y < CHAR_HEIGHT - 2; y++) {
            for (int x = 0; x < strLength; x++) {
                int bitNumber = y * strLength + x;
                int dataElemNumber = bitNumber / 64;
                charMap = font.get("" + chars.charAt(x / CHAR_WIDTH_MIN));
                int charX = x - (x / CHAR_WIDTH_MIN * CHAR_WIDTH_MIN);
                if ((data[dataElemNumber] & (1L << (63 - bitNumber))) != 0L) {
                    charMap[charX][y + 1] = this.color;
                    for (int xx = charX - 1; xx < charX + 2; xx++) {
                        for (int yy = y - 1; yy < y + 2; yy++) {
                            if (xx >= 0 && xx < CHAR_WIDTH && yy >= 0 & yy < CHAR_HEIGHT) {
                                if (charMap[xx][yy + 1] != this.color) charMap[xx][yy + 1] = this.outlineColor;
                            }
                        }
                    }
                }
            }
        }

        charMap = font.get("_");
        for (int y = 0; y < CHAR_HEIGHT; y++) {
            if (charMap[0][y] != transparent) charMap[0][y] = this.outlineColor;
        }
    }

    public void p(String s, int x, int y) {
        p(s, x, y, bufGfx);
    }

    public void pCenter(String s, int x, int y) {
        p(s, x - (length(s) / 2), y - (CHAR_HEIGHT / 2) + 1);
    }

    public void p(String s, int x, int y, BufGfx b) {
        int[] t = b.pixels;
        int w = b.width;
        for (int i = 0; i < s.length(); i++) {
            m = font.get("" + s.charAt(i));
            for (int x0 = 0; x0 < CHAR_WIDTH; x0++) {
                for (int y0 = 0; y0 < CHAR_HEIGHT; y0++) {
                    if (m[x0][y0] != transparent) t[(y + y0) * w + x + (i * CHAR_WIDTH_MIN) + x0] =
                            m[x0][y0];
                }
            }
        }
    }

    public void p(String s, int x, int y, int c, BufGfx b) {
        int[] t = b.pixels;
        int w = b.width;
        int clr;
        for (int i = 0; i < s.length(); i++) {
            m = font.get("" + s.charAt(i));
            for (int x0 = 0; x0 < CHAR_WIDTH; x0++) {
                for (int y0 = 0; y0 < CHAR_HEIGHT; y0++) {
                    if (m[x0][y0] != transparent) t[(y + y0) * w + x + (i * CHAR_WIDTH_MIN) + x0] =
                            m[x0][y0];
                    clr = m[x0][y0];
                    if (clr != transparent) {
                        if (clr == this.color) {
                            t[(y + y0) * w + x + (i * CHAR_WIDTH_MIN) + x0] = c;
                        } else {
                            t[(y + y0) * w + x + (i * CHAR_WIDTH_MIN) + x0] = clr;
                        }
                    }
                }
            }
        }
    }

    private static int length(String s) {
        return s.length() * CHAR_WIDTH_MIN;
    }
}
