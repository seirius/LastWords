package com.lastwords.ashley.animation

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.lastwords.ashley.entities.EntityState
import com.lastwords.ashley.entities.EntityStateComponent
import com.lastwords.ashley.entities.MoveDirection
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
            textureComponent.textureRegion = animationComponent.currentAnimation
                    ?.getKeyFrame(animationComponent.animationTime)
            if (animationComponent?.currentAnimation!!.isAnimationFinished(animationComponent.animationTime)) {
                animationComponent.animationTime = 0f
            }
        }
    }


}

class AshleyEntityAnimationSystem : EntitySystem() {

    private lateinit var entities: ImmutableArray<Entity>

    private val entityStateMapper = ComponentMapper.getFor(EntityStateComponent::class.java)
    private val animationMapper = ComponentMapper.getFor(AnimationComponent::class.java)

    override fun addedToEngine(engine: Engine?) {
        entities = engine!!
                .getEntitiesFor(Family.all(EntityStateComponent::class.java, AnimationComponent::class.java).get())
    }

    override fun update(deltaTime: Float) {
        for (entity in entities) {
            val entityStateComponent = entityStateMapper.get(entity)
            val animationComponent = animationMapper.get(entity)
            animationComponent.currentAnimation = null
            if (entityStateComponent.state == EntityState.STILL) {
                animationComponent.currentAnimation = animationComponent.animationIdle
            } else if (entityStateComponent.state == EntityState.MOVING) {
                if (entityStateComponent.moveDirection == MoveDirection.DOWN ||
                        entityStateComponent.moveDirection == MoveDirection.RIGHT ||
                        entityStateComponent.moveDirection == MoveDirection.UP) {
                    animationComponent.currentAnimation = animationComponent.animationWalkRight
                } else if (entityStateComponent.moveDirection == MoveDirection.LEFT) {
                    animationComponent.currentAnimation = animationComponent.animationWalkLeft
                }
            }
        }
    }
}
