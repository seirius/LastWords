package com.lastwords.ashley.body

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*

class BodyComponent(
        private var entity: Entity,
        private var initPosition: Vector2?,
        private var bodyType: BodyDef.BodyType?,
        private var shape: Shape?,
        private var isSensor: Boolean = false
) : Component {

    var body: Body? = null
    private lateinit var bodyDef: BodyDef
    private lateinit var fixture: Fixture
    private lateinit var world: World

    fun initBody(world: World) {
        this.world = world
        bodyDef = BodyDef()
        bodyDef.type = bodyType
        bodyType = null
        bodyDef.position.set(initPosition!!.cpy())
        initPosition = null

        body = world.createBody(bodyDef)

        val fixtureDef = FixtureDef()
        fixtureDef.shape = shape
        fixtureDef.isSensor = isSensor
        fixtureDef.density = 0.1f

        fixture = body!!.createFixture(fixtureDef)
        fixture.userData = entity

        shape!!.dispose()
        shape = null
    }

    fun die() {
        world.destroyBody(body)
    }
}
