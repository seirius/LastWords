package com.lastwords.entities

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.CircleShape
import com.lastwords.ashley.body.BodyComponent
import com.lastwords.ashley.deathcondition.DistanceLimitComponent
import com.lastwords.ashley.deathcondition.TimeLimitComponent
import com.lastwords.ashley.move.ToTargetComponent
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.ashley.stats.PropertiesComponent
import com.lastwords.ashley.stats.StatsComponent
import com.lastwords.ashley.velocity.VelocityComponent
import com.lastwords.ashley.world.AddToWorldComponent
import com.lastwords.util.angleMagnitudeToVector
import com.lastwords.util.angleToTarget

class Projectile(
        initPosition: Vector2,
        targetPosition: Vector2,
        speed: Float,
        offset: Float
) : Entity() {


    init {
        val statsComponent = StatsComponent()
        val propertiesComponent = PropertiesComponent(3f, 3f)
        initPosition.add(angleMagnitudeToVector(initPosition
                .angleToTarget(targetPosition), offset + 1).scl(propertiesComponent.width / 2 + 1))
        statsComponent.speed = speed
        add(statsComponent)
        add(propertiesComponent)
        val circleShape = CircleShape()
        circleShape.radius = propertiesComponent.width
        add(BodyComponent(this, initPosition, BodyDef.BodyType.DynamicBody, circleShape, true))
        add(AddToWorldComponent())
        add(PositionComponent(initPosition.x, initPosition.y))
        add(ToTargetComponent(targetPosition))
        add(VelocityComponent())
        add(DistanceLimitComponent(200f, initPosition))
    }

}