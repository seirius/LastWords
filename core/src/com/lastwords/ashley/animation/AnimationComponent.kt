package com.lastwords.ashley.animation

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion

class AnimationComponent(
        var animationStill: Animation<TextureRegion>,
        var animationWalkLeft: Animation<TextureRegion>,
        var animationWalkRight: Animation<TextureRegion>,
        var currentAnimation: Animation<TextureRegion>? = null,
        var animationTime: Float = 0f
) : Component
