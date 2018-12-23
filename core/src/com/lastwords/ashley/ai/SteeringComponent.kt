package com.lastwords.ashley.ai

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.ai.steer.Steerable
import com.badlogic.gdx.ai.steer.SteeringAcceleration
import com.badlogic.gdx.ai.steer.SteeringBehavior
import com.badlogic.gdx.ai.steer.behaviors.Seek
import com.badlogic.gdx.ai.utils.Location
import com.badlogic.gdx.math.Vector2
import com.lastwords.ashley.body.BodyComponent
import com.lastwords.util.angleMagnitudeToVector

class SteeringComponent(private var bodyComponent: BodyComponent): Component, Steerable<Vector2> {

    private var _maxAngularSpeed: Float = 10f
    private var _maxLinearSpeed: Float = 10f
    private var _angularVelocity: Float = 10f
    private var _maxAngularAcceleration: Float = 10f
    private var _linearVelocity: Vector2 = Vector2(10f, 10f)
    private var _maxLinearAcceleration: Float = 10f
    private var tagged: Boolean = false
    private var boundingRadius: Float = 10f

    var steeringBehavior: SteeringBehavior<Vector2>? = null

    fun update(deltaTime: Float) {
        steeringBehavior?.calculateSteering(STEERING_OUTPUT)
        applySteering(STEERING_OUTPUT, deltaTime)
    }

    private fun applySteering(steering: SteeringAcceleration<Vector2>, deltaTime: Float) {
        if (!STEERING_OUTPUT.linear.isZero) {
            bodyComponent.body.applyForceToCenter(STEERING_OUTPUT.linear, true)
        }
    }

    override fun getMaxAngularSpeed(): Float {
        return _maxAngularSpeed
    }

    override fun getMaxLinearSpeed(): Float {
        return _maxLinearSpeed
    }

    override fun getAngularVelocity(): Float {
        return _angularVelocity
    }

    override fun getMaxAngularAcceleration(): Float {
        return _maxAngularAcceleration
    }

    override fun getLinearVelocity(): Vector2 {
        return _linearVelocity
    }

    override fun setMaxLinearSpeed(maxLinearSpeed: Float) {
        this._maxLinearSpeed = maxLinearSpeed
    }

    override fun getMaxLinearAcceleration(): Float {
        return _maxLinearAcceleration
    }

    override fun getPosition(): Vector2? {
        return bodyComponent.body.position
    }

    override fun isTagged(): Boolean {
        return tagged
    }

    override fun setMaxLinearAcceleration(maxLinearAcceleration: Float) {
        this._maxLinearAcceleration = maxLinearAcceleration
    }

    override fun setMaxAngularSpeed(maxAngularSpeed: Float) {
        this._maxAngularSpeed = maxAngularSpeed
    }

    override fun setOrientation(orientation: Float) {
    }

    override fun getZeroLinearSpeedThreshold(): Float {
        return 0f
    }

    override fun setZeroLinearSpeedThreshold(value: Float) {
    }

    override fun getOrientation(): Float {
        return 0f
    }

    override fun setMaxAngularAcceleration(maxAngularAcceleration: Float) {
        this._maxAngularAcceleration = maxAngularAcceleration
    }

    override fun angleToVector(outVector: Vector2?, angle: Float): Vector2 {
        return angleMagnitudeToVector(angle)
    }

    override fun vectorToAngle(vector: Vector2?): Float {
        return vector!!.angle()
    }

    override fun getBoundingRadius(): Float {
        return boundingRadius
    }

    override fun newLocation(): Location<Vector2>? {
        return null
    }

    override fun setTagged(tagged: Boolean) {
        this.tagged = tagged
    }

    companion object {
        val STEERING_OUTPUT: SteeringAcceleration<Vector2> = SteeringAcceleration(Vector2())
    }

}