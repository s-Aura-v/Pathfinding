package com.aurav.pathfinding.screens;

import com.aurav.pathfinding.BaseGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class StartScreen extends BaseScreen {

    @Override
    public void initialize() {
        Label title = new Label("Pathfinder", BaseGame.labelStyle);
        Label subtitle = new Label("(not the dining hall or the bank)", BaseGame.levelLabelStyle);
        TextButton startButton = new TextButton("Start", BaseGame.textButtonStyle);
        TextButton quitButton = new TextButton("Quit", BaseGame.textButtonStyle);

        uiTable.add(title).colspan(2);
        uiTable.row();
        uiTable.add(subtitle).colspan(2).spaceBottom(60);
        uiTable.row();
        uiTable.add(startButton).colspan(2);
        uiTable.row();
        uiTable.add(quitButton).colspan(2);

        // Listeners; (in reference to IM)
        startButton.addListener(e -> {
            if (!(e instanceof InputEvent) || !(((InputEvent) e).getType().equals(InputEvent.Type.touchDown))) {
                return false;
            }
            BaseGame.setActiveScreen(new MazeScreen());
            return false;
        });

        quitButton.addListener(e -> {
            if (!(e instanceof InputEvent) || !(((InputEvent) e).getType().equals(InputEvent.Type.touchDown))) {
                return false;
            }
            Gdx.app.exit();
            return false;
        });
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(float dt) {
        super.render(dt);
    }
}
