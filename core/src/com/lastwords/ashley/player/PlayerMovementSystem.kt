package com.lastwords.ashley.player

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.lastwords.ashley.entities.EntityMovementState
import com.lastwords.ashley.entities.EntityStateComponent
import com.lastwords.ashley.stats.StatsComponent
import com.lastwords.ashley.velocity.VelocityComponent
import com.lastwords.ashley.velocity.VelocitySystem

class PlayerMovementSystem: EntitySystem() {

    private lateinit var players: ImmutableArray<Entity>

    override fun addedToEngine(engine: Engine?) {
        players = engine!!.getEntitiesFor(Family.all(
                PlayerComponent::class.java, EntityStateComponent::class.java,
                VelocityComponent::class.java, StatsComponent::class.java,
                PlayerMovementComponent::class.java
        ).get())
    }

    override fun update(deltaTime: Float) {
        for (entity in players) {
            updateToMove(entity)
        }
    }

    private fun updateToMove(entity: Entity) {
        val velocityComponent = VelocityComponent.MAPPER.get(entity)
        val statsComponent = StatsComponent.MAPPER.get(entity)
        val stateComponent = EntityStateComponent.MAPPER.get(entity)

        val total = Vector2.Zero.cpy()

        if (stateComponent.movementState == EntityMovementState.CAN_MOVE) {
            val finalSpeed = statsComponent.speed * VelocitySystem.SPEED_MULTIPLIER

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

    companion object {

        private const val RAD_ANGLE_RIGHT = 0f
        private const val RAD_ANGLE_DOWN = MathUtils.degreesToRadians * 270f
        private const val RAD_ANGLE_LEFT = MathUtils.degreesToRadians * 180f
        private const val RAD_ANGLE_UP = MathUtils.degreesToRadians * 90f

        private val RIGHT_V = Vector2(MathUtils.cos(RAD_ANGLE_RIGHT), MathUtils.sin(RAD_ANGLE_RIGHT))
        private val DOWN_V = Vector2(MathUtils.cos(RAD_ANGLE_DOWN), MathUtils.sin(RAD_ANGLE_DOWN))
        private val UP_V = Vector2(MathUtils.cos(RAD_ANGLE_UP), MathUtils.sin(RAD_ANGLE_UP))
        private val LEFT_V = Vector2(MathUtils.cos(RAD_ANGLE_LEFT), MathUtils.sin(RAD_ANGLE_LEFT))
    }


}