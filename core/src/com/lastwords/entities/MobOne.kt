package com.lastwords.entities

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.lastwords.ashley.animation.AnimationChunk
import com.lastwords.ashley.animation.AnimationComponent
import com.lastwords.ashley.animation.AnimationType
import com.lastwords.ashley.body.BodyComponent
import com.lastwords.ashley.body.ContactSensor
import com.lastwords.ashley.body.FixtureComponent
import com.lastwords.ashley.body.FixtureType
import com.lastwords.ashley.death.DeathComponent
import com.lastwords.ashley.entities.EntityStateComponent
import com.lastwords.ashley.move.TrackTargetComponent
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.ashley.spawner.SpawnableClass
import com.lastwords.ashley.stats.Damage
import com.lastwords.ashley.stats.PropertiesComponent
import com.lastwords.ashley.stats.StatsComponent
import com.lastwords.ashley.texture.TextureComponent
import com.lastwords.ashley.velocity.VelocityComponent
import com.lastwords.ashley.world.ContactComponent
import com.lastwords.ashley.world.ContactImpl
import com.lastwords.util.setAsBoxPixel
import com.lastwords.util.setPositionPixel
import com.lastwords.util.setRadiusPixel

class MobOne: Entity(), SpawnableClass {

    private var bodyComponent: BodyComponent
    private val positionComponent = PositionComponent(0f, 0f)

    init {
        add(positionComponent)
        add(EntityStateComponent())
        add(VelocityComponent())
        val statsComponent = StatsComponent()
        statsComponent.speed = 15f
        statsComponent.healthPoints = 3
        statsComponent.attack = 1
        add(statsComponent)

        val propertiesComponent = PropertiesComponent(5f, 5f)
        add(propertiesComponent)

        val polygonShape = PolygonShape()
        polygonShape.setAsBoxPixel(5.5f, 6f)

        val playerSensor = CircleShape()
        playerSensor.setRadiusPixel(190f)
        bodyComponent = BodyComponent(positionComponent.position, BodyDef.BodyType.DynamicBody)
        add(bodyComponent)
        add(FixtureComponent(
                bodyComponent.body,
                mutableListOf(
                        ContactSensor(this, polygonShape, FixtureType.MAIN, MobOneSensor, true),
                        ContactSensor(this, playerSensor, FixtureType.SENSOR, MobOnePlayerSensor, false)
                )
        ))

        add(ContactComponent())

        add(TextureComponent())
        add(AnimationComponent()
        .generateRegions(
                path = "micro/PNG/Human/mob_one.png",
                rows = 2, cols = 5,
                chunks = arrayOf(
                        AnimationChunk(AnimationType.IDLE, .15f, arrayOf(0)),
                        AnimationChunk(AnimationType.WALK_RIGHT, .15f, arrayOf(1, 2, 3, 4)),
                        AnimationChunk(AnimationType.WALK_LEFT, .15f, arrayOf(5, 6, 7, 8))
                )))
    }

    override fun setPosition(position: Vector2) {
        positionComponent.position = position.cpy()
        bodyComponent.body.setPositionPixel(position)
    }

}

object MobOneSensor: ContactImpl {
    private val statsMapper = ComponentMapper.getFor(StatsComponent::class.java)

    override fun contact(thisEntity: Entity, contactSensor: ContactSensor, engine: Engine) {
        if (contactSensor.entity is MobOne
                || !contactSensor.linkedState
                || contactSensor.fixtureType == FixtureType.WALL) {
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

object MobOnePlayerSensor: ContactImpl {

    private val trackTargetMapper = ComponentMapper.getFor(TrackTargetComponent::class.java)
    private val velocityMapper = ComponentMapper.getFor(VelocityComponent::class.java)

    override fun contact(thisEntity: Entity, contactSensor: ContactSensor, engine: Engine) {
        if (contactSensor.entity is AshleyEntity && contactSensor.linkedState) {
            thisEntity.add(TrackTargetComponent(contactSensor.entity))
        }
    }

    override fun endContact(thisEntity: Entity, contactSensor: ContactSensor, engine: Engine) {
        if (trackTargetMapper.get(thisEntity) != null
                && contactSensor.entity is AshleyEntity
                && contactSensor.linkedState) {
            thisEntity.remove(TrackTargetComponent::class.java)
            velocityMapper.get(thisEntity).stop()
        }
    }
}