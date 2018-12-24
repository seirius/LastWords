package com.lastwords.entities

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.steer.behaviors.Flee
import com.badlogic.gdx.ai.steer.behaviors.Seek
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
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.ashley.stats.PropertiesComponent
import com.lastwords.ashley.stats.StatsComponent
import com.lastwords.ashley.velocity.VelocityComponent
import com.lastwords.ashley.world.AddToWorldComponent
import com.lastwords.ashley.world.ContactComponent
import com.lastwords.ashley.world.ContactImpl

class Prometheus(
        position: Vector2
): Entity() {

    init {
        val propertiesComponent = PropertiesComponent(15f, 15f)
        val circleShape = CircleShape()
        circleShape.radius = propertiesComponent.width
        val bodyComponent = BodyComponent(Vector2(position.x, position.y),
                BodyDef.BodyType.DynamicBody)
        add(bodyComponent)
        val circleShapeSensor = CircleShape()
        circleShapeSensor.radius = 120f
        add(FixtureComponent(bodyComponent.body, mutableListOf(
                ContactSensor(this, circleShape, FixtureType.MAIN),
                ContactSensor(this, circleShapeSensor, FixtureType.SENSOR,
                        PrometheusSensor, false)
        )))
        add(CastComponent())
        add(AddToWorldComponent())
        add(PositionComponent(position.x, position.y))
        add(VelocityComponent())
        add(EntityStateComponent())
        val statsComponent = StatsComponent()
        statsComponent.speed = 5f
        statsComponent.healthPoints = 5000
        add(statsComponent)
        val steeringComponent = SteeringComponent(bodyComponent.body)
        add(steeringComponent)
        add(ContactComponent())
    }

}

object PrometheusSensor: ContactImpl {

    private val steeringMapper = ComponentMapper.getFor(SteeringComponent::class.java)
    private val velocityMapper = ComponentMapper.getFor(VelocityComponent::class.java)

    override fun contact(thisEntity: Entity, contactSensor: ContactSensor, engine: Engine) {
        val steeringComponent = steeringMapper.get(contactSensor.entity)
        if (steeringComponent != null) {
            val thisSteeringComponent = steeringMapper.get(thisEntity)
            thisSteeringComponent.steeringBehavior =
                    Flee<Vector2>(thisSteeringComponent, steeringComponent)
        }
    }

    override fun endContact(thisEntity: Entity, contactSensor: ContactSensor, engine: Engine) {
        println("End contact prometheus")
        val thisSteeringComponent = steeringMapper.get(thisEntity)
        thisSteeringComponent?.steeringBehavior = null
        thisSteeringComponent.steeringAcceleration.linear = Vector2()
        velocityMapper.get(thisEntity)?.velocity = Vector2()
    }
}