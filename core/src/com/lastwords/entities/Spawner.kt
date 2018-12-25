package com.lastwords.entities

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.ashley.spawner.SpawnerComponent

class Spawner<T>(
        clazz: Class<T>,
        position: Vector2,
        interval: Float,
        number: Int
): Entity() {

    init {
        add(PositionComponent(position.x, position.y))
        add(SpawnerComponent(clazz, interval, number))
    }

}