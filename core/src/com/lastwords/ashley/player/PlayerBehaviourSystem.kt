package com.lastwords.ashley.player

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.lastwords.ashley.entities.EntityStateComponent
import com.lastwords.ashley.orders.CastOrderComponent
import com.lastwords.ashley.orders.FireSpellComponent
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.ashley.spells.CastComponent
import com.lastwords.ashley.spells.Spell
import com.lastwords.ashley.spells.TargetComponent
import com.lastwords.ashley.stats.StatsComponent
import com.lastwords.ashley.tiledmap.getNodes
import com.lastwords.ashley.velocity.VelocityComponent
import com.lastwords.states.PlayState
import com.lastwords.states.State
import com.lastwords.util.tileNode

class PlayerBehaviourSystem(private var state: State): EntitySystem() {

    private lateinit var players: ImmutableArray<Entity>

    private val velocityMapper = ComponentMapper.getFor(VelocityComponent::class.java)
    private val statsMapper = ComponentMapper.getFor(StatsComponent::class.java)
    private val entityStateMapper = ComponentMapper.getFor(EntityStateComponent::class.java)
    private val castMapper = ComponentMapper.getFor(CastComponent::class.java)
    private val targetMapper = ComponentMapper.getFor(TargetComponent::class.java)

    private var justTouchedRight = false


    override fun addedToEngine(engine: Engine?) {
        players = engine!!.getEntitiesFor(Family.all(
                PlayerComponent::class.java, EntityStateComponent::class.java,
                VelocityComponent::class.java, StatsComponent::class.java,
                TargetComponent::class.java
        ).get())
    }

    override fun update(deltaTime: Float) {
        val auxRightTouched = Gdx.input.isButtonPressed(Input.Buttons.RIGHT)

        for (entity in players) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                entity.add(CastOrderComponent())
            }

            val targetComponent = targetMapper.get(entity)
            targetComponent.target = state.getWorldMousePosition()

            val stateComponent = entityStateMapper.get(entity)

            if (stateComponent.castState) {
                updateToCast(entity)
            }

            if (Gdx.input.justTouched()) {
                val positionComponent = entity.getComponent(PositionComponent::class.java)
                val tiledMapComponent = PlayState.tiledMapComponent
                if (tiledMapComponent != null && positionComponent != null) {
                    val tiledMap = tiledMapComponent.tiledMap
                    getNodes(tiledMap, positionComponent.position.tileNode(), targetComponent.target.tileNode())
                }
                entity.add(FireSpellComponent(Spell.S1))
            }
            if (!justTouchedRight && auxRightTouched) {
                justTouchedRight = true
                entity.add(FireSpellComponent(Spell.S2))
            } else if (justTouchedRight && !auxRightTouched) {
                justTouchedRight = false
            }

        }
    }

    private fun updateToCast(entity: Entity) {
        val castComponent = castMapper.get(entity)

        if (castComponent != null) {
            if (castComponent.castPile.size > 6) {
                castComponent.castPile.clear()
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
                castComponent.castPile.add(Input.Keys.Q)
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
        }
    }

}