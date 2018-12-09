package com.lastwords.ashley.stats

import com.badlogic.ashley.core.Component

class StatsComponent : Component {

    var speed: Float = 0f

    var healthPoints: Int = 0

    var energy: Int = 0
    var maxEnergy: Int = 100

    var energyReg: Int = 5


    var attack: Int = 0

    var damageReceived: MutableList<Damage> = mutableListOf()

}

class Damage(var amount: Int)
