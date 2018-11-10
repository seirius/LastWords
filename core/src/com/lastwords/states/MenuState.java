package com.lastwords.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lastwords.LastWords;

public class MenuState extends State {

    private Texture background1;
    private Texture background2;
    private Texture background3;
    private Texture background4;
    private Texture background5;
    private Texture background6;

    public MenuState(GameStateManager gameStateManager) {
        super(gameStateManager);
        background1 = new Texture("hill/PNG/background1.png");
        background2 = new Texture("hill/PNG/background2.png");
        background3 = new Texture("hill/PNG/background3.png");
        background4 = new Texture("hill/PNG/background4.png");
        background5 = new Texture("hill/PNG/background5.png");
        background6 = new Texture("hill/PNG/background6.png");
        camera.setToOrtho(false, background1.getWidth(), background1.getHeight());
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            gameStateManager.set(new PlayState(gameStateManager));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(background1, 0, 0);
        spriteBatch.draw(background2, 0, 0);
        spriteBatch.draw(background3, 0, 0);
        spriteBatch.draw(background4, 0, 0);
        spriteBatch.draw(background5, 0, 0);
        spriteBatch.draw(background6, 0, 0);
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        background1.dispose();
        System.out.println("MenuState disposed");
    }
}
