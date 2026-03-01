package com.aurav.pathfinding.screens;

import com.aurav.pathfinding.BaseGame;
import com.aurav.pathfinding.utility.Config;
import com.aurav.pathfinding.utility.FileInput;
import com.aurav.pathfinding.utility.SearchAlgorithm;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;

import java.util.Arrays;

public class MazeScreen extends BaseScreen {
    Texture tileTexture = new Texture("maps/tile.png");
    int scaleX;
    int scaleY;
    int[][] tiles;

    // Camera
    private Vector3 prevCamPos = new Vector3();
    OrthographicCamera camera;

    // Grids
    boolean start = true;
    int[] source;
    int[] destination;

    @Override
    public void initialize() {
        tiles = FileInput.readInput("assets/inputs/input3");

        source = new int[2];
        destination = new int[2];

        camera = getCamera();
        scaleX = (Config.WORLD_WIDTH / tiles.length);
        scaleY = (Config.WORLD_HEIGHT) / tiles[0].length;

        initUI();

    }

    /**
     * Creates the UI for selection interface.
     */
    Label sourceUI;
    Label destUI;
    Slider speedSlider;
    TextButton searchButton;

    private void initUI() {
        sourceUI = new Label("Initial", BaseGame.levelLabelStyle);
        destUI = new Label("Initial", BaseGame.levelLabelStyle);
        Slider.SliderStyle style = new Slider.SliderStyle();
        speedSlider = new Slider(0, 100, 20, false, style);
        searchButton = new TextButton("Search", BaseGame.textButtonStyle);
        Label sourceText = new Label("Source: ", BaseGame.levelLabelStyle);
        Label destinationText = new Label("Destination: ", BaseGame.levelLabelStyle);

        uiTable.top().left().padTop(20).padLeft(10);
        uiTable.add(sourceText, sourceUI);
        uiTable.row();
        uiTable.add(destinationText, destUI);
        uiTable.row();
        uiTable.add(speedSlider);
        uiTable.row();
        uiTable.add(searchButton).colspan(2);

        searchButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                System.out.println("Searching from: " + Arrays.toString(source) + ", to: " + Arrays.toString(destination));
                SearchAlgorithm.search(tiles, source[0], source[1], destination[0], destination[1]);
            }
        });
    }

    private void updateUI() {
        sourceUI.setText("( " + source[0] + " ," + source[1] + " )");
        destUI.setText("( " + destination[0] + " ," + destination[1] + " )");
    }

    private void teleportCamera() {

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
        // Camera
        prevCamPos.set(screenX, screenY, 0);

        // Grid
        Vector3 worldPos = new Vector3(screenX, screenY, 0);
        camera.unproject(worldPos);
        handleGrid((int) (worldPos.x / tileSize), (int) (worldPos.y / tileSize));
        return true;
    }

    /**
     * Given the array index, [x][y], handle pathfinding inputs.
     */
    private void handleGrid(int x, int y) {
        if (start) {
            source[0] = x;
            source[1] = y;
            start = false;
        } else {
            destination[0] = x;
            destination[1] = y;
            start = true;
        }
        updateUI();
    }

    /**
     * Calculate the camera translation using previous position and new position.
     *
     * @param screenX the new x position
     * @param screenY the new y position
     * @param pointer lol idk
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

    float tileSize = 24;

    @Override
    public void render(float dt) {
        super.render(dt);

        int bottomLeftX = (int) Math.max(0, ((camera.position.x - (camera.viewportWidth)) / tileSize));
        int bottomLeftY = (int) Math.max(0, ((camera.position.y - (camera.viewportHeight)) / tileSize));
        int topRightX = (int) Math.min(tiles[0].length - 1, ((camera.position.x + (camera.viewportWidth)) / tileSize));
        int topRightY = (int) Math.min(tiles.length - 1, ((camera.position.y + (camera.viewportHeight)) / tileSize));

        // Game Render
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (int x = bottomLeftX; x <= topRightX; x++) {
            for (int y = bottomLeftY; y <= topRightY; y++) {
                if (x == source[0] && y == source[1]) {
                    batch.setColor(Color.GREEN);
                } else if (x == destination[0] && y == destination[1]) {
                    batch.setColor(Color.RED);
                } else {
                    batch.setColor(Color.WHITE);
                }

                int weight = tiles[x][y];
                switch (weight) {
                    case 10:
                        batch.setColor(Color.BLACK);
                    case 11:
                        batch.setColor(Color.CYAN);
                }

                batch.draw(tileTexture, x * tileSize, y * tileSize, tileSize, tileSize);
            }
        }
        batch.end();

        // UI Render
        uiStage.act(dt);
        uiStage.draw();
    }

    private void bindCamera() {
        camera.position.x = MathUtils.clamp(camera.position.x,
            0,
            tiles[0].length * tileSize + (camera.viewportWidth / 2));
        camera.position.y = MathUtils.clamp(camera.position.y,
            0,
            tiles.length * tileSize + (camera.viewportHeight / 2));
    }
}
