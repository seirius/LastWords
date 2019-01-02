package com.lastwords.ashley.player

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.lastwords.ashley.entities.EntityStateComponent
import com.lastwords.ashley.orders.CastOrderComponent
import com.lastwords.ashley.orders.FireSpellComponent
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.ashley.spells.CastComponent
import com.lastwords.ashley.spells.Spell
import com.lastwords.ashley.spells.TargetComponent
import com.lastwords.ashley.stats.StatsComponent
import com.lastwords.ashley.tiledmap.TileNode
import com.lastwords.ashley.tiledmap.TiledMapComponent
import com.lastwords.ashley.tiledmap.getNodes
import com.lastwords.ashley.velocity.VelocityComponent
import com.lastwords.ashley.velocity.VelocitySystem
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

            updateToMove(entity, stateComponent.castState)
            if (stateComponent.castState) {
                updateToCast(entity)
            }

            if (Gdx.input.justTouched()) {
                val positionComponent = entity.getComponent(PositionComponent::class.java)
                val tiledMapComponent = PlayState.tiledMapComponent
                if (tiledMapComponent != null && positionComponent != null) {
                    val tiledMap = tiledMapComponent.tiledMap
                    getNodes(tiledMap, positionComponent.position.tileNode(), targetComponent.target.tileNode())
                    val blocked = tiledMapComponent.isCellBlockedCoord(
                            positionComponent.position.x,
                            positionComponent.position.y
                    )
                    println("is blocked $blocked at position ${positionComponent.position}")

                    val aiNode = tiledMap.layers["ai_nodes"] as TiledMapTileLayer
                    val cell = aiNode.getCell(
                            (targetComponent.target.x / TiledMapComponent.TILE_SIZE).toInt(),
                            (targetComponent.target.y / TiledMapComponent.TILE_SIZE).toInt()
                    )

                    if (cell != null) {
                        println(cell.tile.properties["type"])
                    }
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

    private fun updateToMove(entity: Entity, stop: Boolean = false) {
        val velocityComponent = velocityMapper.get(entity)
        val statsComponent = statsMapper.get(entity)

        val total = Vector2.Zero.cpy()

        if (!stop) {
            val finalSpeed = statsComponent.speed * VelocitySystem.SPEED_MULTIPLIER

            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                total.add(PlayerBehaviourSystem.RIGHT_V.cpy().scl(finalSpeed))
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                total.add(PlayerBehaviourSystem.UP_V.cpy().scl(finalSpeed))
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                total.add(PlayerBehaviourSystem.DOWN_V.cpy().scl(finalSpeed))
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                total.add(PlayerBehaviourSystem.LEFT_V.cpy().scl(finalSpeed))
            }
        }

        velocityComponent.velocity = total
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

    companion object {

        private const val RAD_ANGLE_RIGHT = 0f
        private const val RAD_ANGLE_DOWN = MathUtils.degreesToRadians * 270f
        private const val RAD_ANGLE_LEFT = MathUtils.degreesToRadians * 180f
        private const val RAD_ANGLE_UP = MathUtils.degreesToRadians * 90f

        private val RIGHT_V = Vector2(MathUtils.cos(RAD_ANGLE_RIGHT), MathUtils.sin(RAD_ANGLE_RIGHT))
        private val DOWN_V = Vector2(MathUtils.cos(RAD_ANGLE_DOWN), MathUtils.sin(RAD_ANGLE_DOWN))
        private val UP_V = Vector2(MathUtils.cos(RAD_ANGLE_UP), MathUtils.sin(RAD_ANGLE_UP))
        private val LEFT_V = Vector2(MathUtils.cos(RAD_ANGLE_LEFT), MathUtils.sin(RAD_ANGLE_LEFT))
    }

}