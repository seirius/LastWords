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

class ContactSensor(
        var entity: Entity,
        var shape: Shape,
        var fixtureType: FixtureType,
        var contact: ContactImpl? = null,
        var linkedState: Boolean = true
) {

    var fixture: Fixture? = null

}

class ContactRes(
        var thisContactSensor: ContactSensor,
        var contactSensor: ContactSensor
)

class FixtureComponent(
        body: Body,
        contactSensors: List<ContactSensor>
): ContactComponent() {

    init {

        contactSensors.forEach {
            val fixtureDef = FixtureDef()
            fixtureDef.shape = it.shape
            fixtureDef.isSensor = it.fixtureType == FixtureType.SENSOR

            it.fixture = body.createFixture(fixtureDef)
            it.fixture?.userData = it

            it.shape.dispose()
        }

    }

}
