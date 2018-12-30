package com.lastwords.ashley.tiledmap

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.physics.box2d.ChainShape
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.maps.objects.PolylineMapObject
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.maps.objects.PolygonMapObject
import com.sun.awt.SecurityWarning.setPosition
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.maps.objects.CircleMapObject
import javax.swing.Spring.height
import javax.swing.Spring.width
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.sun.deploy.uitoolkit.ToolkitStore.dispose
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.maps.objects.TextureMapObject
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.MapObjects
import com.badlogic.gdx.physics.box2d.World



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
        val TILE_SIZE = 16
    }

}

class TiledMapSystem(private var camera: OrthographicCamera): EntitySystem() {

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

//object MapBodyBuilder {
//
//    // The pixels per tile. If your tiles are 16x16, this is set to 16f
//    private var ppt = 0f
//
//    fun buildShapes(map: Map<*, *>, pixels: Float, world: World): Array<Body> {
//        ppt = pixels
//        val objects = map.getLayers().get("Obstacles").getObjects()
//
//        val bodies = Array<Body>()
//
//        for (`object` in objects) {
//
//            if (`object` is TextureMapObject) {
//                continue
//            }
//
//            val shape: Shape
//
//            shape = if (`object` is RectangleMapObject) {
//                getRectangle(`object` as RectangleMapObject)
//            } else if (`object` is PolygonMapObject) {
//                getPolygon(`object` as PolygonMapObject)
//            } else if (`object` is PolylineMapObject) {
//                getPolyline(`object` as PolylineMapObject)
//            } else if (`object` is CircleMapObject) {
//                getCircle(`object` as CircleMapObject)
//            } else {
//                continue
//            }
//
//            val bd = BodyDef()
//            bd.type = BodyType.StaticBody
//            val body = world.createBody(bd)
//            body.createFixture(shape, 1)
//
//            bodies.add(body)
//
//            shape.dispose()
//        }
//        return bodies
//    }
//
//    private fun getRectangle(rectangleObject: RectangleMapObject): PolygonShape {
//        val rectangle = rectangleObject.rectangle
//        val polygon = PolygonShape()
//        val size = Vector2((rectangle.x + rectangle.width * 0.5f) / ppt,
//                (rectangle.y + rectangle.height * 0.5f) / ppt)
//        polygon.setAsBox(rectangle.width * 0.5f / ppt,
//                rectangle.height * 0.5f / ppt,
//                size,
//                0.0f)
//        return polygon
//    }
//
//    private fun getCircle(circleObject: CircleMapObject): CircleShape {
//        val circle = circleObject.circle
//        val circleShape = CircleShape()
//        circleShape.radius = circle.radius / ppt
//        circleShape.position = Vector2(circle.x / ppt, circle.y / ppt)
//        return circleShape
//    }
//
//    private fun getPolygon(polygonObject: PolygonMapObject): PolygonShape {
//        val polygon = PolygonShape()
//        val vertices = polygonObject.polygon.transformedVertices
//
//        val worldVertices = FloatArray(vertices.size)
//
//        for (i in vertices.indices) {
//            println(vertices[i])
//            worldVertices[i] = vertices[i] / ppt
//        }
//
//        polygon.set(worldVertices)
//        return polygon
//    }
//
//    private fun getPolyline(polylineObject: PolylineMapObject): ChainShape {
//        val vertices = polylineObject.polyline.transformedVertices
//        val worldVertices = arrayOfNulls<Vector2>(vertices.size / 2)
//
//        for (i in 0 until vertices.size / 2) {
//            worldVertices[i] = Vector2()
//            worldVertices[i].x = vertices[i * 2] / ppt
//            worldVertices[i].y = vertices[i * 2 + 1] / ppt
//        }
//
//        val chain = ChainShape()
//        chain.createChain(worldVertices)
//        return chain
//    }
//}