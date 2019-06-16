package com.lastwords

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.lastwords.ashley.move.TrackTargetComponent
import com.lastwords.entities.MobOne
import com.lastwords.eventlistener.EventListener
import com.lastwords.mqtt.LastMqtt
import com.lastwords.mqtt.LastMqttData
import com.lastwords.mqtt.LastMqttEvent
import com.lastwords.states.GameStateManager
import com.lastwords.states.PlayState
import com.lastwords.states.TestState
import com.lastwords.util.STATS

class LastWords : ApplicationAdapter() {

    private lateinit var spriteBatch: SpriteBatch
    private lateinit var gameStateManager: GameStateManager
    private var eventListener = EventListener()
    private lateinit var lastMqtt: LastMqtt

    override fun create() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        spriteBatch = SpriteBatch()
        gameStateManager = GameStateManager(spriteBatch)
//        gameStateManager.push(PlayState(gameStateManager, eventListener))
        gameStateManager.push(TestState(gameStateManager, eventListener))
        //        gameStateManager.push(new MenuState(gameStateManager));

        val mobOneEntities: ImmutableArray<Entity> = gameStateManager.engine
                .getEntitiesFor(Family.all(TrackTargetComponent::class.java).get())

        try {
            lastMqtt = LastMqtt(MQTT_HOST) {
                if (it) {
                    lastMqtt.requestMapper("entities/mobone-list", LastMqttEvent {
                        return@LastMqttEvent LastMqttData(mobOneEntities.map { entity -> STATS[entity].ID })
                    })

                    lastMqtt.requestMapper("get-node-map", LastMqttEvent {
                        for (i in (0 until gameStateManager.states.size)) {
                            val state = gameStateManager.states[i]
                            if (state is PlayState) {
                                return@LastMqttEvent LastMqttData(state.aiNodes)
                            }
                        }
                    })

                    lastMqtt.requestMapper("get-entity-list", LastMqttEvent {
                        for (i in (0 until gameStateManager.states.size)) {
                            val state = gameStateManager.states[i]
                            if (state is PlayState) {
                                return@LastMqttEvent LastMqttData(state.getAllEntityPositions())
                            }
                        }
                    })

                    gameStateManager.engine.addEntityListener(object : EntityListener {
                        override fun entityRemoved(entity: Entity?) {
                            if (entity is MobOne) {
                                lastMqtt.emit("entities/mob-one/removed", LastMqttData(STATS[entity].ID))
                            }
                        }

                        override fun entityAdded(entity: Entity?) {
                            if (entity is MobOne) {
                                lastMqtt.emit("entities/mob-one/added", LastMqttData(STATS[entity].ID))
                            }
                        }

                    })

                    eventListener.on("entity-path") { it1 ->
                        lastMqtt.emit("entity-path", LastMqttData(it1.data))
                    }
                } else {
                    println("Couldn't connect to MQTT at $MQTT_HOST")
                }
            }
        } catch(e: Exception) {
            e.printStackTrace()
            println("Couldn't connect to MQTT at $MQTT_HOST")
        }
    }

    override fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        gameStateManager.update(Gdx.graphics.deltaTime)
        gameStateManager.render(spriteBatch)
    }

    override fun resize(width: Int, height: Int) {
        gameStateManager.resize(width, height)
    }

    companion object {
        const val TITLE = "LastWords"
        const val WIDTH = 1200
        const val HEIGHT = 720
        const val SCALE = 3f
        const val DEBUG_BOX2D = true
//        const val METER_TO_PIXEL = 1f
        const val METER_TO_PIXEL = 32f
//        const val PIXEL_TO_METER = 1f
        const val PIXEL_TO_METER = .03125f
        const val MQTT_HOST = "tcp://localhost:1883"
    }
}
