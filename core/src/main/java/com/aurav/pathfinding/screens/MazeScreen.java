package com.aurav.pathfinding.screens;

import com.aurav.pathfinding.BaseGame;
import com.aurav.pathfinding.entities.Node;
import com.aurav.pathfinding.utility.FileInput;
import com.aurav.pathfinding.utility.SearchAlgorithm;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.Arrays;

public class MazeScreen extends BaseScreen {
    Texture tileTexture = new Texture("maps/tile.png");
    int[][] tiles;

    // Camera
    private Vector3 prevCamPos = new Vector3();
    OrthographicCamera camera;

    // Grids
    boolean start = true;
    int[] source;
    int[] destination;

    public Node node;
    public boolean[][] path;

    @Override
    public void initialize() {
        FileInput fi = new FileInput();
        tiles = fi.readInput("assets/inputs/hw2input3");

        source = new int[2];
        destination = new int[2];

        camera = getCamera();
        initUI();
    }

    /**
     * Creates the UI for selection interface.
     */
    Label sourceUI;
    Label destUI;
    Slider speedSlider;
    TextButton searchButton;
    TextButton resetButton;
    CheckBox selectNodeBox;
    Label costLabel;

    private void initUI() {
        sourceUI = new Label("Initial", BaseGame.skin);
        destUI = new Label("Initial", BaseGame.skin);
        speedSlider = new Slider(1, 10, 1, false, BaseGame.skin);
        searchButton = new TextButton("Search", BaseGame.skin);
        resetButton = new TextButton("Reset", BaseGame.skin);
        Label sourceText = new Label("Source: ", BaseGame.skin);
        Label destinationText = new Label("Destination: ", BaseGame.skin);
        selectNodeBox = new CheckBox("Select node?", BaseGame.skin);
        selectNodeBox.setChecked(true);
        costLabel = new Label("", BaseGame.skin);

        uiTable.top().left().padTop(20).padLeft(10);
        uiTable.add(sourceText, sourceUI);
        uiTable.row();
        uiTable.add(destinationText, destUI);
        uiTable.row();
        uiTable.add(selectNodeBox);
        uiTable.row();
        uiTable.add(new Label("Camera Speed :", BaseGame.skin), speedSlider);
        uiTable.row();
        uiTable.add(searchButton, resetButton);
        uiTable.row().row();
        uiTable.add(new Label("Cost to Dest: ", BaseGame.skin), costLabel);

        searchButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                System.out.println("Searching from: " + Arrays.toString(source) + ", to: " + Arrays.toString(destination));
                node = SearchAlgorithm.search(tiles, source[0], source[1], destination[0], destination[1]);
                path = SearchAlgorithm.getPath(node);
                if (node != null) costLabel.setText(node.gCost);
                else costLabel.setText("Undefined Path");
            }
        });

        resetButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                node = null;
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
        if (selectNodeBox.isChecked()) {
            Vector3 worldPos = new Vector3(screenX, screenY, 0);
            camera.unproject(worldPos);
            handleGrid((int) (worldPos.x / tileSize), (int) (worldPos.y / tileSize));
        }
        return true;
    }

    /**
     * Given the array index, [x][y], handle pathfinding inputs.
     */
    private void handleGrid(int x, int y) {
        try {
            if (tiles[x][y] == 10 || tiles[x][y] == 11) return;
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }

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

        camera.translate(x * speedSlider.getValue(), y * speedSlider.getValue());
        camera.update();
//        bindCamera();

        prevCamPos.set(screenX, screenY, 0);
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        camera.zoom += amountY * 0.1f;
        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 5f);
        camera.update();
        return true;
    }

    float tileSize = 24;

    @Override
    public void render(float dt) {
        super.render(dt);

        int bottomLeftX = (int) Math.max(0, ((camera.position.x - (camera.viewportWidth * camera.zoom)) / tileSize));
        int bottomLeftY = (int) Math.max(0, ((camera.position.y - (camera.viewportHeight * camera.zoom)) / tileSize));
        int topRightX = (int) Math.min(tiles.length - 1, ((camera.position.x + (camera.viewportWidth)) / tileSize));
        int topRightY = (int) Math.min(tiles[0].length - 1, ((camera.position.y + (camera.viewportHeight)) / tileSize));

        // Game Render
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (int x = bottomLeftX; x <= topRightX; x++) {
            for (int y = bottomLeftY; y <= topRightY; y++) {
                if (x == source[0] && y == source[1]) {
                    batch.setColor(Color.GREEN);
                } else {
                    batch.setColor(Color.WHITE);
                }

                int weight = tiles[x][y];
                if (weight == 10) {
                    batch.setColor(Color.GRAY);
                } else if (weight > 100) {
                    batch.setColor(Color.CYAN);
                }

                if (node != null) {
                    if (path[x][y]) {
                        batch.setColor(Color.YELLOW);
                    }
                }

                if (x == destination[0] && y == destination[1]) {
                    batch.setColor(Color.RED);
                }

                batch.draw(tileTexture, x * tileSize, y * tileSize, tileSize, tileSize);
            }
        }
        batch.end();

        // UI Render
        uiStage.act(dt);
        uiStage.draw();
    }

    private void cameraControls() {

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
