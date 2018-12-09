package com.lastwords.ashley.body

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.lastwords.states.PlayState

class BodyComponent(
        entity: Entity,
        initPosition: Vector2,
        bodyType: BodyDef.BodyType,
        shape: Shape?,
        isSensor: Boolean = false
) : Component {

    var body: Body
    private var bodyDef: BodyDef = BodyDef()
    private var fixture: Fixture

    init {
        bodyDef.type = bodyType
        bodyDef.position.set(initPosition.cpy())

        body = PlayState.world!!.createBody(bodyDef)

        val fixtureDef = FixtureDef()
        fixtureDef.shape = shape
        fixtureDef.isSensor = isSensor
        fixtureDef.density = 0.1f

        fixture = body.createFixture(fixtureDef)
        fixture.userData = entity

        shape!!.dispose()
    }

    fun die() {
        PlayState.world?.destroyBody(body)
    }
}

class NoBodyComponent: Component
