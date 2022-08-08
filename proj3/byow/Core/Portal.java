package byow.Core;

public class Portal {

    int x;
    int y;

    Portal link;

    public Portal(int x,int y){
        this.x = x;
        this.y = y;
    }

    public void setLink(Portal p){
        this.link = p;
    }
}
