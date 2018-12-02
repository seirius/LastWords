package com.lastwords.ashley.move

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2

class ToTargetComponent(
        public var targetPosition: Vector2
): Component