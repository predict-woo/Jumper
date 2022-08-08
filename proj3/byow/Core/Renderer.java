package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import java.util.Random;

public class Renderer {
    Player player;
    Map map;
    TETile[][] world;

    int WIDTH;
    int HEIGHT;
    Random gen;


    public Renderer(Player player, Map map, TETile[][] world){
        this.player = player;
        this.map = map;
        this.world = world;
    }

}
