package com.lastwords.ashley.spells

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2

class TargetComponent(var target: Vector2 = Vector2.Zero.cpy()): Component