package com.lastwords.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lastwords.LastWords;

public class MenuState extends State {

    public MenuState(GameStateManager gameStateManager) {
        super(gameStateManager);
        camera.setToOrtho(false, LastWords.WIDTH / 2f, LastWords.HEIGHT / 2f);
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch spriteBatch) {

    }

    @Override
    public void dispose() {

    }
}
