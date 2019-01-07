package com.lastwords.ashley.tiledmap

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.lastwords.ashley.myashley.LWEntitySystem
import com.lastwords.states.TiledMapState


class TiledMapComponent(val tiledMap: TiledMap): Component {

    var tiledMapRenderer: OrthogonalTiledMapRenderer = OrthogonalTiledMapRenderer(tiledMap)

    fun isCellBlockedCoord(x: Float, y: Float): Boolean {
        return isCellBlocked((x / TILE_SIZE).toInt(), (y / TILE_SIZE).toInt())
    }

    fun isCellBlocked(x: Int, y: Int): Boolean {
        for (layer in tiledMap.layers) {
            if (layer is TiledMapTileLayer) {
                val cell = layer.getCell(x, y)
                if (cell != null) {
                    val type = cell.tile.properties["type"]
                    if (type != null && TileType.valueOf(type as String) == TileType.WALL) {
                        println("cell x: ${cell.tile.offsetX}, y: ${cell.tile.offsetY}")
                        return true
                    }
                }
            }
        }
        return false
    }

    companion object {
        const val TILE_SIZE = 16
        const val HALF_TILE_SIZE = TILE_SIZE / 2
    }

}

class TiledMapSystem(private var tiledMapState: TiledMapState, private var camera: OrthographicCamera): LWEntitySystem(tiledMapState) {

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

    NOT_DEFINED, FLOOR, WALL;

}