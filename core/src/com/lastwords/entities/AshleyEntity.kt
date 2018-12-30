package com.lastwords.entities

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.lastwords.LastWords
import com.lastwords.ashley.animation.AnimationComponent
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
        polygonShape.setAsBoxPixel(7.5f, 8.5f)
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

        val texture = Texture("micro/PNG/Human/temp.png")
        val tmp = TextureRegion.split(texture, texture.width / 4, texture.height)
        val walkRight: Array<TextureRegion?> = Array(4) { null }
        var index = 0
        for (j in (0..3)) {
            walkRight[index++] = tmp[0][j]
        }

        val textureLeft = Texture("micro/PNG/Human/temp_left.png")
        val tmpLeft = TextureRegion.split(textureLeft, textureLeft.width / 4, texture.height)
        val walkLeft: Array<TextureRegion?> = Array(4) { null }
        index = 0
        for (i in (0..3)) {
            walkLeft[index++] = tmpLeft[0][i]
        }

        val textureStill = Texture("micro/PNG/Human/temp_still.png")
        val tmpStill = TextureRegion.split(textureStill, textureStill.width / 2, texture.height)
        val still: Array<TextureRegion?> = Array(2) { null }
        index = 0
        for (i in (0..1)) {
            still[index++] = tmpStill[0][i]
        }
        add(TextureComponent())
        add(AnimationComponent(Animation<TextureRegion>(0.75f, *still), Animation<TextureRegion>(0.25f, *walkLeft),
                Animation<TextureRegion>(0.25f, *walkRight)))
    }

}
