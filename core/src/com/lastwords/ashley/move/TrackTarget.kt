package com.lastwords.ashley.move

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.lastwords.LastWords
import com.lastwords.ashley.myashley.LWEntitySystem
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.ashley.stats.StatsComponent
import com.lastwords.ashley.tiledmap.getPath
import com.lastwords.ashley.tiledmap.toJson
import com.lastwords.ashley.velocity.VelocityComponent
import com.lastwords.ashley.velocity.VelocitySystem
import com.lastwords.eventlistener.EventListener
import com.lastwords.eventlistener.EventPayload
import com.lastwords.eventlistener.LastEventEmitter
import com.lastwords.mqtt.LastMqttData
import com.lastwords.states.TiledMapState
import com.lastwords.util.angleMagnitudeToVector
import com.lastwords.util.angleToTarget
import com.lastwords.util.tileNode

class TrackTargetComponent(
        var entity: Entity,
        var aStarUpdate: Float = 0.3f,
        var aStarCounter: Float = 0f,
        var aStar: Boolean = true
): Component

class TrackTargetSystem(
        tiledMapState: TiledMapState,
        override val eventListener: EventListener
): LWEntitySystem(tiledMapState), LastEventEmitter {

    private lateinit var entities: ImmutableArray<Entity>

    private val velocityMapper = ComponentMapper.getFor(VelocityComponent::class.java)
    private val trackTargetMapper = ComponentMapper.getFor(TrackTargetComponent::class.java)
    private val statsMapper = ComponentMapper.getFor(StatsComponent::class.java)
    private val positionMapper = ComponentMapper.getFor(PositionComponent::class.java)

    override fun addedToEngine(engine: Engine?) {
        entities = engine!!.getEntitiesFor(Family
                .all(
                    VelocityComponent::class.java,
                    TrackTargetComponent::class.java,
                    StatsComponent::class.java
                ).get()
        )
    }

    override fun update(deltaTime: Float) {
        for (entity in entities) {
            val trackTargetComponent = trackTargetMapper.get(entity)
            val targetPositionComponent = positionMapper.get(trackTargetComponent.entity)

            if (targetPositionComponent != null) {
                val positionComponent = positionMapper.get(entity)
                val velocityComponent = velocityMapper.get(entity)
                val statsComponent = statsMapper.get(entity)

                if (trackTargetComponent.aStar) {
                    if (trackTargetComponent.aStarCounter < trackTargetComponent.aStarUpdate) {
                        trackTargetComponent.aStarCounter += deltaTime
                    } else {
                        trackTargetComponent.aStarCounter = 0f
                        val path = getPath(tiledMapState.aiNodes,
                                positionComponent.position.tileNode(),
                                targetPositionComponent.position.tileNode())
                        eventListener.emit("entity-path", EventPayload(path))
                        if (path.isNotEmpty()) {
                            var nextNode = path.last()
                            if (nextNode == positionComponent.position.tileNode() && path.size > 1) {
                                nextNode = path[path.size - 2]
                            }
                            val angle = positionComponent.position.angleToTarget(nextNode.centeredPosition())
                            val finalSpeed = statsComponent.speed * VelocitySystem.SPEED_MULTIPLIER
                            velocityComponent.velocity = angleMagnitudeToVector(angle, finalSpeed)
                        }
                    }
                } else {
                    val angle = positionComponent.position.angleToTarget(targetPositionComponent.position)
                    val finalSpeed = statsComponent.speed * VelocitySystem.SPEED_MULTIPLIER

                    velocityComponent.velocity = angleMagnitudeToVector(angle, finalSpeed)
                }
            }
        }
    }

}