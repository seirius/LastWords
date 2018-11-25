package com.lastwords.ashley.world;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.lastwords.ashley.body.BodyComponent;
import com.lastwords.ashley.position.PositionComponent;

public class WorldSystem extends EntitySystem {

    private World world;
    private Box2DDebugRenderer renderer;

    private ImmutableArray<Entity> entitiesToAdd;
    private ImmutableArray<Entity> entitiesWithPosition;

    private ComponentMapper<BodyComponent> bodyMapper = ComponentMapper.getFor(BodyComponent.class);
    private ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);

    public WorldSystem() {
        this.world = new World(Vector2.Zero, true);
        this.renderer = new Box2DDebugRenderer();
    }

    @Override
    public void addedToEngine(Engine engine) {
        entitiesToAdd = engine.getEntitiesFor(Family
        .all(BodyComponent.class, AddToWorldComponent.class).get());

        entitiesWithPosition = engine.getEntitiesFor(Family
        .all(BodyComponent.class, PositionComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {

        for (Entity entity: entitiesToAdd) {
            BodyComponent bodyComponent = bodyMapper.get(entity);
            bodyComponent.initBody(world);
            entity.remove(AddToWorldComponent.class);
        }

        for (Entity entity: entitiesWithPosition) {
            BodyComponent bodyComponent = bodyMapper.get(entity);
            PositionComponent positionComponent = positionMapper.get(entity);
            Body body = bodyComponent.getBody();
            if (!body.getPosition().equals(positionComponent.position)) {
                positionComponent.position = body.getPosition();
            }
        }

        world.step(deltaTime, 6, 2);
    }

    public void render(Matrix4 combined) {
        renderer.render(world, combined);
    }

    public World getWorld() {
        return world;
    }
}
