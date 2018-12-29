package com.lastwords.ashley.entities

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.lastwords.ashley.velocity.VelocityComponent

class EntityStateComponent : Component {

    var castState: Boolean = false
    var moveDirection: MoveDirection = MoveDirection.DOWN
    var state: EntityState = EntityState.STILL

    fun toggleCastState() {
        castState = !castState
    }

}

class EntityStateSystem: EntitySystem() {

    private lateinit var entities: ImmutableArray<Entity>

    private val velocityMapper = ComponentMapper.getFor(VelocityComponent::class.java)
    private val entityStateMapper = ComponentMapper.getFor(EntityStateComponent::class.java)

    override fun addedToEngine(engine: Engine?) {
        entities = engine!!.getEntitiesFor(Family.all(VelocityComponent::class.java, EntityStateComponent::class.java).get())
    }

    override fun update(deltaTime: Float) {
        for (entity in entities) {
            val velocityComponent = velocityMapper.get(entity)
            val entityStateComponent = entityStateMapper.get(entity)
            if (velocityComponent.velocity.isZero) {
                entityStateComponent.state = EntityState.STILL
            } else {
                entityStateComponent.state = EntityState.MOVING
            }
        }
    }

}

enum class MoveDirection {
    UP, RIGHT, DOWN, LEFT
}

enum class EntityState {
    MOVING, STILL
}
