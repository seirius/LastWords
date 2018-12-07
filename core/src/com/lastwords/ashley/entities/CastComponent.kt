package com.lastwords.ashley.entities

import com.badlogic.ashley.core.Component

import java.util.ArrayList

class CastComponent : Component {

    var castPile: MutableList<Int> = ArrayList()

}

object Spells {
    val FIRE_BALL = intArrayOf(34, 34)

    fun tryCast(pile: MutableList<Int>): IntArray {
        for (key in (0 until pile.size)) {
            val preSpell: IntArray = pile.slice(0..key).toIntArray()
            if (preSpell.contentEquals(FIRE_BALL)) {
                for (index in (0..key)) {
                    pile.removeAt(0)
                }
                return FIRE_BALL
            }
        }

        return intArrayOf()
    }
}
