package com.aurav.pathfinding.screens;

import com.aurav.pathfinding.BaseGame;
import com.aurav.pathfinding.entities.Tile;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.utils.Array;

public class MazeScreen extends BaseScreen {
    Map map = new Map();
    Array<Tile> tiles;

    int[][] tilesList;

    int start;
    int scale;
    int destination;
    int layover;

    // go through the teleporters,
    // find a straightforward path
    //

    public MazeScreen() {
        tiles = new Array<>();
        for (short i = 0; i < 100; i++) {
            for (short j = 0; j < 100; j++) {
                tiles.add(new Tile(i, j, (short) 1));
            }
        }
        scale = 1000/100;
        System.out.println("Created list");
    }

    void createList() {
//        tilesList = new int[][];
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        batch.begin();
        for (Tile t : tiles) {
            int posX = t.getPosX() * scale;
            int posY = t.getPosY() * scale;
            batch.draw(t.getTexture(), posX, posY);
        }
        batch.end();
    }
}
