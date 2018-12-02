package com.lastwords.ashley.entities

import com.badlogic.ashley.core.Component

import java.util.ArrayList

class CastComponent : Component {

    var castPile: MutableList<Int> = ArrayList()

}
