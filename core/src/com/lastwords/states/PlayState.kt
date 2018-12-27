package com.lastwords.states

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.lastwords.LastWords
import com.lastwords.ashley.animation.AnimationSystem
import com.lastwords.ashley.death.DeathSystem
import com.lastwords.ashley.deathcondition.DistanceLimitSystem
import com.lastwords.ashley.deathcondition.TimeLimitSystem
import com.lastwords.ashley.draw.DrawSystem
import com.lastwords.ashley.draw.GUISystem
import com.lastwords.ashley.draw.TextSystem
import com.lastwords.ashley.move.MoveToTargetSystem
import com.lastwords.ashley.move.TrackTargetSystem
import com.lastwords.ashley.player.PlayerBehaviourSystem
import com.lastwords.ashley.spawner.SpawnerSystem
import com.lastwords.ashley.spells.CastSystem
import com.lastwords.ashley.stats.StatsSystem
import com.lastwords.ashley.velocity.VelocitySystem
import com.lastwords.ashley.world.CameraSystem
import com.lastwords.ashley.world.WorldSystem
import com.lastwords.entities.*
import com.lastwords.entities.gui.CastBar
import com.lastwords.entities.gui.EnergyBar
import com.lastwords.entities.gui.HealthPointsBar
import com.lastwords.entities.gui.SpellSelectedBar

class PlayState(gameStateManager: GameStateManager): State(gameStateManager) {

    private val ashleyEntity: AshleyEntity
    private val engine: Engine
    private val worldSystem: WorldSystem

    init {
        world = World(Vector2.Zero, true)
        camera.setToOrtho(false, LastWords.WIDTH / LastWords.SCALE, LastWords.HEIGHT / LastWords.SCALE)
        guiCamera.setToOrtho(false, LastWords.WIDTH / LastWords.SCALE, LastWords.HEIGHT / LastWords.SCALE)
        worldSystem = WorldSystem(world!!)
        engine = gameStateManager.engine
        engine.addSystem(worldSystem)
        engine.addSystem(PlayerBehaviourSystem(this))
        engine.addSystem(SpawnerSystem())
        engine.addSystem(CastSystem())
        engine.addSystem(MoveToTargetSystem())
        engine.addSystem(TrackTargetSystem())
        engine.addSystem(VelocitySystem())
        engine.addSystem(StatsSystem())
        engine.addSystem(DistanceLimitSystem())
        engine.addSystem(TimeLimitSystem())
        engine.addSystem(DeathSystem())
        engine.addSystem(DrawSystem(gameStateManager.spriteBatch))
        engine.addSystem(TextSystem(gameStateManager.spriteBatch))
        engine.addSystem(AnimationSystem())
        engine.addSystem(CameraSystem(camera))
        engine.addSystem(GUISystem(guiCamera, gameStateManager.spriteBatch))
        ashleyEntity = AshleyEntity(16f, 16f, 30f)
        engine.addEntity(ashleyEntity)
//        engine.addEntity(Prometheus(Vector2(200f, 200f)))
        engine.addEntity(HealthPointsBar(ashleyEntity))
        engine.addEntity(CastBar(ashleyEntity))
        engine.addEntity(SpellSelectedBar(ashleyEntity))
        engine.addEntity(EnergyBar(ashleyEntity))
        engine.addEntity(Wall(Vector2(-125f, 0f), WallDirection.HORIZONTAL, 500f))
        engine.addEntity(Wall(Vector2(0f, 125f), WallDirection.VERTICAL, 500f))
        engine.addEntity(Wall(Vector2(125f, 0f), WallDirection.HORIZONTAL, 500f))
        engine.addEntity(Wall(Vector2(0f, -125f), WallDirection.VERTICAL, 500f))
        engine.addEntity(Spawner(MobOne::class.java, Vector2(-220f, -220f), 5f, 0))
        engine.addEntity(Spawner(MobOne::class.java, Vector2(220f, -220f), 5f, 0))
        engine.addEntity(Spawner(MobOne::class.java, Vector2(-220f, 220f), 5f, 0))
        engine.addEntity(Spawner(MobOne::class.java, Vector2(220f, 220f), 5f, 0))
    }

    override fun handleInput() {

    }

    override fun update(dt: Float) {
        engine.update(dt)
    }

    override fun render(spriteBatch: SpriteBatch?) {
        spriteBatch!!.projectionMatrix = camera.combined
        worldSystem.render(camera.combined)
    }

    override fun dispose() {
        PlayState.world = null
    }

    companion object {
        var world: World? = null
    }
}
