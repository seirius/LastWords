package com.lastwords.ashley.draw

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.states.State
import java.io.File

class TextSystem(private var spriteBatch: SpriteBatch): EntitySystem() {

    companion object {
        val PATH_TO_FONT = "font${File.separator}firacode${File.separator}ttf${File.separator}fira.fnt"
    }


    private lateinit var entities: ImmutableArray<Entity>

    private val textMapper = ComponentMapper.getFor(TextComponent::class.java)
    private val positionMapper = ComponentMapper.getFor(PositionComponent::class.java)

    private val font: BitmapFont = BitmapFont(Gdx.files.internal(TextSystem.PATH_TO_FONT))

    init {
        font.data.setScale(.06f)
    }

    override fun addedToEngine(engine: Engine?) {
        entities = engine!!.getEntitiesFor(Family.all(TextComponent::class.java, PositionComponent::class.java).get())
    }

    override fun update(deltaTime: Float) {
        spriteBatch.begin()
        for (entity in entities) {
            val textComponent = textMapper.get(entity)
            val positionComponent = positionMapper.get(entity)
            font.draw(spriteBatch, textComponent.text, positionComponent.position.x, positionComponent.position.y * State.CURRENT_HEIGHT / State.CURRENT_WIDTH)
        }
        spriteBatch.end()
    }
}

class TextComponent(var text: String): Component

