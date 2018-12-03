package com.lastwords.ashley.death

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.lastwords.ashley.body.BodyComponent

class DeathComponent: Component

class DeathSystem: EntitySystem() {

    private lateinit var entities: ImmutableArray<Entity>

    private val bodyMapper = ComponentMapper.getFor(BodyComponent::class.java)

    override fun addedToEngine(engine: Engine?) {
        entities = engine!!.getEntitiesFor(Family.all(DeathComponent::class.java).get())
    }

    override fun update(dt: Float) {
        for (entity in entities) {
            engine.removeEntity(entity)
            bodyMapper.get(entity)?.die()
        }
    }

}