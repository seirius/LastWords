package com.lastwords;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lastwords.states.GameStateManager;
import com.lastwords.states.MenuState;

public class LastWords extends ApplicationAdapter {


    public static final String TITLE = "LastWords";
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 720;
    public static final float SCALE = 1.8f;


    private SpriteBatch spriteBatch;
    private GameStateManager gameStateManager;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        gameStateManager = new GameStateManager(spriteBatch);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        gameStateManager.push(new MenuState(gameStateManager));
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameStateManager.update(Gdx.graphics.getDeltaTime());
        gameStateManager.renrer(spriteBatch);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void resize(int width, int height) {
        gameStateManager.resize(width, height);
    }
}
