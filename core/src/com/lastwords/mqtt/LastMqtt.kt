package com.lastwords.mqtt

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.eclipse.paho.client.mqttv3.*

class LastMqtt(hostName: String) {

    private val mqttClient: MqttAsyncClient = MqttAsyncClient(hostName, MqttAsyncClient.generateClientId())
    private val objectMapper = ObjectMapper()
    private val topics: HashMap<String, MutableList<LastMqttEvent>> = hashMapOf()

    init {
        objectMapper.registerModule(KotlinModule())

        val mqttOptions = MqttConnectOptions()
        mqttOptions.isCleanSession = true
        mqttOptions.keepAliveInterval = 30

        mqttClient.connect(mqttOptions)
        mqttClient.setCallback(object : MqttCallback {
            override fun messageArrived(topic: String?, message: MqttMessage?) {
                try {
                    if (message != null) {
                        val payload: LastMqttData = objectMapper.readValue(message.payload, LastMqttData::class.java)
                        topics[topic]?.forEach { event -> event.callback(payload) }
                    }
                } catch(e: Exception) {
                    e.printStackTrace()
                }
            }
            override fun connectionLost(cause: Throwable?) {}
            override fun deliveryComplete(token: IMqttDeliveryToken?) {}
        })
    }

    public fun emit(topic: String, payload: LastMqttData) {
        val mqttMsg = MqttMessage()
        mqttMsg.payload = objectMapper.writeValueAsBytes(payload)
        mqttClient.publish(topic, mqttMsg)
    }

    public fun on(topic: String, callback: (payload: LastMqttData) -> Unit) {
        this.addListener(topic, LastMqttEvent(callback))
    }

    private fun addListener(topic: String, event: LastMqttEvent) {
        if (topics.containsKey(topic)) {
            topics[topic]?.add(event)
        } else {
            mqttClient.subscribe(topic, 2)
            topics[topic] = mutableListOf(event)
        }
    }

}

class LastMqttEvent(val callback: (payload: LastMqttData) -> Unit)