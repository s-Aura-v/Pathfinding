package com.aurav.pathfinding.screens;

import com.aurav.pathfinding.utility.Config;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class BaseScreen implements Screen, InputProcessor {
    Viewport levelViewport;
    private OrthographicCamera camera;
    public SpriteBatch batch;

    protected Stage uiStage;
    protected Table uiTable;

    public BaseScreen() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        levelViewport = new FitViewport(Config.WORLD_WIDTH, Config.WORLD_HEIGHT, camera);

        uiStage = new Stage(levelViewport, batch);
        uiTable = new Table();
        uiStage.addActor(uiTable);
        uiTable.setTouchable(uiStage.getRoot().getTouchable());

        uiTable.setFillParent(true);
        uiTable.center();

        initialize();
    }

    public abstract void initialize();

    public abstract void update(float dt);

    public OrthographicCamera getCamera() {
        return camera;
    }

    @Override
    public void show() {
        InputMultiplexer im = (InputMultiplexer) Gdx.input.getInputProcessor();
        im.addProcessor(this);
        im.addProcessor(uiStage);
    }

    @Override
    public void hide() {
        InputMultiplexer im = (InputMultiplexer) Gdx.input.getInputProcessor();
        im.removeProcessor(this);
        im.removeProcessor(uiStage);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        uiStage.act(dt);
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        levelViewport.update(width, height);
        camera.update();
        levelViewport.apply();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }
}
