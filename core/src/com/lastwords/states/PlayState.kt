package com.lastwords.states

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.lastwords.LastWords
import com.lastwords.ashley.animation.AnimationSystem
import com.lastwords.ashley.death.DeathSystem
import com.lastwords.ashley.draw.DrawSystem
import com.lastwords.ashley.entities.CastSystem
import com.lastwords.ashley.deathcondition.DistanceLimitSystem
import com.lastwords.ashley.deathcondition.TimeLimitSystem
import com.lastwords.ashley.move.MoveToTargetSystem
import com.lastwords.ashley.stats.StatsSystem
import com.lastwords.ashley.velocity.InputToVelocitySystem
import com.lastwords.ashley.velocity.VelocitySystem
import com.lastwords.ashley.world.WorldSystem
import com.lastwords.entities.AshleyEntity
import com.lastwords.entities.Player
import com.lastwords.entities.Prometheus

class PlayState(gameStateManager: GameStateManager): State(gameStateManager) {

    private val player: Player? = null
    private val ashleyEntity: AshleyEntity
    private val engine: Engine
    private val worldSystem: WorldSystem

    init {
        camera.setToOrtho(false, LastWords.WIDTH / LastWords.SCALE, LastWords.HEIGHT / LastWords.SCALE)
        worldSystem = WorldSystem()
        engine = gameStateManager.engine
        engine.addSystem(worldSystem)
        engine.addSystem(InputToVelocitySystem())
        engine.addSystem(CastSystem(this))
        engine.addSystem(MoveToTargetSystem())
        engine.addSystem(VelocitySystem())
        engine.addSystem(StatsSystem())
        engine.addSystem(DistanceLimitSystem())
        engine.addSystem(TimeLimitSystem())
        engine.addSystem(DeathSystem())
        engine.addSystem(DrawSystem(gameStateManager.spriteBatch))
        engine.addSystem(AnimationSystem())
        ashleyEntity = AshleyEntity(16f, 16f, 30f)
        engine.addEntity(ashleyEntity)
        engine.addEntity(Prometheus(Vector2(200f, 200f)))
    }

    override fun handleInput() {

    }

    override fun update(dt: Float) {
        engine.update(dt)
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
