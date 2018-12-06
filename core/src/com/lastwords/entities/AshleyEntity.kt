package com.lastwords.entities

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.CircleShape
import com.lastwords.ashley.body.BodyComponent
import com.lastwords.ashley.entities.CastComponent
import com.lastwords.ashley.entities.EntityStateComponent
import com.lastwords.ashley.playerinput.PlayerComponent
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.ashley.stats.PropertiesComponent
import com.lastwords.ashley.stats.StatsComponent
import com.lastwords.ashley.velocity.VelocityComponent
import com.lastwords.ashley.world.AddToWorldComponent

class AshleyEntity(xPosition: Float, yPosition: Float, speed: Float) : Entity() {

    init {
        val propertiesComponent = PropertiesComponent(6f, 6f)
        add(propertiesComponent)
        val circleShape = CircleShape()
        circleShape.radius = propertiesComponent.width
        add(BodyComponent(
                this, Vector2(xPosition, yPosition),
                BodyDef.BodyType.DynamicBody, circleShape
        ))
        add(CastComponent())
        add(AddToWorldComponent())
        add(PositionComponent(xPosition, yPosition))
        add(VelocityComponent())
        add(EntityStateComponent())
        val statsComponent = StatsComponent()
        statsComponent.speed = speed
        statsComponent.healthPoints = 5
        add(statsComponent)
        add(PlayerComponent())
    }

}
