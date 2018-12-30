package com.lastwords.ashley.tiledmap

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.maps.objects.CircleMapObject
import com.badlogic.gdx.maps.objects.EllipseMapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.objects.TextureMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.Shape
import com.lastwords.LastWords
import com.lastwords.ashley.body.BodyComponent
import com.lastwords.ashley.body.ContactSensor
import com.lastwords.ashley.body.FixtureComponent
import com.lastwords.ashley.body.FixtureType
import com.lastwords.ashley.position.PositionComponent

fun TiledMap.getObstacles(): Array<Entity> {
    val entities = mutableListOf<Entity>()

//    val tileSize = this.properties["tileSize"]?.toString()?.toInt() ?: return entities.toTypedArray()

    val objects = this.layers["obstacles"].objects
    for (obj in objects) {
        if (obj is TextureMapObject) {
            continue
        }

        val positionComponent = PositionComponent(0f, 0f)

        val shape: Shape = if (obj is RectangleMapObject) {
            positionComponent.position = Vector2(obj.rectangle.x, obj.rectangle.y)
            getRectangle(obj)
        } else {
            continue
        }

        val entity = Entity()
        entity.add(positionComponent)
        val bodyComponent = BodyComponent(positionComponent.position, BodyDef.BodyType.StaticBody)
        entity.add(bodyComponent)
        entity.add(FixtureComponent(bodyComponent.body, mutableListOf(
                ContactSensor(entity, shape, FixtureType.WALL)
        )))
        entities.add(entity)

    }

    return entities.toTypedArray()
}

fun getRectangle(rectangleMapObject: RectangleMapObject): PolygonShape {
    val rectangle = rectangleMapObject.rectangle
    val polygon = PolygonShape()
    val normalizedWidth = rectangle.width * LastWords.PIXEL_TO_METER * .5f
    val normalizedHeight = rectangle.height * LastWords.PIXEL_TO_METER * .5f
    val size = Vector2(normalizedWidth, normalizedHeight)
    polygon.setAsBox(normalizedWidth, normalizedHeight, size, 0.0f)
    return polygon
}