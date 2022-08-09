package byow.Core;

import java.io.Serializable;

public class Portal implements Serializable {

    int x;
    int y;

    Portal link;

    public Portal(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setLink(Portal p) {
        this.link = p;
    }
}
