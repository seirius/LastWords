package com.lastwords.entities

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.CircleShape
import com.lastwords.ashley.ai.SteeringComponent
import com.lastwords.ashley.body.BodyComponent
import com.lastwords.ashley.body.ContactSensor
import com.lastwords.ashley.body.FixtureComponent
import com.lastwords.ashley.body.FixtureType
import com.lastwords.ashley.spells.CastComponent
import com.lastwords.ashley.entities.EntityStateComponent
import com.lastwords.ashley.player.PlayerComponent
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.ashley.spells.TargetComponent
import com.lastwords.ashley.stats.PropertiesComponent
import com.lastwords.ashley.stats.StatsComponent
import com.lastwords.ashley.velocity.VelocityComponent
import com.lastwords.ashley.world.AddToWorldComponent
import com.lastwords.ashley.world.ContactComponent

class AshleyEntity(xPosition: Float, yPosition: Float, speed: Float) : Entity() {

    init {
        val propertiesComponent = PropertiesComponent(6f, 6f)
        add(propertiesComponent)
        val circleShape = CircleShape()
        circleShape.radius = propertiesComponent.width
        val bodyComponent = BodyComponent(Vector2(xPosition, yPosition), BodyDef.BodyType.DynamicBody)
        add(bodyComponent)
        add(FixtureComponent(bodyComponent.body, mutableListOf(
                ContactSensor(this, circleShape, FixtureType.MAIN)
        )))
        add(CastComponent())
        add(AddToWorldComponent())
        add(PositionComponent(xPosition, yPosition))
        add(VelocityComponent())
        add(EntityStateComponent())
        val statsComponent = StatsComponent()
        statsComponent.speed = speed
        statsComponent.healthPoints = 5
        statsComponent.energy = 100
        add(statsComponent)
        add(PlayerComponent())
        add(TargetComponent())
        add(SteeringComponent(bodyComponent.body))
        add(ContactComponent())
    }

}
