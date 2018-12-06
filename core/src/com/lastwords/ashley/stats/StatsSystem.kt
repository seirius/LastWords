package com.lastwords.ashley.stats

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.lastwords.ashley.death.DeathComponent
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.entities.indicators.DamageIndicator

class StatsSystem: EntitySystem() {

    private lateinit var entities: ImmutableArray<Entity>

    private val statsMapper = ComponentMapper.getFor(StatsComponent::class.java)
    private val positionMapper = ComponentMapper.getFor(PositionComponent::class.java)

    override fun addedToEngine(engine: Engine?) {
        entities = engine!!.getEntitiesFor(Family.all(StatsComponent::class.java, PositionComponent::class.java).get())
    }

    override fun update(deltaTime: Float) {

        for (entity in entities) {
            val statsComponent = statsMapper.get(entity)
            val positionComponent = positionMapper.get(entity)

            for (damageCounter in statsComponent.damageReceived) {
                engine.addEntity(DamageIndicator(damageCounter.amount.toString(), positionComponent.position.cpy()))
                statsComponent.healthPoints -= damageCounter.amount
            }
            statsComponent.damageReceived.clear()

            if (statsComponent.healthPoints < 1) {
                entity.add(DeathComponent())
            }

        }
    }

}