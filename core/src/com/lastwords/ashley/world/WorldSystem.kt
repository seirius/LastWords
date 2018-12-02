package com.lastwords.ashley.world

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.lastwords.ashley.body.BodyComponent
import com.lastwords.ashley.position.PositionComponent

class WorldSystem : EntitySystem() {

    val world: World = World(Vector2.Zero, true)
    private val renderer: Box2DDebugRenderer = Box2DDebugRenderer()

    private var entitiesToAdd: ImmutableArray<Entity>? = null
    private var entitiesWithPosition: ImmutableArray<Entity>? = null

    private val bodyMapper = ComponentMapper.getFor(BodyComponent::class.java)
    private val positionMapper = ComponentMapper.getFor(PositionComponent::class.java)

    override fun addedToEngine(engine: Engine?) {
        entitiesToAdd = engine!!.getEntitiesFor(Family
                .all(BodyComponent::class.java, AddToWorldComponent::class.java).get())

        entitiesWithPosition = engine.getEntitiesFor(Family
                .all(BodyComponent::class.java, PositionComponent::class.java).get())
    }

    override fun update(deltaTime: Float) {

        for (entity in entitiesToAdd!!) {
            val bodyComponent = bodyMapper.get(entity)
            bodyComponent.initBody(world)
            entity.remove(AddToWorldComponent::class.java)
        }

        for (entity in entitiesWithPosition!!) {
            val bodyComponent = bodyMapper.get(entity)
            val positionComponent = positionMapper.get(entity)
            val bodyPosition = bodyComponent.body?.position
            if (bodyPosition != null && bodyPosition != positionComponent.position) {
                positionComponent.position = bodyPosition
            }
        }

        world.step(deltaTime, 6, 2)
    }

    fun render(combined: Matrix4) {
        renderer.render(world, combined)
    }
}
