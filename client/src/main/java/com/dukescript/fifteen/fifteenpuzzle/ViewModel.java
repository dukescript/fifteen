package com.dukescript.fifteen.fifteenpuzzle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.html.json.ComputedProperty;
import net.java.html.json.Function;
import net.java.html.json.Model;
import net.java.html.json.Models;
import net.java.html.json.Property;

@Model(className = "Game", properties = {
    @Property(name = "tiles", type = Tile.class, array = true),
    @Property(name = "moves", type = int.class),
    @Property(name = "best", type = int.class),
    @Property(name = "solved", type = boolean.class),})
public class ViewModel {
    private static Game game;
    private static Tile zero = new Tile(0,0,0);
    private static Tile one = new Tile(0,1,0);
    private static Tile two = new Tile(0,2,0);
    private static Tile three = new Tile(0,3,0);
    private static Tile four = new Tile(1,0,0);
    private static Tile five = new Tile(1,1,0);
    private static Tile six = new Tile(1,2,0);
    private static Tile seven = new Tile(1,3,0);
    private static Tile eight = new Tile(2,0,0);
    private static Tile nine = new Tile(2,1,0);
    private static Tile ten = new Tile(2,2,0);
    private static Tile eleven = new Tile(2,3,0);
    private static Tile twelve = new Tile(3,0,0);
    private static Tile thirteen = new Tile(3,1,0);
    private static Tile fourteen = new Tile(3,2,0);
    private static Tile fifteen = new Tile(3,3,0);
    
    
     /**
     * Called when the page is ready.
     */
    public static void onPageLoad() {
        game = initGame();
//        String test = StorageManager.getStorage().get("fifteen");
//        if (test != null && !test.isEmpty() && !test.equals("undefined")) {
//            Logger.getLogger(Main.class.getName()).log(Level.INFO, "Test:" + test + "<");
//            InputStream inputStream = new ByteArrayInputStream(test.getBytes());
//            try {
//                game.clone();
//                game = Models.parse(BrwsrCtx.findDefault(Game.class), Game.class, inputStream);
//            } catch (IOException ex) {
//                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//                game = initGame();
//            }
//
//        }
        Models.applyBindings(game, "game");
    }

    private static Game initGame() {
        Logger.getLogger(Main.class.getName()).log(Level.INFO, "init Game!");

        LinkedList<Integer> positions = new LinkedList<>();
        for (int i = 0; i < 16; i++) {
            positions.add(i);
        }
        Collections.shuffle(positions);
        while (!ViewModel.isSolveable(positions)) {
            Collections.shuffle(positions);
        }

        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(zero);
        tiles.add(one);
        tiles.add(two);
        tiles.add(three);
        tiles.add(four);
        tiles.add(five);
        tiles.add(six);
        tiles.add(seven);
        tiles.add(eight);
        tiles.add(nine);
        tiles.add(ten);
        tiles.add(eleven);
        tiles.add(twelve);
        tiles.add(thirteen);
        tiles.add(fourteen);
        tiles.add(fifteen);
        zero.setP(positions.pop());
        one.setP(positions.pop());
        two.setP(positions.pop());
        three.setP(positions.pop());
        four.setP(positions.pop());
        five.setP(positions.pop());
        six.setP(positions.pop());
        seven.setP(positions.pop());
        eight.setP(positions.pop());
        nine.setP(positions.pop());
        ten.setP(positions.pop());
        eleven.setP(positions.pop());
        twelve.setP(positions.pop());
        thirteen.setP(positions.pop());
        fourteen.setP(positions.pop());
        fifteen.setP(positions.pop());
//        for (int i = 0; i < 4; i++) {
//            for (int j = 0; j < 4; j++) {
//                final Integer pos = positions.pop();
//                final Tile tile = new Tile(i, j, pos);
//                tiles.add(tile);
//            }
//        }
        return new Game(0, 0, false, tiles.toArray(new Tile[16]));
    }
    
    
    @Function
    public static void move(Game game, Tile data) {
        System.out.println("game "+game);
        if (data.getP() == 0 || game.isSolved()) {
            return;
        }
        int x = data.getX();
        int y = data.getY();
        int x1 = game.getEmpty().getX();
        int y1 = game.getEmpty().getY();
        if ((Math.abs(x1 - x) == 1 && Math.abs(y1 - y) == 0)
                || (Math.abs(x1 - x) == 0 && Math.abs(y1 - y) == 1)) {
            data.setX(x1);
            data.setY(y1);
            game.getEmpty().setX(x);
            game.getEmpty().setY(y);
            game.setMoves(game.getMoves() + 1);
            if (finished(game.getTiles())) {
                if (game.getBest() == 0) {
                    game.setBest(game.getMoves());
                } else if (game.getMoves() < game.getBest()) {
                    game.setBest(game.getMoves());
                }
                game.setSolved(true);
            }
            StorageManager.getStorage().put("fifteen", game.toString());
        }
    }

    @ComputedProperty
    public static Tile empty(List<Tile> tiles) {
        for (Tile tile : tiles) {
            if (tile.getP() == 0) {
                return tile;
            }
        }
        throw new IllegalArgumentException("This list doesn't contain an empty Tile");
    }



    @Function
    public static void shuffle(Game game) {
        LinkedList<Integer> positions = new LinkedList<>();
        for (int i = 0; i < 16; i++) {
            positions.add(i);
        }
        Collections.shuffle(positions);
        while (!ViewModel.isSolveable(positions)) {
            Collections.shuffle(positions);
        }
        List<Tile> tiles = game.getTiles();
        for (Tile tile : tiles) {
            for (int i = 0; i < positions.size(); i++) {
                final Integer pos = positions.get(i);
                if (tile.getP() == pos) {
                    tile.setX(i % 4);
                    tile.setY(i / 4);
                }
            }
        }
        game.setMoves(0);
        game.setSolved(false);
        StorageManager.getStorage().put("game", game.toString());
    }

  
    @Model(className = "Tile", properties = {
        @Property(name = "x", type = int.class),
        @Property(name = "y", type = int.class),
        @Property(name = "p", type = int.class)
    })
    final static class TileModel {

        @ComputedProperty()
        public static int currentPosition(int x, int y) {
            return (y * 4) + x;
        }

    }

    public static boolean finished(List<Tile> tiles) {
        for (Tile tile : tiles) {
            if (tile.getP() != 0 && (tile.getP() - 1) != tile.getCurrentPosition()) {
                return false;
            }
        }
        return true;
    }

    public static boolean isSolveable(List<Integer> tiles) {
        int n1 = 0;
        int n2 = 0;
        int empty = 15;
        for (int i = 0; i < tiles.size(); i++) {
            // a ist der Wert an Position i
            Integer a = tiles.get(i);
            if (a != 0) {
                for (int j = i; j < tiles.size(); j++) {
                    Integer b = tiles.get(j);
                    if (b != 0 && a > b) {
                        n1 += 1;
                    }

                }
            } else {
                empty = i;
            }

        }
        n2 = 1 + empty / 4;
        return (n1 + n2) % 2 == 0;
    }
}
