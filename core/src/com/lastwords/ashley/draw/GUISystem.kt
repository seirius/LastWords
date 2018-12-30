package com.lastwords.ashley.draw

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.lastwords.ashley.entities.EntityStateComponent
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.ashley.spells.CastComponent
import com.lastwords.ashley.spells.CastSystem
import com.lastwords.ashley.spells.Spell
import com.lastwords.ashley.stats.StatsComponent
import com.lastwords.entities.gui.*
import com.lastwords.states.State

class GUISystem(
        private var guiCamera: OrthographicCamera,
        private var spriteBatch: SpriteBatch
): EntitySystem() {

    private lateinit var entities: ImmutableArray<Entity>
    private lateinit var hpEntities: ImmutableArray<Entity>
    private lateinit var castEntities: ImmutableArray<Entity>
    private lateinit var spellEntities: ImmutableArray<Entity>
    private lateinit var energyEntities: ImmutableArray<Entity>

    private val textMapper = ComponentMapper.getFor(TextGUIComponent::class.java)
    private val positionMapper = ComponentMapper.getFor(PositionComponent::class.java)

    private val statsMapper = ComponentMapper.getFor(StatsComponent::class.java)
    private val textGUIMapper = ComponentMapper.getFor(TextGUIComponent::class.java)
    private val entityStateMapper = ComponentMapper.getFor(EntityStateComponent::class.java)

    private val castMapper = ComponentMapper.getFor(CastComponent::class.java)

    private val font: BitmapFont = BitmapFont(Gdx.files.internal(TextSystem.PATH_TO_FONT))

    init {
        font.data.setScale(.06f)
    }

    override fun addedToEngine(engine: Engine?) {
        entities = engine!!.getEntitiesFor(Family.all(
                TextGUIComponent::class.java, PositionComponent::class.java
        ).get())

        hpEntities = engine.getEntitiesFor(Family.all(
                HealthPointsComponent::class.java, TextGUIComponent::class.java
        ).get())

        castEntities = engine.getEntitiesFor(Family.all(
                CastBarComponent::class.java, TextGUIComponent::class.java
        ).get())

        spellEntities = engine.getEntitiesFor(Family.all(
                SpellSelectedBarComponent::class.java, TextGUIComponent::class.java
        ).get())

        energyEntities = engine.getEntitiesFor(Family.all(
                EnergyBarComponent::class.java, TextGUIComponent::class.java
        ).get())
    }

    override fun update(deltaTime: Float) {
        for (entity in hpEntities) {
            if (entity is GUIEntity) {
                val statsComponent = statsMapper.get(entity.trackEntity)
                positionMapper.get(entity).position.y = State.CURRENT_HEIGHT - 10f
                if (statsComponent != null) {
                    textGUIMapper.get(entity).text = "HP ${statsComponent.healthPoints}"
                }
            }
        }

        for (entity in castEntities) {
            if (entity is GUIEntity) {
                val entityStateComponent = entityStateMapper.get(entity.trackEntity)
                if (entityStateComponent != null) {
                    val textGUIComponent = textGUIMapper.get(entity)
                    if (entityStateComponent.castState) {
                        val castComponent = castMapper.get(entity.trackEntity)
                        positionMapper.get(entity).position.x = State.CURRENT_WIDTH / 2 - 20f
                        if (!castComponent.castPile.isEmpty()) {
                            val castPileInd = StringBuilder()
                            for (key in castComponent.castPile) {
                                castPileInd.append(CastSystem.SPELL_KEY_MAP[key])
                            }
                            textGUIComponent.text = "[$castPileInd]"
                        } else {
                            textGUIComponent.text = "[]"
                        }
                    } else {
                        textGUIComponent.text = ""
                    }
                }
            }
        }

        for (entity in spellEntities) {
            if (entity is GUIEntity) {
                val castComponent = castMapper.get(entity.trackEntity)
                if (castComponent != null) {
                    positionMapper.get(entity).position.x = State.CURRENT_WIDTH - 60f
                    val text = StringBuilder()
                    for (spell in Spell.values()) {
                        text.append("[${spell.name}:${castComponent.spells[spell]?.name}]\n")
                    }
                    textGUIMapper.get(entity).text = text.toString()
                }
            }
        }

        for (entity in energyEntities) {
            if (entity is GUIEntity) {
                val statsComponent = statsMapper.get(entity.trackEntity)
                if (statsComponent != null) {
                    val positionComponent = positionMapper.get(entity)
                    positionComponent.position.x = State.CURRENT_WIDTH - 50f
                    positionComponent.position.y = State.CURRENT_HEIGHT - 10f

                    textGUIMapper.get(entity).text = "ENERGY: ${statsComponent.energy}"
                }
            }
        }

        spriteBatch.projectionMatrix = guiCamera.combined
        spriteBatch.begin()
        for (entity in entities) {
            val textComponent = textMapper.get(entity)
            val positionComponent = positionMapper.get(entity)
            font.draw(spriteBatch, textComponent.text, positionComponent.position.x, positionComponent.position.y)
        }
        spriteBatch.end()
    }

}

class TextGUIComponent(var text: String = ""): Component