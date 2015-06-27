package com.dukescript.fifteen.fifteenpuzzle;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.html.BrwsrCtx;
import net.java.html.boot.BrowserBuilder;
import net.java.html.json.Models;

public final class Main {

    private Main() {
    }

    public static void main(String... args) throws Exception {
        BrowserBuilder.newBrowser().
                loadPage("pages/index.html").
                loadClass(Main.class).
                invoke("onPageLoad", args).
                showAndWait();
        System.exit(0);
    }

    /**
     * Called when the page is ready.
     */
    public static void onPageLoad() {
        Game game = initGame();
        String test = StorageManager.getStorage().get("fifteen");
        if (test != null && !test.isEmpty() && !test.equals("undefined")) {
            Logger.getLogger(Main.class.getName()).log(Level.INFO, "Test:" + test + "<");
            InputStream inputStream = new ByteArrayInputStream(test.getBytes());
            try {
                game.clone();
                game = Models.parse(BrwsrCtx.findDefault(Game.class), Game.class, inputStream);
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                game = initGame();
            }

        }
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
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                final Integer pos = positions.pop();
                final Tile tile = new Tile(i, j, pos);
                tiles.add(tile);
            }
        }
        return new Game(0, 0, false, tiles.toArray(new Tile[16]));
    }

}
