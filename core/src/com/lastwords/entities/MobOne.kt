package com.lastwords.entities

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.PolygonShape
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

        var texture = Texture("micro/PNG/Human/mob_one.png")
        val size = 8
        var tmpRight = TextureRegion.split(texture, texture.width / size, texture.height)
//        val walkRight: Array<TextureRegion?> = Array(size - 4) { null }
//        var index = 0
//        for (i in (0 until size)) {
//            if (i > 3) {
//                walkRight[index++] = tmpRight[0][i]
//            }
//        }
//        val animationComponent = AnimationComponent().generateRegions()
//        val chunk = AnimationChunk(AnimationType.WALK_RIGHT, arrayOf(4, 5, 6, 7))
//        generateRegions(
//                path = "micro/PNG/Human/mob_one.png",
//                cols = 8, rows = 1,
//                chunks = arrayOf(chunk)
//        )
//        val walkRight = chunk.regions!!
//
//        texture = Texture("micro/PNG/Human/mob_one_left.png")
//        var tmpLeft = TextureRegion.split(texture, texture.width / size, texture.height)
//        val walkLeft: Array<TextureRegion?> = Array(size - 4) { null }
//        var index = 0
//        for (i in (0 until size)) {
//            if (i < 4) {
//                walkLeft[index++] = tmpLeft[0][i]
//            }
//        }
//        walkLeft.reverse()
//
//        val walkRightAnimation = Animation<TextureRegion>(0.15f, *walkRight)
//        val walkLeftAnimation = Animation<TextureRegion>(0.15f, *walkLeft)
//        val stillAnimation = Animation<TextureRegion>(1f, tmpRight[0][2])
//
//        add(TextureComponent())
//        add(AnimationComponent(stillAnimation, walkLeftAnimation, walkRightAnimation))
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