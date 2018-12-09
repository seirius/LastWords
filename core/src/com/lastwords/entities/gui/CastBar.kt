package com.lastwords.entities.gui

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.lastwords.ashley.draw.TextGUIComponent
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.states.State

class CastBar(override var trackEntity: Entity): Entity(), GUIEntity {

    init {
        add(TextGUIComponent())
        add(PositionComponent(State.CURRENT_WIDTH / 2 - 20f, 10f))
        add(CastBarComponent())
    }

}

class CastBarComponent: Component