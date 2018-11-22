package com.lastwords.components.velocity;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.lastwords.components.position.PositionComponent;
import com.lastwords.components.stats.StatsComponent;

public class VelocitySystem extends EntitySystem {

    private ImmutableArray<Entity> entities;

    private ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<VelocityComponent> velocityMapper = ComponentMapper.getFor(VelocityComponent.class);

    public VelocitySystem() {}

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family
                .all(PositionComponent.class, VelocityComponent.class, StatsComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity: entities) {
            PositionComponent positionComponent = positionMapper.get(entity);
            VelocityComponent velocityComponent = velocityMapper.get(entity);
            if (positionComponent.position != null
                && velocityComponent.velocity != null
                && !velocityComponent.velocity.isZero()) {
                positionComponent.position.add(velocityComponent.velocity.cpy().scl(deltaTime));
            }
        }
    }
}
