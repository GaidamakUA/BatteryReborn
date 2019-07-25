package b.gfx;

import java.util.ArrayList;
import java.util.List;

public class Console {
    private Font77 font;
    private int w;
    private int h;
    private List<String> strings;

    public Console(Font77 font) {
        this.font = font;
        w = font.bufGfx.w;
        h = font.bufGfx.h;
        strings = new ArrayList<String>();
    }

    public void addString(String s) {
        strings.add(s);
    }

    public void displayString() {
        int string = 0;
        int hStart = (int) (h / 2 - (strings.size() * (double) Font77.CHAR_HEIGHT / 2) + (Font77.CHAR_HEIGHT / 2));
        for (String s : strings) {
            font.pCenter(s, 255, string * font.CHAR_HEIGHT + hStart);
            string++;
        }
        strings.clear();
    }
}
