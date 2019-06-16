package com.lastwords.entities

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.lastwords.ashley.body.BodyComponent
import com.lastwords.ashley.body.ContactSensor
import com.lastwords.ashley.body.FixtureComponent
import com.lastwords.ashley.body.FixtureType
import com.lastwords.ashley.entities.EntityStateComponent
import com.lastwords.ashley.player.PlayerComponent
import com.lastwords.ashley.player.PlayerMovementComponent
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.ashley.spells.TargetComponent
import com.lastwords.ashley.stats.PropertiesComponent
import com.lastwords.ashley.stats.StatsComponent
import com.lastwords.ashley.texture.TextureComponent
import com.lastwords.ashley.velocity.VelocityComponent
import com.lastwords.ashley.world.AddToWorldComponent
import com.lastwords.util.setAsBoxPixel


class TestPlayer(
        xPosition: Float = 10f,
        yPosition: Float = 10f,
        speed: Float = 30f,
        squareSize: Float = 9f,
        hp: Int = 5,
        energy: Int = 100,
        maxEnergy: Int = 100,
        energyReg: Int = 10
) : Entity() {

    init {
        add(PropertiesComponent(squareSize, squareSize))
        val polygonShape = PolygonShape()
        polygonShape.setAsBoxPixel(squareSize, squareSize)
        val bodyComponent = BodyComponent(Vector2(xPosition, yPosition), BodyDef.BodyType.DynamicBody)
        add(bodyComponent)
        add(FixtureComponent(bodyComponent.body, mutableListOf(ContactSensor(this, polygonShape, FixtureType.MAIN))))
        add(AddToWorldComponent())
        add(PositionComponent(xPosition, yPosition))
        add(VelocityComponent())
        add(TargetComponent())
        add(EntityStateComponent())
        val statsComponent = StatsComponent()
        statsComponent.speed = speed
        statsComponent.healthPoints = hp
        statsComponent.energy = energy
        statsComponent.maxEnergy = maxEnergy
        statsComponent.energyReg = energyReg
        add(statsComponent)
        add(PlayerComponent())
        add(PlayerMovementComponent())
        add(TextureComponent(TextureRegion.split(Texture("square_9.png"), 9, 9)[0][0]))
    }

}