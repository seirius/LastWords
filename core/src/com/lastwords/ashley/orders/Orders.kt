package com.lastwords.ashley.orders

import com.badlogic.ashley.core.Component
import com.lastwords.ashley.spells.Spell

class CastOrderComponent: Component

class FireSpellComponent(
        var spell: Spell
): Component
