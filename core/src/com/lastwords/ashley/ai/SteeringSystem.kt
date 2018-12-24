package com.lastwords.ashley.ai

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.lastwords.ashley.stats.StatsComponent
import com.lastwords.ashley.velocity.VelocityComponent
import com.lastwords.ashley.velocity.VelocitySystem
import com.lastwords.entities.AshleyEntity
import com.lastwords.util.angleMagnitudeToVector

class SteeringSystem: EntitySystem() {

    private lateinit var entities: ImmutableArray<Entity>

    private val steeringMapper = ComponentMapper.getFor(SteeringComponent::class.java)
    private val velocityMapper = ComponentMapper.getFor(VelocityComponent::class.java)
    private val statsMapper = ComponentMapper.getFor(StatsComponent::class.java)

    override fun addedToEngine(engine: Engine?) {
        entities = engine!!.getEntitiesFor(Family.all(
                SteeringComponent::class.java, VelocityComponent::class.java,
                StatsComponent::class.java
        ).get())
    }

    override fun update(deltaTime: Float) {
        for (entity in entities) {
            val steeringComponent = steeringMapper.get(entity)
            val velocityComponent = velocityMapper.get(entity)
                steeringComponent.steeringBehavior
                        ?.calculateSteering(steeringComponent.steeringAcceleration)
            if (!steeringComponent.steeringAcceleration.linear.isZero) {
                val statsComponent = statsMapper.get(entity)
                velocityComponent.velocity = angleMagnitudeToVector(
                        steeringComponent.steeringAcceleration.linear.angle() * MathUtils.degreesToRadians,
                        statsComponent.speed * VelocitySystem.SPEED_MULTIPLIER
                )
            }


        }
    }

}