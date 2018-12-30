package com.lastwords.states

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.lastwords.LastWords
import com.lastwords.ashley.animation.AnimationSystem
import com.lastwords.ashley.animation.AshleyEntityAnimationSystem
import com.lastwords.ashley.death.DeathSystem
import com.lastwords.ashley.deathcondition.DistanceLimitSystem
import com.lastwords.ashley.deathcondition.TimeLimitSystem
import com.lastwords.ashley.draw.DrawSystem
import com.lastwords.ashley.draw.GUISystem
import com.lastwords.ashley.draw.TextSystem
import com.lastwords.ashley.entities.EntityStateSystem
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

    private var tiledMap: TiledMap = TmxMapLoader().load("new_map.tmx")
    private var tiledMapRenderer: OrthogonalTiledMapRenderer

    init {
        tiledMapRenderer = OrthogonalTiledMapRenderer(tiledMap)

        world = World(Vector2.Zero, true)
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
        engine.addSystem(EntityStateSystem())
        engine.addSystem(DrawSystem(gameStateManager.spriteBatch))
        engine.addSystem(TextSystem(gameStateManager.spriteBatch))
        engine.addSystem(AshleyEntityAnimationSystem())
        engine.addSystem(AnimationSystem())
        engine.addSystem(GUISystem(guiCamera, gameStateManager.spriteBatch))
        engine.addSystem(CameraSystem(arrayOf(camera, box2dCamera)))
        ashleyEntity = AshleyEntity(64f, 64f, 35f)
        engine.addEntity(ashleyEntity)
//        engine.addEntity(Prometheus(Vector2(200f, 200f)))
        engine.addEntity(HealthPointsBar(ashleyEntity))
        engine.addEntity(CastBar(ashleyEntity))
        engine.addEntity(SpellSelectedBar(ashleyEntity))
        engine.addEntity(EnergyBar(ashleyEntity))
        engine.addEntity(Wall(Vector2(2f, 64f), WallDirection.HORIZONTAL, 216f))
        engine.addEntity(Wall(Vector2(126f, 64f), WallDirection.HORIZONTAL, 216f))
        engine.addEntity(Wall(Vector2(64f, 10f), WallDirection.VERTICAL, 248f))
        engine.addEntity(Wall(Vector2(64f, 118f), WallDirection.VERTICAL, 248f))
//        engine.addEntity(Spawner(MobOne::class.java, Vector2(32f, 32f), 2f, 0))
        val mobOne = MobOne()
        mobOne.setPosition(Vector2(100f, 100f))
        engine.addEntity(mobOne)
//        engine.addEntity(Spawner(MobOne::class.java, Vector2(220f, -220f), 1f, 0))
//        engine.addEntity(Spawner(MobOne::class.java, Vector2(-220f, 220f), 1f, 0))
//        engine.addEntity(Spawner(MobOne::class.java, Vector2(220f, 220f), 1f, 0))
    }

    override fun handleInput() {

    }

    override fun update(dt: Float) {
        tiledMapRenderer.setView(camera)
        tiledMapRenderer.render()
        engine.update(dt)
    }

    override fun render(spriteBatch: SpriteBatch?) {
        spriteBatch!!.projectionMatrix = camera.combined
        worldSystem.render(box2dCamera.combined)
    }

    override fun dispose() {
        PlayState.world = null
    }

    companion object {
        var world: World? = null
    }
}
