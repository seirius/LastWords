package com.lastwords.ashley.position

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2

class PositionComponent(xPosition: Float, yPosition: Float) : Component {

    var position: Vector2 = Vector2(xPosition, yPosition)

}
