package com.lastwords.ashley.world

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.lastwords.ashley.body.ContactRes
import com.lastwords.ashley.body.ContactSensor


open class ContactComponent(): Component {

    val contacts: MutableList<ContactRes> = mutableListOf()

    val endContacts: MutableList<ContactRes> = mutableListOf()

}

interface ContactImpl {

    fun contact(thisEntity: Entity, contactSensor: ContactSensor, engine: Engine)

    fun endContact(thisEntity: Entity, contactSensor: ContactSensor, engine: Engine)

}