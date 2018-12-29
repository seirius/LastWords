package com.lastwords.ashley.world

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector3
import com.lastwords.ashley.player.PlayerComponent
import com.lastwords.ashley.position.PositionComponent

class CameraSystem(private val cameras: Array<OrthographicCamera>): EntitySystem() {

    private lateinit var entities: ImmutableArray<Entity>


    private val positionMapper = ComponentMapper.getFor(PositionComponent::class.java)

    override fun addedToEngine(engine: Engine?) {
        entities = engine!!.getEntitiesFor(Family.all(
            PlayerComponent::class.java, PositionComponent::class.java
        ).get())
    }

    override fun update(deltaTime: Float) {
        for (entity in entities) {
            val position = positionMapper.get(entity).position
            for (camera in cameras) {
                camera.position.set(Vector3(position.x, position.y, 0f))
                camera.update()
            }
        }
    }

}