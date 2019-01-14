package com.lastwords.mqtt

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.eclipse.paho.client.mqttv3.*
import java.util.*

class LastMqtt(
        hostName: String,
        val onConnect: (success: Boolean) -> Unit
) {

//    private val clientId = "unique"
    private val clientId = MqttAsyncClient.generateClientId()
    private val mqttClient: MqttAsyncClient = MqttAsyncClient(hostName, clientId)
    private val objectMapper = ObjectMapper()
    private val topics: HashMap<String, MutableList<LastMqttEvent>> = hashMapOf()

    init {
        objectMapper.registerModule(KotlinModule())

        val mqttOptions = MqttConnectOptions()
        mqttOptions.isCleanSession = true
        mqttOptions.keepAliveInterval = 30

        mqttClient.connect(mqttOptions, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                onConnect(true)
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                onConnect(true)
            }
        })
        mqttClient.setCallback(object : MqttCallback {
            override fun messageArrived(topic: String?, message: MqttMessage?) {
                try {
                    if (message != null) {
                        val payload: LastMqttData = objectMapper.readValue(message.payload, LastMqttData::class.java)
                        val events = topics[topic]
                        if (events != null) {
                            for (i in events.size downTo 1) {
                                events[i - 1].callback(payload)
                            }
                        }
                    }
                } catch(e: Exception) {
                    e.printStackTrace()
                }
            }
            override fun connectionLost(cause: Throwable?) {}
            override fun deliveryComplete(token: IMqttDeliveryToken?) {}
        })
    }

    fun emit(topic: String, payload: LastMqttData) {
        val mqttMsg = MqttMessage()
        mqttMsg.payload = objectMapper.writeValueAsBytes(payload)
        mqttClient.publish(topic, mqttMsg)
    }

    fun on(topic: String, event: LastMqttEvent) {
        this.addListener(topic, event)
    }


    fun get(
            topic: String,
            payload: LastMqttData = LastMqttData(hashMapOf<Any, Any>()),
            event: LastMqttEvent
    ) {
        val requestTopic = "request:$topic"
        payload.requestId = UUID.randomUUID().toString()
        emit(requestTopic, payload)
        var resolveGet: LastMqttEvent? = null
        resolveGet = LastMqttEvent {
            if (it.requestId == payload.requestId) {
                event.callback(it)
                removeListener(topic, resolveGet!!)
            }
        }
        on(topic, resolveGet)
    }

    fun requestMapper(
            topic: String,
            callback: LastMqttEvent
    ) {
        on("request:$topic", LastMqttEvent {
            var payloadResponse = callback.callback(it)
            if (payloadResponse == null) {
                payloadResponse = LastMqttData(hashMapOf<Any, Any>())
            }
            if (payloadResponse is LastMqttData) {
                payloadResponse.requestId = it.requestId
                emit(topic, payloadResponse)
            }
        })
    }


    private fun addListener(topic: String, event: LastMqttEvent) {
        if (topics.containsKey(topic)) {
            topics[topic]?.add(event)
        } else {
            mqttClient.subscribe(topic, 2)
            topics[topic] = mutableListOf(event)
        }
    }

    private fun removeListener(topic: String, event: LastMqttEvent) {
        topics[topic]?.removeIf { topic in topics && it == event }
    }

}

class LastMqttEvent(val callback: (payload: LastMqttData) -> Any?)