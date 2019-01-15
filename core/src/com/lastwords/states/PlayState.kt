package com.lastwords.states

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
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
import com.lastwords.ashley.position.PositionComponent
import com.lastwords.ashley.spawner.SpawnerSystem
import com.lastwords.ashley.spells.CastSystem
import com.lastwords.ashley.stats.StatsSystem
import com.lastwords.ashley.tiledmap.*
import com.lastwords.ashley.velocity.VelocitySystem
import com.lastwords.ashley.world.CameraSystem
import com.lastwords.ashley.world.WorldSystem
import com.lastwords.entities.AshleyEntity
import com.lastwords.entities.MobOne
import com.lastwords.entities.gui.CastBar
import com.lastwords.entities.gui.EnergyBar
import com.lastwords.entities.gui.HealthPointsBar
import com.lastwords.entities.gui.SpellSelectedBar
import com.lastwords.eventlistener.EventListener
import com.lastwords.eventlistener.LastEventEmitter

class PlayState(
        gameStateManager: GameStateManager,
        override val eventListener: EventListener
): State(gameStateManager), TiledMapState, LastEventEmitter {

    private val ashleyEntity: AshleyEntity
    private val engine: Engine
    private val worldSystem: WorldSystem

    override lateinit var tiledMap: TiledMap
    override lateinit var aiNodes: NodeMap

    private var entityPositions: ImmutableArray<Entity>
    private val positionMapper = ComponentMapper.getFor(PositionComponent::class.java)

    init {
        world = World(Vector2.Zero, true)
        worldSystem = WorldSystem(world!!)
        engine = gameStateManager.engine

        entityPositions = engine.getEntitiesFor(Family.all(PositionComponent::class.java).get())

        /**SYSTEMS**/
        engine.addSystem(TiledMapSystem(this, camera))
        engine.addSystem(worldSystem)
        engine.addSystem(PlayerBehaviourSystem(this))
        engine.addSystem(SpawnerSystem())
        engine.addSystem(CastSystem())
        engine.addSystem(MoveToTargetSystem())
        engine.addSystem(TrackTargetSystem(this, eventListener))
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


        /**ENTITIES**/
        val tiledEntity = Entity()
        tiledMap = TmxMapLoader().load("new_map.tmx")
        aiNodes = tiledMap.createNodeMap("ai_nodes")
        PlayState.tiledMapComponent = TiledMapComponent(tiledMap)
        tiledEntity.add(PlayState.tiledMapComponent)
        engine.addEntity(tiledEntity)
        val entities = tiledMap.getObstacles()
        for (entity in entities) {
            engine.addEntity(entity)
        }

        ashleyEntity = AshleyEntity(64f, 64f, 35f)
        engine.addEntity(ashleyEntity)
        engine.addEntity(HealthPointsBar(ashleyEntity))
        engine.addEntity(CastBar(ashleyEntity))
        engine.addEntity(SpellSelectedBar(ashleyEntity))
        engine.addEntity(EnergyBar(ashleyEntity))
//        engine.addEntity(Spawner(MobOne::class.java, Vector2(32f, 32f), 1f, 0))
        val mobOne = MobOne()
        mobOne.setPosition(Vector2(150f, 150f))
        engine.addEntity(mobOne)
    }

    override fun handleInput() {

    }

    override fun update(dt: Float) {
        engine.update(dt)
    }

    override fun render(spriteBatch: SpriteBatch?) {
        spriteBatch!!.projectionMatrix = camera.combined
        worldSystem.render(box2dCamera.combined)
    }

    override fun dispose() {
        PlayState.world = null
    }

    fun getAllEntityPositions(): MutableList<PositionComponent> {
        val positions = mutableListOf<PositionComponent>()
        for (entity in entityPositions) {
            positions.add(positionMapper.get(entity))
        }
        return positions
    }

    companion object {
        var world: World? = null
        var tiledMapComponent: TiledMapComponent? = null
    }
}
