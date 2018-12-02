package com.lastwords.ashley.body

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*

class BodyComponent(
        private var initPosition: Vector2?,
        private var bodyType: BodyDef.BodyType?,
        private var shape: Shape?
) : Component {

    var body: Body? = null
    private var bodyDef: BodyDef? = null
    var fixture: Fixture? = null

    fun initBody(world: World) {
        bodyDef = BodyDef()
        bodyDef!!.type = bodyType
        bodyType = null
        bodyDef!!.position.set(initPosition!!.cpy())
        initPosition = null

        body = world.createBody(bodyDef!!)

        val fixtureDef = FixtureDef()
        fixtureDef.shape = shape
        fixtureDef.density = 0.1f

        fixture = body!!.createFixture(fixtureDef)

        shape!!.dispose()
        shape = null
    }
}
