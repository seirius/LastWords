package com.lastwords.ashley.stats

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.lastwords.ashley.death.DeathComponent

class StatsSystem: EntitySystem() {

    private lateinit var entities: ImmutableArray<Entity>

    private val statsMapper = ComponentMapper.getFor(StatsComponent::class.java)

    override fun addedToEngine(engine: Engine?) {
        entities = engine!!.getEntitiesFor(Family.all(StatsComponent::class.java).get())
    }

    override fun update(deltaTime: Float) {

        for (entity in entities) {
            val statsComponent = statsMapper.get(entity)

            for (damageCounter in statsComponent.damageReceived) {
                statsComponent.healthPoints -= damageCounter.amount
            }
            statsComponent.damageReceived.clear()

            if (statsComponent.healthPoints < 1) {
                entity.add(DeathComponent())
            }

        }
    }

}