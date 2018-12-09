package com.lastwords.entities

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.CircleShape
import com.lastwords.ashley.ai.SteeringComponent
import com.lastwords.ashley.body.BodyComponent
import com.lastwords.ashley.spells.CastComponent
import com.lastwords.ashley.entities.EntityStateComponent
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.ashley.stats.PropertiesComponent
import com.lastwords.ashley.stats.StatsComponent
import com.lastwords.ashley.velocity.VelocityComponent
import com.lastwords.ashley.world.AddToWorldComponent

class Prometheus(
        position: Vector2
): Entity() {

    init {
        val propertiesComponent = PropertiesComponent(15f, 15f)
        val circleShape = CircleShape()
        circleShape.radius = propertiesComponent.width
        val bodyComponent = BodyComponent(
                this, Vector2(position.x, position.y),
                BodyDef.BodyType.DynamicBody, circleShape
        )
        add(bodyComponent)
        add(CastComponent())
        add(AddToWorldComponent())
        add(PositionComponent(position.x, position.y))
        add(VelocityComponent())
        add(EntityStateComponent())
        val statsComponent = StatsComponent()
        statsComponent.speed = 5f
        statsComponent.healthPoints = 5000
        add(statsComponent)
        add(SteeringComponent(bodyComponent))
    }

}