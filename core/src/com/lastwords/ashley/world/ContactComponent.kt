package com.lastwords.ashley.world

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity


open class ContactComponent(
        open var contact: ContactImpl? = null
): Component {

    val contacts: MutableList<Entity> = mutableListOf()

}

interface ContactImpl {

    fun contact(thisEntity: Entity, entity: Entity, engine: Engine)

}