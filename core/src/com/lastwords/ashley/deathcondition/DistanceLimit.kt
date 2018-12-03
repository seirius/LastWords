package com.lastwords.ashley.deathcondition

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.math.Vector2
import com.lastwords.ashley.body.BodyComponent
import com.lastwords.ashley.death.DeathComponent
import com.lastwords.ashley.position.PositionComponent

class DistanceLimitComponent(var distance: Float, var position: Vector2): Component {

    var distanceDone = 0f

}

class DistanceLimitSystem: EntitySystem() {

    private lateinit var entities: ImmutableArray<Entity>

    private var tick: Float = 0f

    private val positionMapper = ComponentMapper.getFor(PositionComponent::class.java)
    private val distanceLimitMapper = ComponentMapper.getFor(DistanceLimitComponent::class.java)
    private val bodyMapper = ComponentMapper.getFor(BodyComponent::class.java)

    override fun addedToEngine(engine: Engine?) {
        entities = engine!!.getEntitiesFor(Family.all(PositionComponent::class.java, DistanceLimitComponent::class.java).get())
    }

    override fun update(dt: Float) {
        tick += dt

        if (tick > 0.1f) {
            tick = 0f
            for (entity in entities) {
                val positionComponent = positionMapper.get(entity)
                val distanceLimitComponent = distanceLimitMapper.get(entity)

                val distancePosition = distanceLimitComponent.position
                val currentPosition = positionComponent.position
                distanceLimitComponent.distanceDone += Math.abs(distancePosition.dst(currentPosition.x, currentPosition.y))
                distanceLimitComponent.position = positionComponent.position.cpy()
                System.out.println(distanceLimitComponent.distanceDone)
                if (distanceLimitComponent.distanceDone >= distanceLimitComponent.distance) {
                    entity.add(DeathComponent())
                }
            }
        }
    }

}