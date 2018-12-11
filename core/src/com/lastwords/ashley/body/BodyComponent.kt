package com.lastwords.ashley.body

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.lastwords.ashley.world.ContactComponent
import com.lastwords.ashley.world.ContactImpl
import com.lastwords.states.PlayState

class BodyComponent(
        initPosition: Vector2,
        bodyType: BodyDef.BodyType
) : Component {

    var body: Body
    private var bodyDef: BodyDef = BodyDef()

    init {
        bodyDef.type = bodyType
        bodyDef.position.set(initPosition.cpy())

        body = PlayState.world!!.createBody(bodyDef)
    }

    fun die() {
        PlayState.world?.destroyBody(body)
    }
}

class NoBodyComponent: Component

enum class FixtureType {
    MAIN, SENSOR
}

class FixtureComponent(
        entity: Entity,
        body: Body,
        shape: Shape,
        fixtureType: FixtureType,
        override var contact: ContactImpl? = null
): ContactComponent() {

    private var fixture: Fixture

    init {
        val fixtureDef = FixtureDef()
        fixtureDef.shape = shape
        fixtureDef.isSensor = fixtureType == FixtureType.SENSOR

        fixture = body.createFixture(fixtureDef)
        fixture.userData = entity

        shape.dispose()
    }

}
