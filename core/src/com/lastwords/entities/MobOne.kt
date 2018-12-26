package com.lastwords.entities

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.steer.behaviors.Seek
import com.badlogic.gdx.ai.steer.behaviors.Wander
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.CircleShape
import com.lastwords.ashley.ai.SteeringComponent
import com.lastwords.ashley.body.BodyComponent
import com.lastwords.ashley.body.ContactSensor
import com.lastwords.ashley.body.FixtureComponent
import com.lastwords.ashley.body.FixtureType
import com.lastwords.ashley.death.DeathComponent
import com.lastwords.ashley.entities.EntityStateComponent
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.ashley.spawner.SpawnableClass
import com.lastwords.ashley.stats.Damage
import com.lastwords.ashley.stats.PropertiesComponent
import com.lastwords.ashley.stats.StatsComponent
import com.lastwords.ashley.velocity.VelocityComponent
import com.lastwords.ashley.world.ContactComponent
import com.lastwords.ashley.world.ContactImpl

class MobOne: Entity(), SpawnableClass {

    var positionComponent: PositionComponent = PositionComponent(0f, 0f)

    init {
        add(positionComponent)
        add(EntityStateComponent())
        add(VelocityComponent())
        val statsComponent = StatsComponent()
        statsComponent.speed = 30f
        statsComponent.healthPoints = 3
        statsComponent.attack = 1
        add(statsComponent)

        val propertiesComponent = PropertiesComponent(5f, 5f)
        add(propertiesComponent)
        val circleShape = CircleShape()
        circleShape.radius = propertiesComponent.width
        val bodyComponent = BodyComponent(positionComponent.position, BodyDef.BodyType.DynamicBody)
        add(bodyComponent)
        add(FixtureComponent(
                bodyComponent.body,
                mutableListOf(
                        ContactSensor(this, circleShape, FixtureType.SENSOR, MobOneSensor, true)
                )
        ))

        val steeringComponent = SteeringComponent(bodyComponent.body)
        add(steeringComponent)
        add(ContactComponent())
    }

    override fun setPosition(position: Vector2) {
        positionComponent.position.x = position.x
        positionComponent.position.y = position.y
    }

}

object MobOneSensor: ContactImpl {
    private val statsMapper = ComponentMapper.getFor(StatsComponent::class.java)

    override fun contact(thisEntity: Entity, contactSensor: ContactSensor, engine: Engine) {
        if (contactSensor.entity is MobOne || !contactSensor.linkedState) {
            return
        }

        val thisEntityStats = statsMapper.get(thisEntity)
        val entityStats = statsMapper.get(contactSensor.entity)
        entityStats?.damageReceived?.add(Damage(thisEntityStats.attack))
        thisEntity.add(DeathComponent())
    }

    override fun endContact(thisEntity: Entity, contactSensor: ContactSensor, engine: Engine) {
    }

}