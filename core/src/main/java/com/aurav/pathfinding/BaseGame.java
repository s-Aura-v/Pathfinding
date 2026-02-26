package com.aurav.pathfinding;

import com.aurav.pathfinding.screens.BaseScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BaseGame extends Game {

    private static BaseGame game;
    public static boolean DEBUG = true;
    public static int VIEWPORT_WIDTH = 1000;
    public static int VIEWPORT_HEIGHT = 1000;

    public static Label.LabelStyle labelStyle;
    public static Label.LabelStyle levelLabelStyle;
    public static TextButton.TextButtonStyle textButtonStyle;
    public static TextField.TextFieldStyle textFieldStyle;

    public BaseGame() {
        game = this;
    }

    @Override
    public void create() {
        InputMultiplexer im = new InputMultiplexer();
        Gdx.input.setInputProcessor(im);
        initializeFonts();
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

    private static void initializeFonts() {
        labelStyle = new Label.LabelStyle();
        levelLabelStyle = new Label.LabelStyle();
        textButtonStyle = new TextButton.TextButtonStyle();

        labelStyle.font = new BitmapFont(); // default is 15px arial

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 48;
        fontParameter.color = Color.WHITE;
        fontParameter.borderWidth = 2;
        fontParameter.borderColor = Color.BLACK;
        fontParameter.borderStraight = true;
        fontParameter.minFilter = Texture.TextureFilter.Linear;
        fontParameter.magFilter = Texture.TextureFilter.Linear;

        BitmapFont customFont = fontGenerator.generateFont(fontParameter);
        labelStyle.font = customFont;

        fontParameter.size = 24;
        customFont = fontGenerator.generateFont(fontParameter);
        levelLabelStyle.font = customFont;

        Texture buttonTex = new Texture(Gdx.files.internal("ui/button.png"));
        NinePatch buttonPatch = new NinePatch(buttonTex, 24, 24, 24, 24);
        textButtonStyle.up = new NinePatchDrawable(buttonPatch);
        textButtonStyle.font = customFont;
        textButtonStyle.fontColor = Color.GRAY;

        textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = customFont;
        textFieldStyle.fontColor = Color.WHITE;

        fontGenerator.dispose();
    }

}
