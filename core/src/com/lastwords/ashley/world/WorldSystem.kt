package com.lastwords.ashley.world

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.physics.box2d.*
import com.lastwords.ashley.body.BodyComponent
import com.lastwords.ashley.body.ContactSensor
import com.lastwords.ashley.position.PositionComponent

class WorldSystem(private val world: World) : EntitySystem(), ContactListener {

    private val renderer: Box2DDebugRenderer = Box2DDebugRenderer()

    private lateinit var entitiesWithPosition: ImmutableArray<Entity>
    private lateinit var contactEntities: ImmutableArray<Entity>

    private val bodyMapper = ComponentMapper.getFor(BodyComponent::class.java)
    private val positionMapper = ComponentMapper.getFor(PositionComponent::class.java)
    private val contactMapper = ComponentMapper.getFor(ContactComponent::class.java)

    init {
        world.setContactListener(this)
    }

    override fun addedToEngine(engine: Engine?) {
        entitiesWithPosition = engine!!.getEntitiesFor(Family
                .all(BodyComponent::class.java, PositionComponent::class.java).get())

        contactEntities = engine.getEntitiesFor(Family
                .all(ContactComponent::class.java).get())
    }

    override fun update(deltaTime: Float) {

        for (entity in entitiesWithPosition) {
            val bodyComponent = bodyMapper.get(entity)
            val positionComponent = positionMapper.get(entity)
            val bodyPosition = bodyComponent.body.position
            if (bodyPosition != null && bodyPosition != positionComponent.position) {
                positionComponent.position = bodyPosition
            }
        }

        for (entity in contactEntities) {
            val contactComponent = contactMapper.get(entity)
            for (contactSensor in contactComponent.contacts) {
                contactComponent.contact?.contact(entity, contactSensor, engine)
            }
            contactComponent.contacts.clear()
        }

        world.step(deltaTime, 6, 2)
    }

    fun render(combined: Matrix4) {
        renderer.render(world, combined)
    }

    override fun endContact(contact: Contact?) {}

    override fun beginContact(contact: Contact?) {
        val contactSensorA: ContactSensor = contact?.fixtureA?.userData as ContactSensor
        val contactSensorB: ContactSensor = contact.fixtureB?.userData as ContactSensor

        contactMapper.get(contactSensorA.entity)?.contacts?.add(contactSensorB)
        contactMapper.get(contactSensorB.entity)?.contacts?.add(contactSensorA)
    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {}

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {}

}
