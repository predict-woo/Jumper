package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;

import java.io.*;
import java.util.Random;


public class Engine implements Serializable {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    TERenderer ter = new TERenderer();
    TETile[][] world = new TETile[WIDTH][HEIGHT];
    Random gen = new Random();

    Renderer ren;

    boolean playing = true;

    int totalMoves = 150;


    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */

    public void interactWithKeyboard() {
        boolean isBackground = false;
        boolean isAboutToQuit = false;
        ter.initialize(WIDTH, HEIGHT + 4, 0, 1);
        while (playing) {
            String seed = "";
            if (StdDraw.hasNextKeyTyped()) {
                char a = StdDraw.nextKeyTyped();
                if (a == 'l' || a == 'L') {
                    load();
                }
                if (a == 'n' || a == 'N') {
                    ter.seedPage("");
                    while (playing) {
                        if (StdDraw.hasNextKeyTyped()) {
                            char b = StdDraw.nextKeyTyped();
                            System.out.println(b);
                            if (b == 's' || b == 'S') {
                                break;
                            } else {
                                seed += b;
                                ter.seedPage(seed);
                            }
                        }
                    }
                    System.out.println(seed);
                    gen.setSeed(Long.parseLong(seed));
                    ren = new Renderer(world, WIDTH, HEIGHT, gen);
                    ren.initialize();
                }

                ren.renderV();
                ter.render(world, totalMoves - this.ren.player.moves, this.ren.player.lightRadius);

                while (playing) {
                    if (StdDraw.hasNextKeyTyped()) {
                        char c = StdDraw.nextKeyTyped();
                        System.out.println(c);
                        if (c == 'w') {
                            ren.player.mover(0, 1);
                        }
                        if (c == 'a') {
                            ren.player.mover(-1, 0);
                        }
                        if (c == 's') {
                            ren.player.mover(0, -1);
                        }
                        if (c == 'd') {
                            ren.player.mover(1, 0);
                        }
                        if (c == 'e') {
                            ren.pickUpLight();
                            ren.enterPortal();
                        }
                        if (c == 'f') {
                            ren.toggleLight();
                        }
                        if (c == 'b') {
                            isBackground = !isBackground;
                        }
                        if (c == ':') {
                            isAboutToQuit = true;
                            System.out.println("about to quit");
                        } else if (isAboutToQuit && (c == 'q' || c == 'Q')) {
                            System.out.println("quitting");
                            quit();
                        } else if (isAboutToQuit && !(c == 'q' || c == 'Q')) {
                            isAboutToQuit = false;
                        }
                        if (isBackground) {
                            ren.render();
                        } else {
                            ren.renderV();
                        }

                        if (totalMoves - this.ren.player.moves > 0) {
                            ter.render(world, totalMoves - this.ren.player.moves,
                                    this.ren.player.lightRadius);
                        } else {
                            playing = false;
                        }
                    }
                }
            }
        }
        ter.gameOver();
    }

    public void quit() {
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream("saves");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(fout);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            oos.writeObject(this.ren);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.exit(-1);
    }

    public void load() {
        Renderer loadedRen = null;
        try (FileInputStream in = new FileInputStream("saves");
             ObjectInputStream s = new ObjectInputStream(in)) {
            loadedRen = (Renderer) s.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.ren = loadedRen;
        this.world = loadedRen.world;
    }


    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        boolean isBackground = true;
        boolean isAboutToQuit = false;
        while (playing && input.length() > 0) {
            String seed = "";
            char a = input.charAt(0);
            input = input.substring(1);
            if (a == 'l' || a == 'L') {
                load();
            }
            if (a == 'n' || a == 'N') {
                while (playing && input.length() > 0) {
                    char b = input.charAt(0);
                    input = input.substring(1);
                    if (b == 's' || b == 'S') {
                        break;
                    } else {
                        seed += b;
                    }
                }
                System.out.println(seed);
                gen.setSeed(Long.parseLong(seed));
                ren = new Renderer(world, WIDTH, HEIGHT, gen);
                ren.initialize();
            }

            ren.render();

            while (playing && input.length() > 0) {
                char c = input.charAt(0);
                input = input.substring(1);
                if (c == 'w') {
                    ren.player.mover(0, 1);
                }
                if (c == 'a') {
                    ren.player.mover(-1, 0);
                }
                if (c == 's') {
                    ren.player.mover(0, -1);
                }
                if (c == 'd') {
                    ren.player.mover(1, 0);
                }
                if (c == 'e') {
                    ren.pickUpLight();
                    ren.enterPortal();
                }
                if (c == 'f') {
                    ren.toggleLight();
                }
                if (c == 'b') {
                    isBackground = !isBackground;
                }
                if (c == ':') {
                    isAboutToQuit = true;
                    System.out.println("about to quit");
                } else if (isAboutToQuit && (c == 'q' || c == 'Q')) {
                    System.out.println("quitting");
                    quit();
                    return this.world;
                } else if (isAboutToQuit && !(c == 'q' || c == 'Q')) {
                    isAboutToQuit = false;
                }
                if (isBackground) {
                    ren.render();
                } else {
                    ren.renderV();
                }
            }
        }
        return this.world;
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
