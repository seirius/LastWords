package com.lastwords.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lastwords.LastWords;
import com.lastwords.entities.Player;

public class PlayState extends State {

    private Player player;

    public PlayState(GameStateManager gameStateManager) {
        super(gameStateManager);
        camera.setToOrtho(false, LastWords.WIDTH / 2f, LastWords.HEIGHT / 2f);
        player = new Player(16, 16, 200);
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        handleInput();
        player.update(dt);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        player.render(spriteBatch);
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        player.dispose();
    }
}
