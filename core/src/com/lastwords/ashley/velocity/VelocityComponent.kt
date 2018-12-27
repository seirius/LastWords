package com.lastwords.ashley.velocity

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2

class VelocityComponent : Component {

    var velocity: Vector2

    constructor() {
        this.velocity = Vector2()
    }

    constructor(velocity: Vector2) {
        this.velocity = velocity
    }

    fun stop() {
        velocity = Vector2()
    }

}
