package com.lastwords.ashley.texture

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.TextureRegion

class TextureComponent(
        var textureRegion: TextureRegion? = null,
        var xOffset: Float = 0f,
        var yOffset: Float = 0f
) : Component
