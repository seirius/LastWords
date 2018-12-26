package com.lastwords.ashley.spawner

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.math.Vector2
import com.lastwords.ashley.position.PositionComponent

class SpawnerComponent<T>(
        var clazz: Class<T>,
        var interval: Float,
        var number: Int,
        var counter: Float = 0f
): Component

class SpawnerSystem: EntitySystem() {

    private lateinit var entities: ImmutableArray<Entity>

    private val positionMapper = ComponentMapper.getFor(PositionComponent::class.java)
    private val spawnerMapper = ComponentMapper.getFor(SpawnerComponent::class.java)

    override fun addedToEngine(engine: Engine?) {
        entities = engine!!.getEntitiesFor(Family
                .all(
                   PositionComponent::class.java, SpawnerComponent::class.java
                ).get()
        )
    }

    override fun update(deltaTime: Float) {
        for (entity in entities) {
            val spawnerComponent = spawnerMapper.get(entity)
            spawnerComponent.counter += deltaTime
            if (spawnerComponent.counter >= spawnerComponent.interval) {
                spawnerComponent.counter = 0f

                val positionComponent = positionMapper.get(entity)

                for (i in (0..spawnerComponent.number)) {
                    val spawnableInstance = spawnerComponent.clazz.newInstance() as SpawnableClass
                    spawnableInstance.setPosition(positionComponent.position.cpy())
                    engine.addEntity(spawnableInstance as Entity)
                }
            }
        }
    }

}

interface SpawnableClass {

    fun setPosition(position: Vector2)

}