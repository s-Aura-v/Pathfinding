package com.aurav.pathfinding.screens;

import com.aurav.pathfinding.utility.Config;
import com.aurav.pathfinding.utility.FileInput;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class MazeScreen extends BaseScreen {
    Map map = new Map();
    Texture tileTexture = new Texture("maps/tile.png");
    int scaleX;
    int scaleY;
    int[][] tiles;

    // Camera
    private Vector3 prevCamPos = new Vector3();
    OrthographicCamera camera;

    @Override
    public void initialize() {
        tiles = FileInput.readInput("assets/inputs/input1");

        camera = getCamera();
        System.out.println(camera.position);
        scaleX = (Config.WORLD_WIDTH / tiles.length);
        scaleY = (Config.WORLD_HEIGHT) / tiles[0].length;
    }

    @Override
    public void update(float dt) {
    }

    /**
     * Just store the camera's position when dragged.
     * Used to calculate camera translation
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        prevCamPos.set(screenX, screenY, 0);
        return true;
    }

    /**
     * Calculate the camera translation using previous position and new position.
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // Previous Position - Change in Position = new Position
        float x = (prevCamPos.x - screenX);
        float y = (screenY - prevCamPos.y);

        camera.translate(x, y);
        camera.update();
        bindCamera();

        prevCamPos.set(screenX, screenY, 0);
        return true;
    }


    @Override
    public void render(float dt) {
        super.render(dt);
        float tileSize = 24f;

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (int x = 0; x <= tiles[0].length; x++) {
            for (int y = 0; y <= tiles.length; y++) {
                batch.draw(tileTexture, x * tileSize, y * tileSize, tileSize, tileSize);
            }
        }
        batch.end();
    }


    private void bindCamera() {
        camera.position.x = MathUtils.clamp(camera.position.x,
            0,
            tiles[0].length + camera.viewportWidth);
        camera.position.y = MathUtils.clamp(camera.position.y,
            0,
            tiles.length + camera.viewportHeight);
    }
}
