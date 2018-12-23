package com.lastwords.ashley.world

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.lastwords.ashley.body.ContactSensor


open class ContactComponent(
        open var contact: ContactImpl? = null
): Component {

    val contacts: MutableList<ContactSensor> = mutableListOf()

}

interface ContactImpl {

    fun contact(thisEntity: Entity, contactSensor: ContactSensor, engine: Engine)

}