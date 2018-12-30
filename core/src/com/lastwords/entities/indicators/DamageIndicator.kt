package com.lastwords.entities.indicators

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.lastwords.ashley.body.NoBodyComponent
import com.lastwords.ashley.deathcondition.DistanceLimitComponent
import com.lastwords.ashley.draw.TextComponent
import com.lastwords.ashley.move.ToTargetComponent
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.ashley.stats.StatsComponent
import com.lastwords.ashley.velocity.VelocityComponent

class DamageIndicator(text: String, position: Vector2): Entity() {

    init {
        val offset = (-2..2).random()
        add(PositionComponent(position.x + offset - 4.5f, position.y + offset + 20f))
        add(TextComponent(text))
        add(ToTargetComponent(Vector2(position.x, position.y + 100)))
        add(DistanceLimitComponent(50f + offset, position))
        add(NoBodyComponent())
        add(VelocityComponent())
        val statsComponent = StatsComponent()
        statsComponent.speed = 30f
        statsComponent.healthPoints = 1
        add(statsComponent)
    }

}