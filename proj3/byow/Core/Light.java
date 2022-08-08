package byow.Core;

public class Light {
    int x;
    int y;
    boolean state;

    Map.Room room;

    public Light(int x, int y, boolean state, Map.Room room){
        this.x = x;
        this.y = y;
        this.state = state;
        this.room = room;
    }

    public void toggle(){
        this.state = !this.state;
    }
}
