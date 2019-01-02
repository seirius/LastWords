package com.lastwords.util

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.lastwords.ashley.tiledmap.TileNode
import com.lastwords.ashley.tiledmap.TiledMapComponent

fun Vector2.angleToTarget(target: Vector2): Float {
    return MathUtils.atan2(target.y - this.y, target.x - this.x)
}

fun Vector2.tileNode(): TileNode {
    return TileNode((this.x / TiledMapComponent.TILE_SIZE).toInt(), (this.y / TiledMapComponent.TILE_SIZE).toInt())
}

fun angleMagnitudeToVector(angle: Float, magnitude: Float? = null): Vector2 {
    var vector = Vector2(MathUtils.cos(angle), MathUtils.sin(angle))
    if (magnitude != null) {
        vector = vector.scl(magnitude)
    }
    return vector
}