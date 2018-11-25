package com.lastwords.ashley.animation;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.lastwords.ashley.texture.TextureComponent;

public class AnimationSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;

    private ComponentMapper<TextureComponent> textureMapper
            = ComponentMapper.getFor(TextureComponent.class);
    private ComponentMapper<AnimationComponent> animationMapper
            = ComponentMapper.getFor(AnimationComponent.class);

    public AnimationSystem() {}

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine
                .getEntitiesFor(Family.all(TextureComponent.class, AnimationComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity: entities) {
            TextureComponent textureComponent = textureMapper.get(entity);
            AnimationComponent animationComponent = animationMapper.get(entity);
            animationComponent.animationTime += deltaTime;
            textureComponent.textureRegion = animationComponent.animation
                    .getKeyFrame(animationComponent.animationTime);
            if (animationComponent.animation.isAnimationFinished(animationComponent.animationTime)) {
                animationComponent.animationTime = 0f;
            }
        }
    }


}
