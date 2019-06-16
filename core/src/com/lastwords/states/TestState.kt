package com.lastwords.states

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.lastwords.ashley.draw.DrawSystem
import com.lastwords.ashley.draw.TextSystem
import com.lastwords.ashley.player.PlayerMovementSystem
import com.lastwords.ashley.tiledmap.NodeMap
import com.lastwords.ashley.tiledmap.TiledMapComponent
import com.lastwords.ashley.tiledmap.TiledMapSystem
import com.lastwords.ashley.tiledmap.createNodeMap
import com.lastwords.ashley.velocity.VelocitySystem
import com.lastwords.ashley.world.CameraSystem
import com.lastwords.ashley.world.WorldSystem
import com.lastwords.entities.TestPlayer
import com.lastwords.entities.gui.EnergyBar
import com.lastwords.entities.gui.HealthPointsBar
import com.lastwords.eventlistener.EventListener
import com.lastwords.eventlistener.LastEventEmitter

class TestState(
        gameStateManager: GameStateManager,
        override val eventListener: EventListener
): State(gameStateManager), TiledMapState, LastEventEmitter {

    override var tiledMap: TiledMap = TmxMapLoader().load("test.tmx")
    override lateinit var aiNodes: NodeMap

    private val engine: Engine
    private val worldSystem: WorldSystem

    init {
        PlayState.world = World(Vector2.Zero, true)
        worldSystem = WorldSystem(PlayState.world!!)
        engine = gameStateManager.engine

        engine.addSystem(TiledMapSystem(this, camera))
        engine.addSystem(worldSystem)
        engine.addSystem(PlayerMovementSystem())
        engine.addSystem(CameraSystem(arrayOf(camera, box2dCamera)))
        engine.addSystem(DrawSystem(gameStateManager.spriteBatch))
        engine.addSystem(TextSystem(gameStateManager.spriteBatch))
        engine.addSystem(VelocitySystem())

        aiNodes = tiledMap.createNodeMap("ai_nodes")
        val tiledEntity = Entity()
        tiledEntity.add(TiledMapComponent(tiledMap))

        engine.addEntity(tiledEntity)

        val testPlayer = TestPlayer()
        engine.addEntity(testPlayer)
        engine.addEntity(EnergyBar(testPlayer))
        engine.addEntity(HealthPointsBar(testPlayer))
    }

    override fun update(dt: Float) {
        engine.update(dt)
    }

    override fun render(spriteBatch: SpriteBatch?) {
        spriteBatch!!.projectionMatrix = camera.combined
        worldSystem.render(box2dCamera.combined)
    }

    override fun dispose() {
    }

    override fun handleInput() {
    }


}