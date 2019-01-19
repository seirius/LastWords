package com.lastwords.ashley.move

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.lastwords.LastWords
import com.lastwords.ashley.myashley.LWEntitySystem
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.ashley.stats.StatsComponent
import com.lastwords.ashley.tiledmap.TileNode
import com.lastwords.ashley.tiledmap.getPath
import com.lastwords.ashley.tiledmap.toJson
import com.lastwords.ashley.velocity.VelocityComponent
import com.lastwords.ashley.velocity.VelocitySystem
import com.lastwords.eventlistener.EventListener
import com.lastwords.eventlistener.EventPayload
import com.lastwords.eventlistener.LastEventEmitter
import com.lastwords.mqtt.LastMqttData
import com.lastwords.states.TiledMapState
import com.lastwords.util.*

class TrackTargetComponent(
        var entity: Entity,
        var aStarUpdate: Float = 0.3f,
        var aStarCounter: Float = 0f,
        var aStar: Boolean = true,
        var path: Array<TileNode> = arrayOf()
): Component

class TrackTargetSystem(
        tiledMapState: TiledMapState,
        override val eventListener: EventListener
): LWEntitySystem(tiledMapState), LastEventEmitter {

    private lateinit var entities: ImmutableArray<Entity>

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
            val trackTargetComponent = TRACK_TARGET.get(entity)
            val targetPositionComponent = POSITION.get(trackTargetComponent.entity)

            if (targetPositionComponent != null) {
                val positionComponent = POSITION.get(entity)
                val velocityComponent = VELOCITY.get(entity)
                val statsComponent = STATS.get(entity)

                if (trackTargetComponent.aStar) {
                    if (trackTargetComponent.aStarCounter < trackTargetComponent.aStarUpdate) {
                        trackTargetComponent.aStarCounter += deltaTime
                    } else {
                        trackTargetComponent.aStarCounter = 0f
                        trackTargetComponent.path = getPath(tiledMapState.aiNodes,
                                positionComponent.position.tileNode(),
                                targetPositionComponent.position.tileNode())
                        if (trackTargetComponent.path.isNotEmpty()) {
                            var nextNode = trackTargetComponent.path.last()
                            if (nextNode == positionComponent.position.tileNode() && trackTargetComponent.path.size > 1) {
                                nextNode = trackTargetComponent.path[trackTargetComponent.path.size - 2]
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