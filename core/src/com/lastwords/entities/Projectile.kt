package com.lastwords.entities

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.CircleShape
import com.lastwords.ashley.body.BodyComponent
import com.lastwords.ashley.move.ToTargetComponent
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.ashley.stats.StatsComponent
import com.lastwords.ashley.velocity.VelocityComponent
import com.lastwords.ashley.world.AddToWorldComponent

class Projectile(
        initPosition: Vector2,
        targetPosition: Vector2,
        speed: Float
) : Entity() {


    init {
        System.out.println("Projectiles fired")
        val circleChape = CircleShape()
        circleChape.radius = 3f
        val statsComponent = StatsComponent()
        statsComponent.speed = 40f
        add(statsComponent)
        add(BodyComponent(initPosition, BodyDef.BodyType.DynamicBody, circleChape))
        add(AddToWorldComponent())
        add(PositionComponent(initPosition.x, initPosition.y))
        add(ToTargetComponent(targetPosition))
        add(VelocityComponent())
    }

}