package com.lastwords.ashley.stats

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import java.util.*

class StatsComponent : Component {

    val ID: String = UUID.randomUUID().toString()

    var speed: Float = 0f

    var healthPoints: Int = 0

    var energy: Int = 0
    var maxEnergy: Int = 100

    var energyReg: Int = 5


    var attack: Int = 0

    var damageReceived: MutableList<Damage> = mutableListOf()

    companion object {
        val MAPPER: ComponentMapper<StatsComponent>
                = ComponentMapper.getFor(StatsComponent::class.java)
    }

}

class Damage(var amount: Int)
