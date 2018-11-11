package com.lastwords.states;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lastwords.LastWords;
import com.lastwords.components.animation.AnimationSystem;
import com.lastwords.components.draw.DrawSystem;
import com.lastwords.components.inputs.UserMovementSystem;
import com.lastwords.components.velocity.VelocitySystem;
import com.lastwords.entities.AshleyEntity;
import com.lastwords.entities.Player;

public class PlayState extends State {

    private Player player;
    private AshleyEntity ashleyEntity;
    private Engine engine;

    public PlayState(GameStateManager gameStateManager) {
        super(gameStateManager);
        camera.setToOrtho(false, LastWords.WIDTH / 2f, LastWords.HEIGHT / 2f);
//        player = new Player(16, 16, 200);
        this.engine = gameStateManager.getEngine();
        this.engine.addSystem(new UserMovementSystem());
        this.engine.addSystem(new DrawSystem(gameStateManager.getSpriteBatch()));
        this.engine.addSystem(new VelocitySystem());
        this.engine.addSystem(new AnimationSystem());
        ashleyEntity = new AshleyEntity(16, 16, 200);
        this.engine.addEntity(ashleyEntity);
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float deltaTime) {
        this.engine.update(deltaTime);
//        player.update(dt);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
//        player.render(spriteBatch);
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        player.dispose();
    }
}
