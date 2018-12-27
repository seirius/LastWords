package com.lastwords.entities

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.EdgeShape
import com.lastwords.ashley.body.BodyComponent
import com.lastwords.ashley.body.ContactSensor
import com.lastwords.ashley.body.FixtureComponent
import com.lastwords.ashley.body.FixtureType

enum class WallDirection {
    HORIZONTAL, VERTICAL
}

class Wall(
        val position: Vector2,
        wallDirection: WallDirection,
        size: Float
) : Entity() {

    init {
        val bodyComponent = BodyComponent(position, BodyDef.BodyType.StaticBody)
        add(bodyComponent)

        val edgeShape = EdgeShape()
        if (wallDirection == WallDirection.VERTICAL) {
            edgeShape.set(Vector2(position.x - size / 2, position.y), Vector2(size / 2 + position.x, position.y))
        } else {
            edgeShape.set(Vector2(position.x, position.y - size / 2), Vector2(position.x, size / 2 + position.y))
        }

        add(FixtureComponent(bodyComponent.body, mutableListOf(
                ContactSensor(this, edgeShape, FixtureType.WALL)
        )))
    }

}