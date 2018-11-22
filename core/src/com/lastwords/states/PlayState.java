package com.lastwords.states;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.lastwords.LastWords;
import com.lastwords.components.animation.AnimationSystem;
import com.lastwords.components.draw.DrawSystem;
import com.lastwords.components.playerinput.PlayerInputSystem;
import com.lastwords.components.velocity.VelocitySystem;
import com.lastwords.components.world.WorldSystem;
import com.lastwords.entities.AshleyEntity;
import com.lastwords.entities.Player;

public class PlayState extends State {

    private Player player;
    private AshleyEntity ashleyEntity;
    private Engine engine;
    private WorldSystem worldSystem;

    public PlayState(GameStateManager gameStateManager) {
        super(gameStateManager);
        camera.setToOrtho(false, LastWords.WIDTH / 2f, LastWords.HEIGHT / 2f);
//        player = new Player(16, 16, 200);
        world = new World(Vector2.Zero, true);

//        BodyDef bodyDef = new BodyDef();
//        bodyDef.type = BodyDef.BodyType.DynamicBody;
//        bodyDef.position.set(new Vector2(30, 30));
//        Body body = world.createBody(bodyDef);
//        body.setLinearVelocity(new Vector2(3, 3));
//
//        CircleShape circleShape = new CircleShape();
//        circleShape.setRadius(3);
//
//        FixtureDef fixtureDef = new FixtureDef();
//        fixtureDef.shape = circleShape;
//        fixtureDef.density = 1;
//
//        Fixture fixture = body.createFixture(fixtureDef);
        worldSystem = new WorldSystem();
        this.engine = gameStateManager.getEngine();
        this.engine.addSystem(worldSystem);
        this.engine.addSystem(new PlayerInputSystem());
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
        worldSystem.render(camera.combined);
    }

    @Override
    public void dispose() {
        player.dispose();
    }
}
