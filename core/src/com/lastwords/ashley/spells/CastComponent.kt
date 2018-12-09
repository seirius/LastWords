package com.lastwords.ashley.spells

import com.badlogic.ashley.core.Component

import java.util.ArrayList

enum class Spell {
    S1, S2
}

class CastComponent : Component {

    var castPile: MutableList<Int> = ArrayList()

    val spells: HashMap<Spell, SpellTypes> = hashMapOf()

}

enum class SpellTypes(val value: IntArray) {
    FIRE_BALL(intArrayOf(45, 34))
}

object Spells {
    fun tryCast(pile: MutableList<Int>, cleanAfter: Boolean = true): SpellTypes? {
        for (key in (0 until pile.size)) {
            val preSpell: IntArray = pile.slice(0..key).toIntArray()
            if (preSpell.contentEquals(SpellTypes.FIRE_BALL.value)) {
                if (cleanAfter) {
                    pile.clear()
                }
                return SpellTypes.FIRE_BALL
            }
        }

        if (cleanAfter) {
            pile.clear()
        }

        return null
    }
}
