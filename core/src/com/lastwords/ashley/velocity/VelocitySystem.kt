package com.lastwords.ashley.velocity

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.math.Vector2
import com.lastwords.ashley.body.BodyComponent
import com.lastwords.ashley.body.NoBodyComponent
import com.lastwords.ashley.entities.EntityStateComponent
import com.lastwords.ashley.entities.MoveDirection
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.ashley.stats.StatsComponent
import com.lastwords.entities.Prometheus

class VelocitySystem : EntitySystem() {

    private lateinit var entities: ImmutableArray<Entity>
    private lateinit var noBodyEntities: ImmutableArray<Entity>
    private lateinit var moveDirectionEntities: ImmutableArray<Entity>

    private val velocityMapper = ComponentMapper.getFor(VelocityComponent::class.java)
    private val bodyMapper = ComponentMapper.getFor(BodyComponent::class.java)
    private val positionMapper = ComponentMapper.getFor(PositionComponent::class.java)
    private val entityStateMapper = ComponentMapper.getFor(EntityStateComponent::class.java)

    override fun addedToEngine(engine: Engine?) {
        entities = engine!!.getEntitiesFor(Family
                .all(VelocityComponent::class.java, BodyComponent::class.java).get())

        noBodyEntities = engine.getEntitiesFor(Family
                .all(VelocityComponent::class.java, PositionComponent::class.java, NoBodyComponent::class.java).get())

        moveDirectionEntities = engine.getEntitiesFor(Family
                .all(VelocityComponent::class.java, EntityStateComponent::class.java).get())
    }

    override fun update(deltaTime: Float) {
        for (entity in entities) {
            val velocityComponent = velocityMapper.get(entity)
            val bodyComponent = bodyMapper.get(entity)
            val body = bodyComponent?.body
            if (body != null) {
                if (!velocityComponent.velocity.isZero) {
                    val finalVelocity = velocityComponent.velocity.cpy().scl(deltaTime)
                    body.linearVelocity = finalVelocity
                    body.setTransform(body.position, finalVelocity.angleRad())
                } else {
                    body.linearVelocity = Vector2.Zero
                }
            }
        }

        for (entity in noBodyEntities) {
            val velocityComponent = velocityMapper.get(entity)
            if (!velocityComponent.velocity.isZero) {
                val finalVelocity = velocityComponent.velocity.cpy().scl(deltaTime)
                val positionComponent = positionMapper.get(entity)
                positionComponent.position.add(finalVelocity)
            }
        }

        for (entity in moveDirectionEntities) {
            val velocityComponent = velocityMapper.get(entity)
            val entityStateComponent = entityStateMapper.get(entity)

            if (!velocityComponent.velocity.isZero) {
                val angle = velocityComponent.velocity.angle()

                when (angle) {
                    in 45.0..135.0 -> entityStateComponent.moveDirection = MoveDirection.UP
                    in 135.0..225.0 -> entityStateComponent.moveDirection = MoveDirection.LEFT
                    in 225.0..315.0 -> entityStateComponent.moveDirection = MoveDirection.DOWN
                    else -> entityStateComponent.moveDirection = MoveDirection.RIGHT
                }
            }
        }
    }

    companion object {
        const val SPEED_MULTIPLIER = 100f
    }
}
