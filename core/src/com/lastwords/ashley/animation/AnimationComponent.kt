package com.lastwords.ashley.animation

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import java.lang.IllegalArgumentException

class AnimationComponent : Component {

    lateinit var animationIdle: Animation<TextureRegion>
    lateinit var animationWalkLeft: Animation<TextureRegion>
    lateinit var animationWalkRight: Animation<TextureRegion>
    var currentAnimation: Animation<TextureRegion>? = null
    var animationTime: Float = 0f

    fun generateRegions(
            texture: Texture? = null,
            path: String? = null,
            rows: Int,
            cols: Int,
            chunks: Array<AnimationChunk>
    ) {
        if (texture == null && path == null) {
            throw IllegalArgumentException("Mate, what?. Path AND Texture are null.")
        }

        val tex: Texture = texture?: Texture(path) ?: texture!!

        val textureRegions = TextureRegion.split(tex, tex.width / cols, tex.height / rows)

        for (chunk in chunks) {
            val regions = mutableListOf<TextureRegion>()
            for (index in chunk.indexes) {
                regions.add(textureRegions[index / cols][index % cols])
            }
            chunk.regions = regions.toTypedArray()

            when (chunk.type) {
                AnimationType.WALK_RIGHT -> this.animationWalkRight = Animation(chunk.frameDuration, *chunk.regions!!)
                AnimationType.WALK_LEFT -> this.animationWalkLeft = Animation(chunk.frameDuration, *chunk.regions!!)
                AnimationType.IDLE -> {
                    this.animationIdle = Animation(chunk.frameDuration, *chunk.regions!!)
                    this.currentAnimation = this.animationIdle
                }
            }
        }
    }

}

enum class AnimationType {
    WALK_LEFT, WALK_RIGHT, IDLE
}

class AnimationChunk(
        val type: AnimationType,
        val frameDuration: Float,
        val indexes: Array<Int>
) {
    var regions: Array<TextureRegion>? = null

}
