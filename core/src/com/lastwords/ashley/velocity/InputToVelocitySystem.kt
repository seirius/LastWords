package com.lastwords.ashley.velocity

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.lastwords.ashley.entities.EntityStateComponent
import com.lastwords.ashley.player.PlayerComponent
import com.lastwords.ashley.stats.StatsComponent

class InputToVelocitySystem : EntitySystem() {

    private var entities: ImmutableArray<Entity>? = null

    private val velocityMapper = ComponentMapper.getFor(VelocityComponent::class.java)
    private val statsMapper = ComponentMapper.getFor(StatsComponent::class.java)
    private val entityState = ComponentMapper.getFor(EntityStateComponent::class.java)

    override fun addedToEngine(engine: Engine?) {
        entities = engine!!.getEntitiesFor(Family
                .all(PlayerComponent::class.java, VelocityComponent::class.java,
                        StatsComponent::class.java, EntityStateComponent::class.java).get())
    }

    override fun update(deltaTime: Float) {
        for (entity in entities!!) {
            val velocityComponent = velocityMapper.get(entity)
            val statsComponent = statsMapper.get(entity)
            val entityStateComponent = entityState.get(entity)

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                entityStateComponent.castState = !entityStateComponent.castState
            }

            val total = Vector2.Zero.cpy()
            if (!entityStateComponent.castState) {
                val finalSpeed = statsComponent.speed * SPEED_MULTIPLIER

                if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                    total.add(RIGHT_V.cpy().scl(finalSpeed))
                }
                if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                    total.add(UP_V.cpy().scl(finalSpeed))
                }
                if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                    total.add(DOWN_V.cpy().scl(finalSpeed))
                }
                if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                    total.add(LEFT_V.cpy().scl(finalSpeed))
                }

            }
            velocityComponent.velocity = total

        }
    }

    companion object {

        private const val RAD_ANGLE_RIGHT = 0f
        private const val RAD_ANGLE_DOWN = MathUtils.degreesToRadians * 270f
        private const val RAD_ANGLE_LEFT = MathUtils.degreesToRadians * 180f
        private const val RAD_ANGLE_UP = MathUtils.degreesToRadians * 90f

        private val RIGHT_V = Vector2(MathUtils.cos(RAD_ANGLE_RIGHT), MathUtils.sin(RAD_ANGLE_RIGHT))
        private val DOWN_V = Vector2(MathUtils.cos(RAD_ANGLE_DOWN), MathUtils.sin(RAD_ANGLE_DOWN))
        private val UP_V = Vector2(MathUtils.cos(RAD_ANGLE_UP), MathUtils.sin(RAD_ANGLE_UP))
        private val LEFT_V = Vector2(MathUtils.cos(RAD_ANGLE_LEFT), MathUtils.sin(RAD_ANGLE_LEFT))

        public const val SPEED_MULTIPLIER = 100f
    }

}
