package com.lastwords.ashley.move

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.ashley.stats.StatsComponent
import com.lastwords.ashley.velocity.InputToVelocitySystem
import com.lastwords.ashley.velocity.VelocityComponent

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

            val angle = MathUtils.atan2(toTargetComponent.targetPosition.y - positionComponent.position.y,
                    toTargetComponent.targetPosition.x - positionComponent.position.x)
            val finalSpeed = statsComponent.speed * InputToVelocitySystem.SPEED_MULTIPLIER

            velocityComponent.velocity = Vector2(MathUtils.cos(angle), MathUtils.sin(angle)).scl(finalSpeed)
            entity.remove(ToTargetComponent::class.java)
        }
    }


}