package com.lastwords.entities

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.CircleShape
import com.lastwords.ashley.body.BodyComponent
import com.lastwords.ashley.position.PositionComponent

class Projectile(
        private var initPosition: Vector2,
        private var targetPosition: Vector2,
        private var speed: Float
) : Entity() {


    init {
        val circleChape = CircleShape()
        circleChape.radius = 3f
        add(BodyComponent(initPosition, BodyDef.BodyType.DynamicBody, circleChape))
        add(PositionComponent(initPosition.x, initPosition.y))
    }

}