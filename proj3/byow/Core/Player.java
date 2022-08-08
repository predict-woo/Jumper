package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Player {
    int x;
    int y;

    int lightRadius;

    TETile[][] world;

    int moves;

    public Player(int x, int y, TETile[][] world){
        this.x = x;
        this.y = y;
        this.lightRadius = 5;
        this.moves = 0;
        this.world = world;
    }

    public void mover(int dx, int dy){
        if (world[x+dx][y].equals(Tileset.WALL)){

        }
    }
}
