package com.lastwords.ashley.draw

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.ashley.texture.TextureComponent

class DrawSystem(private val spriteBatch: SpriteBatch?) : EntitySystem() {

    private var entities: ImmutableArray<Entity>? = null

    private val positionMapper = ComponentMapper.getFor(PositionComponent::class.java)

    private val textureMapper = ComponentMapper.getFor(TextureComponent::class.java)

    override fun addedToEngine(engine: Engine?) {
        entities = engine!!
                .getEntitiesFor(Family.all(PositionComponent::class.java, TextureComponent::class.java).get())
    }

    override fun update(deltaTime: Float) {
        var textureWidth: Float
        var textureHeight: Float
        var xTexture: Float
        var yTexture: Float
        spriteBatch!!.begin()
        for (entity in entities!!) {
            val positionComponent = positionMapper.get(entity)
            val textureComponent = textureMapper.get(entity)
            val textureRegion = textureComponent.textureRegion
            if (textureRegion != null) {
                textureWidth = textureRegion.regionWidth.toFloat()
                textureHeight = textureRegion.regionHeight.toFloat()
                xTexture = positionComponent.position.x - textureWidth / 2f
                yTexture = positionComponent.position.y - textureHeight / 2f
                spriteBatch
                        .draw(textureRegion, xTexture, yTexture, textureWidth, textureHeight)
            }
        }
        spriteBatch.end()
    }
}
