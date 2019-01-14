package com.lastwords.eventlistener

class EventListener {

    private val events: HashMap<String, MutableList<LastEvent>> = hashMapOf()

    fun emit(event: String, payload: EventPayload) {
        if (event in events) {
            events[event]!!.forEach { it.event(payload) }
        }
    }

    fun on(event: String, callback: (payload: EventPayload) -> Unit) {
        var allEvents = events[event]
        if (allEvents == null) {
            allEvents = mutableListOf()
            events[event] = allEvents
        }
        allEvents.add(LastEvent(callback))
    }

}

class EventPayload(var data: Any)
class LastEvent(val event: (payload: EventPayload) -> Unit)

interface LastEventEmitter {
    val eventListener: EventListener
}

