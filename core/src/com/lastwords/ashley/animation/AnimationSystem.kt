package com.lastwords.ashley.animation

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.lastwords.ashley.texture.TextureComponent

class AnimationSystem : EntitySystem() {

    private var entities: ImmutableArray<Entity>? = null

    private val textureMapper = ComponentMapper.getFor(TextureComponent::class.java)
    private val animationMapper = ComponentMapper.getFor(AnimationComponent::class.java)

    override fun addedToEngine(engine: Engine?) {
        entities = engine!!
                .getEntitiesFor(Family.all(TextureComponent::class.java, AnimationComponent::class.java).get())
    }

    override fun update(deltaTime: Float) {
        for (entity in entities!!) {
            val textureComponent = textureMapper.get(entity)
            val animationComponent = animationMapper.get(entity)
            animationComponent.animationTime += deltaTime
            textureComponent.textureRegion = animationComponent.animation
                    .getKeyFrame(animationComponent.animationTime)
            if (animationComponent.animation.isAnimationFinished(animationComponent.animationTime)) {
                animationComponent.animationTime = 0f
            }
        }
    }


}
