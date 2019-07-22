package b.util;

import java.io.Serializable;

public class Triple implements Serializable {
    public Object o1;
    public Object o2;
    public Object o3;

    public Triple(Object o1, Object o2, Object o3) {
        this.o1 = o1;
        this.o2 = o2;
        this.o3 = o3;
    }
}
