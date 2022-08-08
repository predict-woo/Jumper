package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Renderer {
    Player player;
    Map map;
    TETile[][] world;

    int WIDTH;
    int HEIGHT;
    Random gen;

    ArrayList<Light> lightArrayList = new ArrayList<>();
    ArrayList<Portal> portalArrayList = new ArrayList<>();

    TETile lightSourceOn = new TETile('○',Color.WHITE, new Color(255, 238, 138), "1");
    TETile lightSourceOff = new TETile('◌',Color.WHITE, new Color(43, 39, 15), "1");

    TETile[] unseenLightArray = new TETile[10];
    TETile[] seenLightArray = new TETile[10];

    int[] startColor = new int[]{255,212,63};
    int[] endColor1 = new int[]{0,0,0};
    int[] endColor2 = new int[]{159,159,159};


    public Renderer(TETile[][] world,int WIDTH, int HEIGHT,Random gen){
        this.gen = gen;
        this.map = new Map(WIDTH,HEIGHT,gen);
        this.map.genDungeon();
        this.world = world;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        for (int i = 0; i < 10; i++) {
            unseenLightArray[i] = new TETile(' ',Color.WHITE,
                    new Color((endColor1[0] * i + startColor[0] * (9-i))/9,
                            (endColor1[1] * i + startColor[1] * (9-i))/9,
                            (endColor1[2] * i + startColor[2] * (9-i))/9),"color");
            seenLightArray[i] = new TETile(' ',Color.WHITE,
                    new Color((endColor2[0] * i + startColor[0] * (9-i))/9,
                            (endColor2[1] * i + startColor[1] * (9-i))/9,
                            (endColor2[2] * i + startColor[2] * (9-i))/9),"color");
        }
//        System.out.println(this.map.world);
    }

    public void pickUpLight(){
        for (int i = 0; i < lightArrayList.size(); i++) {
            Light l = lightArrayList.get(i);
            if(l.x == player.x && l.y == player.y){
                lightArrayList.remove(i);
                player.lightRadius += 5;
            }
        }
    }

    public void enterPortal(){
        for (int i = 0; i < portalArrayList.size(); i++) {
            Portal p = portalArrayList.get(i);
            if(p.x == player.x && p.y == player.y && p.link != null){
                if(player.lightRadius > 5){
                    player.lightRadius -= 5;
                    player.x = p.link.x;
                    player.y = p.link.y;

                    break;
                } else {
                    return;
                }
            }
        }
    }

    public void toggleLight(){
        for(Light l : lightArrayList){
            if(l.x == player.x && l.y == player.y){
                l.toggle();
            }
        }
    }

    public void initialize(){

        for (Map.Room r : this.map.roomList){
            if (gen.nextInt(2) == 1){
                Light l = new Light(gen.nextInt(r.x2 - r.x1 - 1) + r.x1 + 1,
                        gen.nextInt(r.y2 - r.y1 - 1) + r.y1 + 1, true,r);
                lightArrayList.add(l);
            } else {
                Portal p = new Portal(gen.nextInt(r.x2 - r.x1 - 1) + r.x1 + 1,
                        gen.nextInt(r.y2 - r.y1 - 1) + r.y1 + 1);
                portalArrayList.add(p);
            }

        }

        Collections.shuffle(portalArrayList,gen);

        for (int i = 0; i < portalArrayList.size()/2; i++) {
            portalArrayList.get(i*2).setLink(portalArrayList.get(i*2+1));
            portalArrayList.get(i*2 + 1).setLink(portalArrayList.get(i*2));
        }

        Map.Room r = this.map.roomList.get(gen.nextInt(this.map.roomList.size()));

        this.player = new Player(gen.nextInt(r.x2 - r.x1 - 1) + r.x1 + 1,
                gen.nextInt(r.y2 - r.y1 - 1) + r.y1 + 1,world);

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
                            if(dist < 10 && dist >= 0){
                                this.world[i][j] = seenLightArray[dist];
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

        for (Light l : lightArrayList){
            if(l.state){
                this.world[l.x][l.y] = lightSourceOn;
                for (int i = l.room.x1; i <= l.room.x2; i++) {
                    for (int j = l.room.y1; j <=l.room.y2 ; j++) {
                        if(i == l.room.x1 || i == l.room.x2 || j == l.room.y1 || j == l.room.y2){
                            continue;
                        } else {
                            int dist = Math.abs(i-l.x) + Math.abs(j-l.y) - 1;
                            if(dist < 10 && dist >= 0){
                                this.world[i][j] = seenLightArray[dist];
                            }
                        }
                    }
                }
            } else {
                this.world[l.x][l.y] = lightSourceOff;
            }
        }

        for (Portal p : portalArrayList){
            if(p.link != null){
                this.world[p.x][p.y] = Tileset.PORTAL;
            }
        }

        this.world[this.player.x][this.player.y] = Tileset.AVATAR;
    }

    public void renderV(){
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                int dist = Math.abs(i-player.x) + Math.abs(j-player.y);
                if(dist < player.lightRadius){
                    if(this.map.world[i][j] == 0){
                        this.world[i][j] = Tileset.FLOOR;
                    } else if (this.map.world[i][j] == 1) {
                        this.world[i][j] = Tileset.WALL;
                    } else if (this.map.world[i][j] == -1) {
                        this.world[i][j] = Tileset.NOTHING;
                    }
                } else {
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
                            int dist2 = Math.abs(i-player.x) + Math.abs(j-player.y);
                            if(dist < 10 && dist >= 0 && dist2 < player.lightRadius){
                                this.world[i][j] = seenLightArray[dist];
                            } else if (dist < 10 && dist >= 0 && dist2 >= player.lightRadius) {
                                this.world[i][j] = unseenLightArray[dist];
                            }
                        }
                    }
                }
            } else {
                this.world[l.x][l.y] = lightSourceOff;
            }
        }
        for (Portal p : portalArrayList){
            if(p.link != null && Math.abs(p.x-player.x) + Math.abs(p.y-player.y) < player.lightRadius){
                this.world[p.x][p.y] = Tileset.PORTAL;
            }
        }

        this.world[this.player.x][this.player.y] = Tileset.AVATAR;

    }

}
