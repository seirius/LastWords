package com.lastwords.entities

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.lastwords.ashley.animation.AnimationChunk
import com.lastwords.ashley.animation.AnimationComponent
import com.lastwords.ashley.animation.AnimationType
import com.lastwords.ashley.body.BodyComponent
import com.lastwords.ashley.body.ContactSensor
import com.lastwords.ashley.body.FixtureComponent
import com.lastwords.ashley.body.FixtureType
import com.lastwords.ashley.entities.EntityStateComponent
import com.lastwords.ashley.player.PlayerComponent
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.ashley.spells.CastComponent
import com.lastwords.ashley.spells.TargetComponent
import com.lastwords.ashley.stats.PropertiesComponent
import com.lastwords.ashley.stats.StatsComponent
import com.lastwords.ashley.texture.TextureComponent
import com.lastwords.ashley.velocity.VelocityComponent
import com.lastwords.ashley.world.AddToWorldComponent
import com.lastwords.ashley.world.ContactComponent
import com.lastwords.util.setAsBoxPixel

class AshleyEntity(xPosition: Float, yPosition: Float, speed: Float) : Entity() {

    init {
        val propertiesComponent = PropertiesComponent(6f, 6f)
        add(propertiesComponent)
        val polygonShape = PolygonShape()
        polygonShape.setAsBoxPixel(9f, 9f)
        val bodyComponent = BodyComponent(Vector2(xPosition, yPosition), BodyDef.BodyType.DynamicBody)
        add(bodyComponent)
        add(FixtureComponent(bodyComponent.body, mutableListOf(
                ContactSensor(this, polygonShape, FixtureType.MAIN)
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
        statsComponent.maxEnergy = 1000
        statsComponent.energyReg = 20
        add(statsComponent)
        add(PlayerComponent())
        add(TargetComponent())
        add(ContactComponent())

        add(TextureComponent(yOffset = 4f))
        add(AnimationComponent()
                .generateRegions(
                        path = "adv.png",
                        rows = 16, cols = 13 ,
                        chunks = arrayOf(
                                AnimationChunk(AnimationType.IDLE, .075f, (0..13).toList().toTypedArray()),
                                AnimationChunk(AnimationType.WALK_RIGHT, .075f, (14..20 ).toList().toTypedArray()),
                                AnimationChunk(AnimationType.WALK_LEFT, .075f, (117..122).toList().toTypedArray())
                        )))

    }

}
