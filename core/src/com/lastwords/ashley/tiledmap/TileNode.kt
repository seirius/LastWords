package com.lastwords.ashley.tiledmap

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.Json
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class TileNode(
    var x: Int = 0,
    var y: Int = 0,
    var h: Int = 0,
    var g: Int = 0,
    var f: Int = 0,
    var tileType: TileType = TileType.NOT_DEFINED,
    var origin: Boolean = false,
    var target: Boolean = false,
    var parent: TileNode? = null
)

fun getNodes(tiledMap: TiledMap, position: TileNode, target: TileNode): Array<Array<TileNode>> {
    val map = mutableListOf<Array<TileNode>>()

    position.origin = true
    target.target = true

    val aiNodes = tiledMap.layers.get("ai_nodes") as TiledMapTileLayer

    for (x in (0 until aiNodes.width)) {
        val rowsList = mutableListOf<TileNode>()
        for (y in (0 until aiNodes.height)) {
            val cell = aiNodes.getCell(x, y)
            val tileNode = if (x == position.x && y == position.y) {
                position
            } else if (x == target.x && y == target.y) {
                target
            } else {
                TileNode(x, y)
            }
            tileNode.h = heuristic(tileNode, target)
            if (cell != null) {
                tileNode.tileType = TileType.valueOf((cell.tile.properties["type"] as String))
            }
            rowsList.add(tileNode)
        }
        map.add(rowsList.toTypedArray())
    }

    val jsonArray = JSONArray()
    for (row in map) {
        val jsonRow = JSONArray()
        for (tileNode in row) {
            val eks = when {
                tileNode.tileType == TileType.WALL -> "X"
                else -> "O"
            }
            val originTarget = when {
                tileNode.target -> "T"
                tileNode.origin -> "O"
                else -> "N"
            }

            val jsonCell = JSONObject()
            jsonCell.put("x", tileNode.x)
            jsonCell.put("y", tileNode.y)
            jsonCell.put("ot", originTarget)
            jsonCell.put("w", eks)
            jsonCell.put("h", tileNode.h)
            jsonCell.put("g", tileNode.g)
            jsonRow.put(jsonCell)
        }
        jsonArray.put(jsonRow)
    }

    println(jsonArray.toString())
    File("/home/andriy/my_dev/LastWordsWeb/tiles.json").writeText(jsonArray.toString())

    return map.toTypedArray()
}

fun heuristic(position: TileNode, target: TileNode): Int {
    return Math.abs(position.x - target.x) + Math.abs(position.y - target.y)
}