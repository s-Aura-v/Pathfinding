package com.aurav.pathfinding.screens;

import com.aurav.pathfinding.BaseGame;
import com.aurav.pathfinding.entities.Tile;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import static com.aurav.pathfinding.utility.FileInput.readInput;

public class MazeScreen extends BaseScreen {
    Map map = new Map();
    Array<Tile> tilesRender;
    Camera cam;

    int start;
    int scale;
    int destination;
    int layover;

    public MazeScreen() {
        tilesRender = new Array<>();
        cam = getCamera();
        for (short i = 0; i < 100; i++) {
            for (short j = 0; j < 100; j++) {
                tilesRender.add(new Tile(i, j, (short) 1));
            }
        }
        scale = 1000 / 100;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void update(float dt) {

    }


    @Override
    public boolean touchDragged(int xPos, int yPos, int pointer) {
        System.out.println("dragged detected");
        System.out.println("X: " + cam.position.x + ", Y: " + cam.position.y + ", " + pointer);
        cam.lookAt(cam.position.x + xPos, cam.position.y + yPos, pointer);

        return true;
    }

    void createList() {
//        tilesList = new int[][];
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        cam.update();
        batch.begin();
        for (Tile t : tilesRender) {
            int posX = t.getPosX() * scale;
            int posY = t.getPosY() * scale;
            batch.draw(t.getTexture(), posX, posY);
        }
        batch.end();
    }
}
