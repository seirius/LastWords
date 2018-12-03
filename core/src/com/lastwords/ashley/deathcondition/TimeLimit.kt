package com.lastwords.ashley.deathcondition

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.lastwords.ashley.death.DeathComponent

class TimeLimitComponent(var time: Float): Component {

    private var totalTime = 0f

    fun shouldYouDie(deltaTime: Float): Boolean {
        totalTime += deltaTime
        return totalTime > time
    }

}

class TimeLimitSystem: EntitySystem() {

    private lateinit var entities: ImmutableArray<Entity>

    private val timeLimitMapper = ComponentMapper.getFor(TimeLimitComponent::class.java)

    override fun addedToEngine(engine: Engine?) {
        entities = engine!!.getEntitiesFor(Family.all(TimeLimitComponent::class.java).get())
    }

    override fun update(deltaTime: Float) {
        for (entity in entities) {
            if (timeLimitMapper.get(entity).shouldYouDie(deltaTime)) {
                entity.add(DeathComponent())
            }
        }
    }

}