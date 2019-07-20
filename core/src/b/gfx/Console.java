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
        w = font.b.w;
        h = font.b.h;
        strings = new ArrayList<String>();
    }

    public void print(String s) {
        strings.add(s);
    }

    public void print() {
        int string = 0;
        int hStart = (int) (h / 2 - (strings.size() * (double) Font77.charH / 2) + (Font77.charH / 2));
        for (String s : strings) {
            font.pCenter(s, 255, string * font.charH + hStart);
            string++;
        }
        strings.clear();
    }
}
