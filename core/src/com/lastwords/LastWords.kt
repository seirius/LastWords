package com.lastwords

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.lastwords.eventlistener.EventListener
import com.lastwords.eventlistener.EventPayload
import com.lastwords.mqtt.LastMqtt
import com.lastwords.mqtt.LastMqttData
import com.lastwords.mqtt.LastMqttEvent
import com.lastwords.states.GameStateManager
import com.lastwords.states.PlayState

class LastWords : ApplicationAdapter() {

    private lateinit var spriteBatch: SpriteBatch
    private lateinit var gameStateManager: GameStateManager
    private var eventListener = EventListener()
    private lateinit var lastMqtt: LastMqtt

    override fun create() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        spriteBatch = SpriteBatch()
        gameStateManager = GameStateManager(spriteBatch)
        gameStateManager.push(PlayState(gameStateManager, eventListener))
        //        gameStateManager.push(new MenuState(gameStateManager));

        try {
            lastMqtt = LastMqtt(LastWords.MQTT_HOST) {
                if (it) {
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

                    eventListener.on("entity-path") {
                        lastMqtt.emit("entity-path", LastMqttData(it.data))
                    }
                } else {
                    println("Coulnd't connect to MQTT at ${LastWords.MQTT_HOST}")
                }
            }
        } catch(e: Exception) {
            e.printStackTrace()
            println("Coulnd't connect to MQTT at ${LastWords.MQTT_HOST}")
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
