package com.lastwords.ashley.myashley

import com.badlogic.ashley.core.EntitySystem
import com.lastwords.states.TiledMapState

open class LWEntitySystem(
        var tiledState: TiledMapState
): EntitySystem()