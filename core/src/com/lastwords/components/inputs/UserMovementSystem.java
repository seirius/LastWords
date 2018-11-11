package com.lastwords.components.inputs;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.lastwords.components.position.PositionComponent;
import com.lastwords.components.velocity.VelocityComponent;

public class UserMovementSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;

    private ComponentMapper<PositionComponent> positionMapper
            = ComponentMapper.getFor(PositionComponent.class);

    private ComponentMapper<VelocityComponent> velocityMapper
            = ComponentMapper.getFor(VelocityComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine
                .getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for(Entity entity: entities) {
            PositionComponent positionComponent = positionMapper.get(entity);
            VelocityComponent velocityComponent = velocityMapper.get(entity);
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                positionComponent.position
                        .add(new Vector2(-1, 0).scl(velocityComponent.velocity).scl(deltaTime));
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                positionComponent.position
                        .add(new Vector2(0, 1).scl(velocityComponent.velocity).scl(deltaTime));
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                positionComponent.position
                        .add(new Vector2(1, 0).scl(velocityComponent.velocity).scl(deltaTime));
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                positionComponent.position
                        .add(new Vector2(0, -1).scl(velocityComponent.velocity).scl(deltaTime));
            }
        }
    }
}
