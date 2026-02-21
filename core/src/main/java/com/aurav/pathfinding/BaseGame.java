package com.aurav.pathfinding;

import com.aurav.pathfinding.screens.BaseScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BaseGame extends Game {

    private static BaseGame game;
    public static int VIEWPORT_WIDTH = 1000;
    public static int VIEWPORT_HEIGHT = 1000;

    public BaseGame() {
        game = this;
    }

    @Override
    public void create() {
    }

    @Override
    public void render() {
        super.render(); // important
    }

    @Override
    public void dispose() {
    }

    public static void setActiveScreen(BaseScreen s) {
        game.setScreen(s);
    }


}
