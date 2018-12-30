package com.lastwords.util

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.lastwords.LastWords
import com.lastwords.ashley.death.DeathComponent

val deathMapper = ComponentMapper.getFor(DeathComponent::class.java)!!

fun Entity.isAboutToDie(): Boolean {
    return deathMapper.get(this) != null
}

fun PolygonShape.setAsBoxPixel(p1: Float, p2: Float) {
    this.setAsBox(p1 * LastWords.PIXEL_TO_METER, p2 * LastWords.PIXEL_TO_METER)
}

fun CircleShape.setRadiusPixel(radius: Float) {
    this.radius = radius * LastWords.PIXEL_TO_METER
}

fun BodyDef.setPositionPixel(position: Vector2) {
    this.position.set(position.cpy().scl(LastWords.PIXEL_TO_METER))
}

fun Body.setPositionPixel(position: Vector2) {
    this.setTransform(position.cpy().scl(LastWords.PIXEL_TO_METER), this.angle)
}

fun Body.getPositionPixel(): Vector2 {
    return this.position.cpy().scl(LastWords.METER_TO_PIXEL)
}