package com.lastwords.ashley.move

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.ashley.stats.StatsComponent
import com.lastwords.ashley.velocity.VelocityComponent
import com.lastwords.ashley.velocity.VelocitySystem
import com.lastwords.util.angleMagnitudeToVector
import com.lastwords.util.angleToTarget

class MoveToTargetSystem: EntitySystem() {

    private lateinit var entities: ImmutableArray<Entity>

    private val velocityMapper = ComponentMapper.getFor(VelocityComponent::class.java)
    private val toTargetMapper = ComponentMapper.getFor(ToTargetComponent::class.java)
    private val positionMapper = ComponentMapper.getFor(PositionComponent::class.java)
    private val statsMapper = ComponentMapper.getFor(StatsComponent::class.java)

    override fun addedToEngine(engine: Engine?) {
        entities = engine!!.getEntitiesFor(Family
                .all(
                    VelocityComponent::class.java,
                    ToTargetComponent::class.java,
                    PositionComponent::class.java,
                    StatsComponent::class.java
                ).get())
    }

    override fun update(deltaTime: Float) {
        for (entity in entities) {
            val toTargetComponent = toTargetMapper.get(entity)
            val velocityComponent = velocityMapper.get(entity)
            val positionComponent = positionMapper.get(entity)
            val statsComponent = statsMapper.get(entity)

            val angle = positionComponent.position.angleToTarget(toTargetComponent.targetPosition)
            val finalSpeed = statsComponent.speed * VelocitySystem.SPEED_MULTIPLIER

            velocityComponent.velocity = angleMagnitudeToVector(angle, finalSpeed)
            entity.remove(ToTargetComponent::class.java)
        }
    }


}