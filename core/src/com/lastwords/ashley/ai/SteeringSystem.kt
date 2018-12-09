package com.lastwords.ashley.ai

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray

class SteeringSystem: EntitySystem() {

    private lateinit var entities: ImmutableArray<Entity>

    private val steeringMapper = ComponentMapper.getFor(SteeringComponent::class.java)

    override fun addedToEngine(engine: Engine?) {
        entities = engine!!.getEntitiesFor(Family.all(SteeringComponent::class.java).get())
    }

    override fun update(deltaTime: Float) {
        for (entity in entities) {
            steeringMapper.get(entity).update(deltaTime)
        }
    }

}