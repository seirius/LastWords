package com.lastwords.util

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.lastwords.ashley.death.DeathComponent

val deathMapper = ComponentMapper.getFor(DeathComponent::class.java)!!

fun Entity.isAboutToDie(): Boolean {
    return deathMapper.get(this) != null
}