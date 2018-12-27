package com.lastwords.ashley.entities

import com.badlogic.ashley.core.Component

class EntityStateComponent : Component {

    var castState: Boolean = false
    var moveDirection: MoveDirection = MoveDirection.DOWN

    fun toggleCastState() {
        castState = !castState
    }

}

enum class MoveDirection {
    UP, RIGHT, DOWN, LEFT
}
