package com.lastwords.ashley.animation

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion

class AnimationComponent @JvmOverloads constructor(var animation: Animation<TextureRegion>, var animationTime: Float = 0f) : Component
