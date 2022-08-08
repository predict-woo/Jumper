package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.util.*;


public class Engine {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 60;
    TERenderer ter = new TERenderer();
    TETile[][] world = new TETile[WIDTH][HEIGHT];
    Random gen = new Random();
    Renderer ren = new Renderer(world,WIDTH,HEIGHT,gen);

    boolean playing = true;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */

    public void interactWithKeyboard() {
        boolean isBackground = false;
        ter.initialize(WIDTH, HEIGHT);
        while(playing){
            String seed = "";
            if(StdDraw.hasNextKeyTyped()){
                char a = StdDraw.nextKeyTyped();
                if (a == 'n' || a == 'N'){
                    ter.seedPage("");
                    while(playing){
                        if (StdDraw.hasNextKeyTyped()){
                            char b = StdDraw.nextKeyTyped();
                            if(b == 's' || b == 'S'){
                                break;
                            } else {
                                seed += b;
                                ter.seedPage(seed);
                            }
                        }
                    }
                    System.out.println(seed);
                    gen.setSeed(Long.parseLong(seed));
                    ren.initialize();
                    ter.renderFrame(world);

                    while(playing){
                        if (StdDraw.hasNextKeyTyped()){
                            char c = StdDraw.nextKeyTyped();
                            System.out.println(c);
                            if (c == 'w'){
                                ren.player.mover(0,1);
                            }
                            if (c == 'a'){
                                ren.player.mover(-1,0);
                            }
                            if (c == 's'){
                                ren.player.mover(0,-1);
                            }
                            if (c == 'd'){
                                ren.player.mover(1,0);
                            }
                            if (c == 'e'){
                                ren.pickUpLight();
                            }
                            if (c == 'f'){
                                ren.toggleLight();
                            }
                            if (c == 'b'){
                                isBackground = !isBackground;
                            }
                            if(isBackground){
                                ren.render();
                            } else {
                                ren.renderV();
                            }
                            ter.renderFrame(world);
                        }
                    }
                }
            }
        }
    }




    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        String seed = "";

        boolean isSeed = false;

        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == 'N' || input.charAt(i) == 'n') {
                isSeed = true;
                continue;
            }
            if (input.charAt(i) == 'S' || input.charAt(i) == 's') {
                break;
            }
            if (isSeed) {
                seed += input.charAt(i);
            }
        }

        gen.setSeed(Long.parseLong(seed));

        ren.initialize();

        return world;
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @return the 2D TETile[][] representing the state of the world
     */

}
