package com.lastwords.entities.spells

import com.badlogic.ashley.core.*
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.CircleShape
import com.lastwords.ashley.animation.AnimationComponent
import com.lastwords.ashley.body.BodyComponent
import com.lastwords.ashley.body.ContactSensor
import com.lastwords.ashley.body.FixtureComponent
import com.lastwords.ashley.body.FixtureType
import com.lastwords.ashley.death.DeathComponent
import com.lastwords.ashley.deathcondition.TimeLimitComponent
import com.lastwords.ashley.move.ToTargetComponent
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.ashley.stats.Damage
import com.lastwords.ashley.stats.PropertiesComponent
import com.lastwords.ashley.stats.StatsComponent
import com.lastwords.ashley.texture.TextureComponent
import com.lastwords.ashley.velocity.VelocityComponent
import com.lastwords.ashley.world.AddToWorldComponent
import com.lastwords.ashley.world.ContactComponent
import com.lastwords.ashley.world.ContactImpl
import com.lastwords.util.angleMagnitudeToVector
import com.lastwords.util.angleToTarget
import com.lastwords.util.isAboutToDie

class Projectile(
        initPosition: Vector2,
        targetPosition: Vector2? = null,
        speed: Float,
        offset: Float,
        duration: Float = 2f
) : Entity() {


    init {
        val statsComponent = StatsComponent()
        val propertiesComponent = PropertiesComponent(2f, 2f)
        if (targetPosition != null) {
            initPosition.add(angleMagnitudeToVector(initPosition
                    .angleToTarget(targetPosition), offset + 1).scl(propertiesComponent.width / 2 + 1))
            add(ToTargetComponent(targetPosition))
        }
        statsComponent.speed = speed
        statsComponent.healthPoints = 1
        statsComponent.attack = 1
        add(statsComponent)
        add(propertiesComponent)
        val circleShape = CircleShape()
        circleShape.radius = propertiesComponent.width
        val bodyComponent = BodyComponent(initPosition, BodyDef.BodyType.DynamicBody)
        add(bodyComponent)
        add(FixtureComponent(bodyComponent.body, mutableListOf(
                ContactSensor(this, circleShape, FixtureType.SENSOR, ProjectileContact)
        )))
        add(AddToWorldComponent())
        add(PositionComponent(initPosition.x, initPosition.y))
        add(VelocityComponent())
        add(TimeLimitComponent(duration))
        add(ContactComponent())

        val texture = Texture("micro/PNG/Human/fireball.png")
        val size = 3
        var tmp = TextureRegion.split(texture, texture.width / size, texture.height)
        val walk: Array<TextureRegion?> = Array(size) { null }
        var index = 0
        for (i in (0 until size)) {
            walk[index++] = tmp[0][i]
        }
        val animation = Animation<TextureRegion>(0.15f, *walk)
        add(TextureComponent())
        add(AnimationComponent(animation, animation, animation, animation))
    }

}

object ProjectileContact: ContactImpl {
    private val statsMapper = ComponentMapper.getFor(StatsComponent::class.java)
    private val velocityMapper = ComponentMapper.getFor(VelocityComponent::class.java)

    override fun contact(thisEntity: Entity, contactSensor: ContactSensor, engine: Engine) {
        if (!thisEntity.isAboutToDie() && contactSensor.linkedState) {
            val thisEntityStats = statsMapper.get(thisEntity)
            val entityStats = statsMapper.get(contactSensor.entity)
            if (contactSensor.entity is Projectile) {
                velocityMapper.get(thisEntity).velocity.scl(2f)
                velocityMapper.get(contactSensor.entity).velocity.scl(2f)
                thisEntityStats.attack += entityStats.attack
                entityStats.attack += thisEntityStats.attack
            } else if (contactSensor.fixtureType === FixtureType.WALL) {
                thisEntity.add(DeathComponent())
            } else {
                entityStats.damageReceived.add(Damage(thisEntityStats.attack))
                thisEntity.add(DeathComponent())
            }
        }

    }

    override fun endContact(thisEntity: Entity, contactSensor: ContactSensor, engine: Engine) {
    }
}