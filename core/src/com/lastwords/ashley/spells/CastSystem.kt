package com.lastwords.ashley.spells

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.lastwords.ashley.entities.EntityStateComponent
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.ashley.stats.PropertiesComponent
import com.lastwords.entities.Projectile
import com.lastwords.states.State

class CastSystem(private var state: State) : EntitySystem() {

    private lateinit var entities: ImmutableArray<Entity>

    private val keyMap: Array<String> = Array(255) { i -> i.toString() }

    private val castMapper = ComponentMapper.getFor(CastComponent::class.java)
    private val entityStateMapper = ComponentMapper.getFor(EntityStateComponent::class.java)
    private val positionMapper = ComponentMapper.getFor(PositionComponent::class.java)
    private val propertiesMapper = ComponentMapper.getFor(PropertiesComponent::class.java)

    init {
        keyMap[Input.Keys.A] = "A"
        keyMap[Input.Keys.S] = "S"
        keyMap[Input.Keys.D] = "D"
        keyMap[Input.Keys.F] = "F"
    }

    override fun addedToEngine(engine: Engine?) {
        entities = engine!!
                .getEntitiesFor(Family
                        .all(CastComponent::class.java, EntityStateComponent::class.java,
                                PropertiesComponent::class.java, PositionComponent::class.java).get())
    }

    override fun update(deltaTime: Float) {

        for (entity in entities) {
            val entityStateComponent = entityStateMapper.get(entity)
            if (entityStateComponent.castState) {
                val castComponent = castMapper.get(entity)

                if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                    castComponent.castPile.add(Input.Keys.A)
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                    castComponent.castPile.add(Input.Keys.S)
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                    castComponent.castPile.add(Input.Keys.D)
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
                    castComponent.castPile.add(Input.Keys.F)
                }

                if (castComponent.castPile.size > 0) {
                    if (Gdx.input.justTouched()) {
                        val casted = StringBuilder()
                        for (key in castComponent.castPile) {
                            casted.append(keyMap[key])
                        }
                        if (Spells.tryCast(castComponent.castPile).contentEquals(Spells.FIRE_BALL)) {
                            val positionComponent = positionMapper.get(entity)
                            val targetPosition = state.getWorldMousePosition()
                            val originPosition = positionComponent.position.cpy()

                            engine.addEntity(Projectile(originPosition, targetPosition, 30f, propertiesMapper.get(entity).width / 2))
                        } else {
                            castComponent.castPile.removeAt(0)
                        }
                        println(castComponent.castPile)
                        println(casted)
                    }
                }
            }
        }

    }

}
