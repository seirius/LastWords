package com.lastwords.entities.gui

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.lastwords.ashley.draw.TextGUIComponent
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.states.State

class EnergyBar(override var trackEntity: Entity): Entity(), GUIEntity {

    init {
        add(TextGUIComponent())
        add(PositionComponent(State.CURRENT_WIDTH - 80f, State.CURRENT_HEIGHT - 10f))
        add(EnergyBarComponent())
    }

}

class EnergyBarComponent: Component