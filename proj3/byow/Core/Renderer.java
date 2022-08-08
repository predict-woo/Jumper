package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Renderer {
    Player player;
    Map map;
    TETile[][] world;

    int WIDTH;
    int HEIGHT;
    Random gen;

    ArrayList<Light> lightArrayList = new ArrayList<>();


    public Renderer(TETile[][] world,int WIDTH, int HEIGHT,Random gen){
        this.map = new Map(WIDTH,HEIGHT,gen);
        this.map.genDungeon();
        this.world = world;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.gen = gen;
//        System.out.println(this.map.world);
    }

    public void initialize(){
        TETile light6 = new TETile('·',Color.WHITE, new Color(43, 39, 15), "1");
        TETile light5 = new TETile('·',Color.WHITE, new Color(77, 70, 27), "1");
        TETile light4 = new TETile('·',Color.WHITE, new Color(125, 115, 45), "1");
        TETile light3 = new TETile('·',Color.WHITE, new Color(163, 150, 59), "1");
        TETile light2 = new TETile('·',Color.WHITE, new Color(204, 188, 73), "1");
        TETile light1 = new TETile('·',Color.WHITE, new Color(255, 236, 94), "1");
        TETile lightSourceOn = new TETile('○',Color.WHITE, new Color(255, 238, 138), "1");
        TETile lightSourceOff = new TETile('◌',Color.WHITE, new Color(43, 39, 15), "1");

        TETile[] lightArray = new TETile[]{light1,light2,light3,light4,light5,light6};



        for (Map.Room r : this.map.roomList){
            if (gen.nextInt(2) == 1){
                Light l = new Light(gen.nextInt(r.x2 - r.x1 - 1) + r.x1 + 1,
                        gen.nextInt(r.y2 - r.y1 - 1) + r.y1 + 1, true,r);
                lightArrayList.add(l);
            }

        }

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if(this.map.world[i][j] == 0){
                    this.world[i][j] = Tileset.FLOOR;
                } else if (this.map.world[i][j] == 1) {
                    this.world[i][j] = Tileset.WALL;
                } else if (this.map.world[i][j] == -1) {
                    this.world[i][j] = Tileset.NOTHING;
                }
            }
        }

        for (Light l : lightArrayList){
            if(l.state){
                this.world[l.x][l.y] = lightSourceOn;
                for (int i = l.room.x1; i <= l.room.x2; i++) {
                    for (int j = l.room.y1; j <=l.room.y2 ; j++) {
                        if(i == l.room.x1 || i == l.room.x2 || j == l.room.y1 || j == l.room.y2){
                            continue;
                        } else {
                            int dist = Math.abs(i-l.x) + Math.abs(j-l.y) - 1;
                            if(dist < 6 && dist >= 0){
                                this.world[i][j] = lightArray[dist];
                            }
                        }
                    }
                }
            } else {
                this.world[l.x][l.y] = lightSourceOff;
            }
        }
    }

    public void render(){
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if(this.map.world[i][j] == 0){
                    this.world[i][j] = Tileset.FLOOR;
                } else if (this.map.world[i][j] == 1) {
                    this.world[i][j] = Tileset.WALL;
                } else if (this.map.world[i][j] == -1) {
                    this.world[i][j] = Tileset.NOTHING;
                }
            }
        }
    }

}
