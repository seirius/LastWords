package com.lastwords.ashley.velocity

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.math.Vector2
import com.lastwords.ashley.body.BodyComponent
import com.lastwords.ashley.stats.StatsComponent

class VelocitySystem : EntitySystem() {

    private var entities: ImmutableArray<Entity>? = null

    private val velocityMapper = ComponentMapper.getFor(VelocityComponent::class.java)
    private val bodyMapper = ComponentMapper.getFor(BodyComponent::class.java)

    override fun addedToEngine(engine: Engine?) {
        entities = engine!!.getEntitiesFor(Family
                .all(VelocityComponent::class.java, StatsComponent::class.java, BodyComponent::class.java).get())
    }

    override fun update(deltaTime: Float) {
        for (entity in entities!!) {
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
    }
}
