package byow.Core;
import java.util.*;

public class Map {

    int WIDTH;
    int HEIGHT;
    Random gen;

    int[][] world;

    ArrayList<Room> roomList = new ArrayList<>();
    ArrayList<BST> bstList = new ArrayList<>();
    ArrayList<Corr> corrList = new ArrayList<>();

    public Map(int WIDTH,int HEIGHT,Random gen){
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.gen = gen;
        this.world = new int[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                this.world[i][j] = -1;
            }
        }
    }



    private void helper1(BST b, ArrayList<Room> roomArrayList, ArrayList<BST> bstArrayList) {
        if (b.isDividable()) {
            b.genChildren();
            bstArrayList.add(b);
            for (BST bc : b.children) {
                helper1(bc, roomArrayList, bstArrayList);
            }
        } else {
            b.genRoom();
            roomArrayList.add(b.room);
            bstArrayList.add(b);
        }
    }

    private void helper2(BST b, ArrayList<Corr> corrArrayList) {
        Queue<BST> bstQueue = new LinkedList<>();
        Stack<BST> bstStack = new Stack<>();
        bstQueue.add(b);
        while (!bstQueue.isEmpty()) {
            BST curr = bstQueue.poll();
            if (curr.children != null) {
                bstStack.push(curr);
                bstQueue.add(curr.children[0]);
                bstQueue.add(curr.children[1]);
            }
        }
        while (!bstStack.isEmpty()) {
            corrArrayList.add(bstStack.pop().genCorr());
        }
    }

    public void genDungeon() {
        BST root = new BST(0, WIDTH - 1, 0, HEIGHT - 1, 0);
        helper1(root, roomList, bstList);
        for (Room r : roomList) {
            Room.drawRoom(r, world);
        }
        helper2(root, corrList);
        for (Corr c : corrList) {
            c.drawCorr(world);
        }
    }
    public class Room {
        int x1;
        int x2;
        int y1;
        int y2;

        Room(int x1, int x2, int y1, int y2) {
            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
            this.y2 = y2;
        }

        public static void drawRoom(Room room, int[][] intTiles) {
            for (int i = room.x1; i <= room.x2; i++) {
                for (int j = room.y1; j <=room.y2 ; j++) {
                    if(i == room.x1 || i == room.x2 || j == room.y1 || j == room.y2){
                        intTiles[i][j] = 1;
                    } else {
                        intTiles[i][j] = 0;
                    }
                }
            }
        }
    }

    private class Corr {
        int dir;
        int x;
        int y;

        Corr(int dir, int x, int y) {
            this.dir = dir;
            this.x = x;
            this.y = y;
        }

        private void dir0(int xCorr, int yCorr, int[][] world) {
            while (!(world[xCorr][yCorr] ==1)) {
                yCorr--;
            }
            if (world[xCorr][yCorr - 1] == 1
                    && world[xCorr + 1][yCorr] == 1) {
                world[xCorr - 1][yCorr - 1] = 1;
                world[xCorr - 1][yCorr - 2] = 1;
                world[xCorr][yCorr - 1] = 0;
            }
            if (world[xCorr][yCorr - 1] == 1
                    && world[xCorr - 1][yCorr] == 1) {
                world[xCorr + 1][yCorr - 1] = 1;
                world[xCorr + 1][yCorr - 2] = 1;
                world[xCorr][yCorr - 1] = 0;
            }
            world[xCorr][yCorr] = 0;
            while (!(world[xCorr][yCorr] ==1)) {
                world[xCorr][yCorr] = 0;
                world[xCorr + 1][yCorr] = 1;
                world[xCorr - 1][yCorr] = 1;
                yCorr++;
            }
            if (world[xCorr][yCorr + 1] == 1
                    && world[xCorr + 1][yCorr] == 1) {
                world[xCorr - 1][yCorr] = 1;
                world[xCorr - 1][yCorr + 1] = 1;
                world[xCorr - 1][yCorr + 2] = 1;
                world[xCorr][yCorr + 1] = 0;
            }
            if (world[xCorr][yCorr + 1] == 1
                    && world[xCorr - 1][yCorr] == 1) {
                world[xCorr + 1][yCorr] = 1;
                world[xCorr + 1][yCorr + 1] = 1;
                world[xCorr + 1][yCorr + 2] = 1;
                world[xCorr][yCorr + 1] = 0;
            }
            world[xCorr][yCorr] = 0;
        }

        private void dir1(int xCorr, int yCorr, int[][] world) {
            while (!(world[xCorr][yCorr] == 1)) {
                xCorr--;
            }
            if (world[xCorr - 1][yCorr] == 1
                    && world[xCorr][yCorr + 1] == 1) {
                world[xCorr - 1][yCorr - 1] = 1;
                world[xCorr - 2][yCorr - 1] = 1;
                world[xCorr - 1][yCorr] = 0;
            }
            if (world[xCorr - 1][yCorr] == 1
                    && world[xCorr][yCorr - 1] == 1) {
                world[xCorr - 1][yCorr + 1] = 1;
                world[xCorr - 2][yCorr + 1] = 1;
                world[xCorr - 1][yCorr] = 0;
            }
            world[xCorr][yCorr] = 0;
            while (!(world[xCorr][yCorr] == 1)) {
                world[xCorr][yCorr] = 0;
                world[xCorr][yCorr + 1] = 1;
                world[xCorr][yCorr - 1] = 1;
                xCorr++;
            }
            if (world[xCorr + 1][yCorr] == 1
                    && world[xCorr][yCorr + 1] == 1) {
                world[xCorr][yCorr - 1] = 1;
                world[xCorr + 1][yCorr - 1] = 1;
                world[xCorr + 2][yCorr - 1] = 1;
                world[xCorr + 1][yCorr] = 0;
            }
            if (world[xCorr + 1][yCorr] == 1
                    && world[xCorr][yCorr - 1] == 1) {
                world[xCorr][yCorr + 1] = 1;
                world[xCorr + 1][yCorr + 1] = 1;
                world[xCorr + 2][yCorr + 1] = 1;
                world[xCorr + 1][yCorr] = 0;
            }
            world[xCorr][yCorr] = 0;
        }

        public void drawCorr(int[][] world) {
            int xCorr = this.x;
            int yCorr = this.y;
            if (dir == 0) {
                dir0(xCorr, yCorr, world);
            } else if (dir == 1) {
                dir1(xCorr, yCorr, world);
            }
        }
    }

    private class BST {

        int x1;
        int x2;
        int y1;
        int y2;

        int height;
        int width;

        int level;

        BST[] children; // children
        Room room; // room for leaf

        int dir = gen.nextInt(2);
        // the direction children are split

        BST(int x1, int x2, int y1, int y2, int level) {
            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
            this.y2 = y2;
            this.height = y2 - y1;
            this.width = x2 - x1;
            this.level = level;
        }

        public void genRoom() {
            int rH = gen.nextInt(height / 3) + height / 2;
            int rW = gen.nextInt(width / 3) + width / 2;
            int y = gen.nextInt(height - rH);
            int x = gen.nextInt(width - rW);
            this.room = new Room(x1 + x, x1 + x + rW, y1 + y, y1 + y + rH);
        }

        public boolean isDividable() {
            return (this.width > 15 && this.height > 15);
        }

        public void genChildren() {
            BST[] res = new BST[2];

            if (width > height * 1.7) {
                dir = 1;
            }
            if (height > width * 1.7) {
                dir = 0;
            }

            if (dir == 0) {
                int divider = gen.nextInt(height / 2) + height / 4;
                res[0] = new BST(x1, x2, y1, y1 + divider, level + 1);
                res[1] = new BST(x1, x2, y1 + divider + 1, y2, level + 1);
            }
            if (dir == 1) {
                int divider = gen.nextInt(width / 2) + width / 4;
                res[0] = new BST(x1, x1 + divider, y1, y2, level + 1);
                res[1] = new BST(x1 + divider + 1, x2, y1, y2, level + 1);
            }
            children = res;
        }

        private void rangeX(BST b, int[] range) {
            if (b.room != null) {
                if (b.room.x1 < range[0] || range[0] == -1) {
                    range[0] = b.room.x1;
                }
                if (b.room.x2 > range[1] || range[0] == -1) {
                    range[1] = b.room.x2;
                }
            } else {
                rangeX(b.children[0], range);
                rangeX(b.children[1], range);
            }
        }

        private void rangeY(BST b, int[] range) {
            if (b.room != null) {
                if (b.room.y1 < range[0] || range[0] == -1) {
                    range[0] = b.room.y1;
                }
                if (b.room.y2 > range[1] || range[0] == -1) {
                    range[1] = b.room.y2;
                }
            } else {
                rangeY(b.children[0], range);
                rangeY(b.children[1], range);
            }
        }

        public Corr genCorr() {
            if (dir == 0) {
                int finX = (x1 + x2) / 2;
                return new Corr(dir, finX, children[0].y2);
            }
            if (dir == 1) {
                int[] possibleY = new int[]{-1, -1};
                int finY = (y1 + y2) / 2;
                return new Corr(dir, children[0].x2, finY);
            }
            return null;
        }
    }
}
