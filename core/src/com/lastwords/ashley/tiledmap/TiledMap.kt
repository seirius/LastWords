package com.lastwords.ashley.tiledmap

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.fasterxml.jackson.annotation.JsonProperty
import com.lastwords.ashley.myashley.LWEntitySystem
import com.lastwords.states.TiledMapState


class TiledMapComponent(val tiledMap: TiledMap): Component {

    var tiledMapRenderer: OrthogonalTiledMapRenderer = OrthogonalTiledMapRenderer(tiledMap)

    companion object {
        const val TILE_SIZE = 16
        const val HALF_TILE_SIZE = TILE_SIZE / 2
    }

}

class TiledMapSystem(
        tiledMapState: TiledMapState,
        private var camera: OrthographicCamera
): LWEntitySystem(tiledMapState) {

    private lateinit var entities: ImmutableArray<Entity>

    private val tiledMapMapper = ComponentMapper.getFor(TiledMapComponent::class.java)

    override fun update(deltaTime: Float) {
        for (entity in entities) {
            val tiledMapComponent = tiledMapMapper.get(entity)
            tiledMapComponent.tiledMapRenderer.setView(camera)
            tiledMapComponent.tiledMapRenderer.render()
        }
    }

    override fun addedToEngine(engine: Engine?) {
        entities = engine!!.getEntitiesFor(Family.all(TiledMapComponent::class.java).get())
    }
}

enum class TileType {

    @JsonProperty("NOT_DEFINED")
    NOT_DEFINED,
    @JsonProperty("FLOOR")
    FLOOR,
    @JsonProperty("WALL")
    WALL;

}