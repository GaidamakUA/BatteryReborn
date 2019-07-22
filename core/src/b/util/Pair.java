package b.util;

import java.io.Serializable;

public class Pair implements Serializable {
    public Object o1;
    public Object o2;

    public Pair(Object o1, Object o2) {
        this.o1 = o1;
        this.o2 = o2;
    }
}
