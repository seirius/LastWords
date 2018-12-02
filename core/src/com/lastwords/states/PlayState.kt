package com.lastwords.states

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.lastwords.LastWords
import com.lastwords.ashley.animation.AnimationSystem
import com.lastwords.ashley.draw.DrawSystem
import com.lastwords.ashley.entities.CastSystem
import com.lastwords.ashley.velocity.InputToVelocitySystem
import com.lastwords.ashley.velocity.VelocitySystem
import com.lastwords.ashley.world.WorldSystem
import com.lastwords.entities.AshleyEntity
import com.lastwords.entities.Player

class PlayState(gameStateManager: GameStateManager) : State(gameStateManager) {

    private val player: Player? = null
    private val ashleyEntity: AshleyEntity
    private val engine: Engine
    private val worldSystem: WorldSystem

    init {
        camera.setToOrtho(false, LastWords.WIDTH / LastWords.SCALE, LastWords.HEIGHT / LastWords.SCALE)
        worldSystem = WorldSystem()
        this.engine = gameStateManager.engine
        this.engine.addSystem(worldSystem)
        this.engine.addSystem(InputToVelocitySystem())
        this.engine.addSystem(CastSystem())
        this.engine.addSystem(DrawSystem(gameStateManager.spriteBatch))
        this.engine.addSystem(VelocitySystem())
        this.engine.addSystem(AnimationSystem())
        ashleyEntity = AshleyEntity(16f, 16f, 30f)
        this.engine.addEntity(ashleyEntity)
    }

    override fun handleInput() {

    }

    override fun update(dt: Float) {
        this.engine.update(dt)
        //        player.update(dt);
    }

    override fun render(spriteBatch: SpriteBatch?) {
        spriteBatch!!.projectionMatrix = camera.combined
        worldSystem.render(camera.combined)
    }

    override fun dispose() {
        player!!.dispose()
    }
}
