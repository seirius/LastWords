package com.lastwords.ashley.entities

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

class CastSystem : EntitySystem() {

    private var entities: ImmutableArray<Entity>? = null

    private val castMapper = ComponentMapper.getFor(CastComponent::class.java)
    private val entityStateMapper = ComponentMapper.getFor(EntityStateComponent::class.java)

    override fun addedToEngine(engine: Engine?) {
        entities = engine!!
                .getEntitiesFor(Family.all(CastComponent::class.java, EntityStateComponent::class.java).get())
    }

    override fun update(deltaTime: Float) {

        for (entity in entities!!) {
            val entityStateComponent = entityStateMapper.get(entity)
            if (entityStateComponent.castState) {
                val castComponent = castMapper.get(entity)

                if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
                    castComponent.castPile.add(Input.Keys.Q)
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
                    castComponent.castPile.add(Input.Keys.W)
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                    castComponent.castPile.add(Input.Keys.E)
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                    castComponent.castPile.add(Input.Keys.R)
                }
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

                if (Gdx.input.justTouched() && castComponent.castPile.size > 0) {
                    val casted = StringBuilder()
                    for (key in castComponent.castPile) {
                        casted.append(key).append(":")
                    }
                    println(casted)
                    castComponent.castPile.clear()
                }
            }
        }

    }

}
