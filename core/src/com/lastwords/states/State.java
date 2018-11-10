package com.lastwords.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class State {

    protected OrthographicCamera camera;
    protected Vector2 mousePosition;
    protected GameStateManager gameStateManager;

    public State(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
        camera = new OrthographicCamera();
        mousePosition = new Vector2();
    }

    protected abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render(SpriteBatch spriteBatch);
    public abstract void dispose();

}
