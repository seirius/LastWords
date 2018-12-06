package com.lastwords.ashley.velocity

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.math.Vector2
import com.lastwords.ashley.body.BodyComponent
import com.lastwords.ashley.body.NoBodyComponent
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.ashley.stats.StatsComponent

class VelocitySystem : EntitySystem() {

    private lateinit var entities: ImmutableArray<Entity>
    private lateinit var noBodyEntities: ImmutableArray<Entity>

    private val velocityMapper = ComponentMapper.getFor(VelocityComponent::class.java)
    private val bodyMapper = ComponentMapper.getFor(BodyComponent::class.java)
    private val positionMapper = ComponentMapper.getFor(PositionComponent::class.java)

    override fun addedToEngine(engine: Engine?) {
        entities = engine!!.getEntitiesFor(Family
                .all(VelocityComponent::class.java, BodyComponent::class.java).get())

        noBodyEntities = engine.getEntitiesFor(Family
                .all(VelocityComponent::class.java, PositionComponent::class.java, NoBodyComponent::class.java).get())
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
    }
}
