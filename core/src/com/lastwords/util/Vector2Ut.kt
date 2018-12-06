package com.lastwords.util

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2

fun Vector2.angleToTarget(target: Vector2): Float {
    return MathUtils.atan2(target.y - this.y, target.x - this.x)
}

fun angleMagnitudeToVector(angle: Float, magnitude: Float?): Vector2 {
    var vector = Vector2(MathUtils.cos(angle), MathUtils.sin(angle))
    if (magnitude != null) {
        vector = vector.scl(magnitude)
    }
    return vector
}