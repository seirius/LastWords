package com.lastwords.ashley.entities;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class CastSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;

    private ComponentMapper<CastComponent> castMapper
            = ComponentMapper.getFor(CastComponent.class);
    private ComponentMapper<EntityStateComponent> entityStateMapper
            = ComponentMapper.getFor(EntityStateComponent.class);

    public CastSystem() {}

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine
                .getEntitiesFor(Family.all(CastComponent.class, EntityStateComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {

        for (Entity entity: entities) {
            EntityStateComponent entityStateComponent = entityStateMapper.get(entity);
            if (entityStateComponent.castState) {
                CastComponent castComponent = castMapper.get(entity);

                if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
                    castComponent.castPile.add(Input.Keys.Q);
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
                    castComponent.castPile.add(Input.Keys.W);
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                    castComponent.castPile.add(Input.Keys.E);
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                    castComponent.castPile.add(Input.Keys.R);
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                    castComponent.castPile.add(Input.Keys.A);
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                    castComponent.castPile.add(Input.Keys.S);
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                    castComponent.castPile.add(Input.Keys.D);
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
                    castComponent.castPile.add(Input.Keys.F);
                }

                if (Gdx.input.justTouched() && castComponent.castPile.size() > 0) {
                    StringBuilder casted = new StringBuilder();
                    for (Integer key: castComponent.castPile) {
                        casted.append(key).append(":");
                    }
                    System.out.println(casted);
                    castComponent.castPile.clear();
                }
            }
        }

    }

}
