package com.lastwords.ashley.velocity;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.lastwords.ashley.body.BodyComponent;
import com.lastwords.ashley.stats.StatsComponent;

public class VelocitySystem extends EntitySystem {

    private ImmutableArray<Entity> entities;

    private ComponentMapper<VelocityComponent> velocityMapper = ComponentMapper.getFor(VelocityComponent.class);
    private ComponentMapper<BodyComponent> bodyMapper = ComponentMapper.getFor(BodyComponent.class);

    public VelocitySystem() {}

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family
                .all(VelocityComponent.class, StatsComponent.class, BodyComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity: entities) {
            VelocityComponent velocityComponent = velocityMapper.get(entity);
            BodyComponent bodyComponent = bodyMapper.get(entity);
            Body body = bodyComponent.getBody();
            if (!velocityComponent.velocity.isZero()) {
                Vector2 finalVelocity = velocityComponent.velocity.cpy().scl(deltaTime);
                body.setLinearVelocity(finalVelocity);
            } else {
                body.setLinearVelocity(Vector2.Zero);
            }
        }
    }
}
