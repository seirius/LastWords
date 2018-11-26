package com.lastwords.states;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lastwords.LastWords;
import com.lastwords.ashley.animation.AnimationSystem;
import com.lastwords.ashley.draw.DrawSystem;
import com.lastwords.ashley.entities.CastSystem;
import com.lastwords.ashley.velocity.InputToVelocity;
import com.lastwords.ashley.velocity.VelocitySystem;
import com.lastwords.ashley.world.WorldSystem;
import com.lastwords.entities.AshleyEntity;
import com.lastwords.entities.Player;

public class PlayState extends State {

    private Player player;
    private AshleyEntity ashleyEntity;
    private Engine engine;
    private WorldSystem worldSystem;

    public PlayState(GameStateManager gameStateManager) {
        super(gameStateManager);
        camera.setToOrtho(false, LastWords.WIDTH / LastWords.SCALE, LastWords.HEIGHT / LastWords.SCALE);
        worldSystem = new WorldSystem();
        this.engine = gameStateManager.getEngine();
        this.engine.addSystem(worldSystem);
        this.engine.addSystem(new InputToVelocity());
        this.engine.addSystem(new CastSystem());
        this.engine.addSystem(new DrawSystem(gameStateManager.getSpriteBatch()));
        this.engine.addSystem(new VelocitySystem());
        this.engine.addSystem(new AnimationSystem());
        ashleyEntity = new AshleyEntity(16, 16, 30);
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
        worldSystem.render(camera.combined);
    }

    @Override
    public void dispose() {
        player.dispose();
    }
}
