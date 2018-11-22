package com.lastwords.components.playerinput;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.lastwords.components.stats.StatsComponent;
import com.lastwords.components.velocity.VelocityComponent;

public class PlayerInputSystem extends EntitySystem {

    public static final float RAD_ANGLE_RIGHT = 0;
    public static final float RAD_ANGLE_DOWN = MathUtils.degreesToRadians * 270f;
    public static final float RAD_ANGLE_LEFT = MathUtils.degreesToRadians * 180f;
    public static final float RAD_ANGLE_UP = MathUtils.degreesToRadians * 90f;

    public static final Vector2 RIGHT_V = new Vector2(MathUtils.cos(RAD_ANGLE_RIGHT), MathUtils.sin(RAD_ANGLE_RIGHT));
    public static final Vector2 DOWN_V = new Vector2(MathUtils.cos(RAD_ANGLE_DOWN), MathUtils.sin(RAD_ANGLE_DOWN));
    public static final Vector2 UP_V = new Vector2(MathUtils.cos(RAD_ANGLE_UP), MathUtils.sin(RAD_ANGLE_UP));
    public static final Vector2 LEFT_V = new Vector2(MathUtils.cos(RAD_ANGLE_LEFT), MathUtils.sin(RAD_ANGLE_LEFT));

    private ImmutableArray<Entity> entities;

    private ComponentMapper<VelocityComponent> velocityMapper = ComponentMapper.getFor(VelocityComponent.class);
    private ComponentMapper<StatsComponent> statsMapper = ComponentMapper.getFor(StatsComponent.class);

    public PlayerInputSystem() {}

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family
                .all(PlayerComponent.class, VelocityComponent.class, StatsComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity: entities) {
            VelocityComponent velocityComponent = velocityMapper.get(entity);
            StatsComponent statsComponent = statsMapper.get(entity);

            Vector2 total = Vector2.Zero.cpy();

            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                total.add(RIGHT_V.cpy().scl(statsComponent.speed));
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                total.add(UP_V.cpy().scl(statsComponent.speed));
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                total.add(DOWN_V.cpy().scl(statsComponent.speed));
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                total.add(LEFT_V.cpy().scl(statsComponent.speed));
            }

            velocityComponent.velocity = total;
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println(MathUtils.sin(RAD_ANGLE_RIGHT));
            System.out.println(RAD_ANGLE_RIGHT);
            System.out.println(RIGHT_V);
            System.out.println(MathUtils.atan2(0, 1));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
