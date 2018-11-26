package com.lastwords.ashley.velocity;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.lastwords.ashley.entities.EntityStateComponent;
import com.lastwords.ashley.playerinput.PlayerComponent;
import com.lastwords.ashley.stats.StatsComponent;

public class InputToVelocity extends EntitySystem {

    private static final float RAD_ANGLE_RIGHT = 0;
    private static final float RAD_ANGLE_DOWN = MathUtils.degreesToRadians * 270f;
    private static final float RAD_ANGLE_LEFT = MathUtils.degreesToRadians * 180f;
    private static final float RAD_ANGLE_UP = MathUtils.degreesToRadians * 90f;

    private static final Vector2 RIGHT_V = new Vector2(MathUtils.cos(RAD_ANGLE_RIGHT), MathUtils.sin(RAD_ANGLE_RIGHT));
    private static final Vector2 DOWN_V = new Vector2(MathUtils.cos(RAD_ANGLE_DOWN), MathUtils.sin(RAD_ANGLE_DOWN));
    private static final Vector2 UP_V = new Vector2(MathUtils.cos(RAD_ANGLE_UP), MathUtils.sin(RAD_ANGLE_UP));
    private static final Vector2 LEFT_V = new Vector2(MathUtils.cos(RAD_ANGLE_LEFT), MathUtils.sin(RAD_ANGLE_LEFT));

    private static final float SPEED_MULTIPLIER = 100;

    private ImmutableArray<Entity> entities;

    private ComponentMapper<VelocityComponent> velocityMapper = ComponentMapper.getFor(VelocityComponent.class);
    private ComponentMapper<StatsComponent> statsMapper = ComponentMapper.getFor(StatsComponent.class);
    private ComponentMapper<EntityStateComponent> entityState = ComponentMapper.getFor(EntityStateComponent.class);

    public InputToVelocity() {}

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family
                .all(PlayerComponent.class, VelocityComponent.class,
                        StatsComponent.class, EntityStateComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity: entities) {
            VelocityComponent velocityComponent = velocityMapper.get(entity);
            StatsComponent statsComponent = statsMapper.get(entity);
            EntityStateComponent entityStateComponent = entityState.get(entity);

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                entityStateComponent.castState = !entityStateComponent.castState;
            }

            Vector2 total = Vector2.Zero.cpy();
            if (!entityStateComponent.castState) {
                float finalSpeed = statsComponent.speed * SPEED_MULTIPLIER;

                if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                    total.add(RIGHT_V.cpy().scl(finalSpeed));
                }
                if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                    total.add(UP_V.cpy().scl(finalSpeed));
                }
                if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                    total.add(DOWN_V.cpy().scl(finalSpeed));
                }
                if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                    total.add(LEFT_V.cpy().scl(finalSpeed));
                }

            }
            velocityComponent.velocity = total;

        }
    }

}
