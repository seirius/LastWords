package com.lastwords.entities.gui

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.lastwords.ashley.draw.TextGUIComponent
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.states.State

class HealthPointsBar(override var trackEntity: Entity): Entity(), GUIEntity {

    init {
        add(TextGUIComponent())
        add(PositionComponent(10f, State.CURRENT_HEIGHT))
        add(HealthPointsComponent())
    }

}

class HealthPointsComponent: Component