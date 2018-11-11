package com.lastwords.components.draw;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lastwords.components.position.PositionComponent;
import com.lastwords.components.texture.TextureComponent;

public class DrawSystem extends EntitySystem {

    private SpriteBatch spriteBatch;

    private ImmutableArray<Entity> entities;

    private ComponentMapper<PositionComponent> positionMapper
            = ComponentMapper.getFor(PositionComponent.class);

    private ComponentMapper<TextureComponent> textureMapper
            = ComponentMapper.getFor(TextureComponent.class);

    public DrawSystem(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine
                .getEntitiesFor(Family.all(PositionComponent.class, TextureComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        float textureWidth, textureHeight, xTexture, yTexture;
        for (Entity entity: entities) {
            PositionComponent positionComponent = positionMapper.get(entity);
            TextureComponent textureComponent = textureMapper.get(entity);
            if (positionComponent.position != null
                && textureComponent.textureRegion != null) {
                textureWidth = textureComponent.textureRegion.getRegionWidth();
                textureHeight = textureComponent.textureRegion.getRegionHeight();
                xTexture = positionComponent.position.x - textureWidth / 2f;
                yTexture = positionComponent.position.y - textureHeight / 2f;
                spriteBatch
                        .draw(textureComponent.textureRegion, xTexture, yTexture, textureWidth, textureHeight);
            }
        }
    }
}
