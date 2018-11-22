package com.lastwords.components.world;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class WorldSystem extends EntitySystem {

    private World world;
    private Box2DDebugRenderer renderer;

    public WorldSystem() {
        this.world = new World(Vector2.Zero, true);
    }

    @Override
    public void addedToEngine(Engine engine) {
    }

    @Override
    public void update(float deltaTime) {
        world.step(deltaTime, 6, 2);
    }

    public void render(Matrix4 combined) {
        renderer.render(world, combined);
    }

    public World getWorld() {
        return world;
    }
}
