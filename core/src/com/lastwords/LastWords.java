package com.lastwords;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lastwords.world.GameMap;
import com.lastwords.world.TiledGameMap;

public class LastWords extends ApplicationAdapter {
    public static final String TITLE = "LastWords";
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 720;

    SpriteBatch batch;
    Texture img;

    private OrthographicCamera camera;

    private GameMap gameMap;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture(Gdx.files.internal("badlogic.jpg"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        gameMap = new TiledGameMap();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isTouched()) {
            camera.translate(-Gdx.input.getDeltaX(), Gdx.input.getDeltaY());
            camera.update();
        }

        gameMap.render(camera);
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }

}
