package com.lastwords.ashley.tiledmap

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.lastwords.LastWords
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.impl.client.HttpClientBuilder
import org.json.JSONArray
import org.json.JSONObject


class TileNode(
    var x: Int = 0,
    var y: Int = 0,
    var h: Int = 0,
    var g: Int = 0,
    var f: Int = 0,
    var tileType: TileType = TileType.NOT_DEFINED,
    var origin: Boolean = false,
    var target: Boolean = false,
    var parent: TileNode? = null,
    var path: Boolean = false
) {
    fun clone(): TileNode {
        return TileNode(x, y, h, g, f, tileType, origin, target, parent, path)
    }

    override fun equals(other: Any?): Boolean {
        return other != null && other is TileNode && other.x == x && other.y == y
    }
}

fun getNodes(tiledMap: TiledMap, origin: TileNode, target: TileNode): Array<Array<TileNode>> {
    val map = mutableListOf<Array<TileNode>>()

    origin.origin = true
    target.target = true

    val aiNodes = tiledMap.layers.get("ai_nodes") as TiledMapTileLayer

    for (x in (0 until aiNodes.width)) {
        val rowsList = mutableListOf<TileNode>()
        for (y in (0 until aiNodes.height)) {
            val cell = aiNodes.getCell(x, y)
            val tileNode = if (x == origin.x && y == origin.y) {
                origin
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

    val path = getPath(map.toTypedArray(), origin, target)

    val jsonArray = JSONArray()
    for (row in map) {
        val jsonRow = JSONArray()
        for (tileNode in row) {
            tileNode.path = tileNode in path
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
            jsonCell.put("path", tileNode.path)
            jsonRow.put(jsonCell)
        }
        jsonArray.put(jsonRow)
    }

    LastWords.SOCKET!!.emit("java-client", jsonArray)
    val createdPathJson = jsonArray.toString().toByteArray()

    val builder = MultipartEntityBuilder.create()
    builder.addBinaryBody("tiles", createdPathJson, ContentType.MULTIPART_FORM_DATA, "tiles.json")
    val httpPost = HttpPost("http://localhost:3000/debug/a-star")
    httpPost.entity = builder.build()
    HttpClientBuilder.create().build().execute(httpPost)

//    File("/home/andriy/dev/LastWordsWeb/tiles.json").writeText(jsonArray.toString())

    return map.toTypedArray()
}

fun heuristic(position: TileNode, target: TileNode): Int {
    return Math.abs(position.x - target.x) + Math.abs(position.y - target.y)
}

fun constructPath(node: TileNode): List<TileNode> {
    var auxNode = node
    val path = mutableListOf(node)
    while (auxNode.parent != null) {
        auxNode = auxNode.parent!!
        path.add(auxNode)
    }
    return path
}

fun neighbors(tileMap: Array<Array<TileNode>>, node: TileNode): List<TileNode> {
    val neighbors = mutableListOf<TileNode>()
    val mapWidth = tileMap.size
    val mapHeight = tileMap[0].size

    val offsets = mutableListOf(
            intArrayOf(-1, 0),
            intArrayOf(1, 0),
            intArrayOf(0, -1),
            intArrayOf(0, 1)
    )
    for (offset in offsets) {
        val xOffSetd = node.x + offset[0]
        val yOffSetd = node.y + offset[1]
        if (isTileInRange(xOffSetd, yOffSetd, mapWidth, mapHeight)) {
            val neighbor = tileMap[xOffSetd][yOffSetd].clone()
            if (neighbor.tileType != TileType.WALL) {
                neighbor.parent = node
                neighbor.g += 1
                neighbors.add(neighbor)
            }
        }
    }

    var xOffsetd = node.x - 1
    var yOffsetd = node.y + 1
    if (isTileInRange(xOffsetd, yOffsetd, mapWidth, mapHeight)) {

    }


    return neighbors
}

fun isTileInRange(x: Int, y: Int, maxWidth: Int, maxHeight: Int): Boolean {
    return x > -1 && x < maxWidth - 1
            && y > -1 && y < maxHeight - 1
}

fun getPath(tileMap: Array<Array<TileNode>>, origin: TileNode, target: TileNode): Array<TileNode> {
    val start = origin.clone()
    val goal = target.clone()

    val openList = mutableListOf(start)
    val closedList = mutableListOf<TileNode>()


    start.g = 0
    start.f = start.g + start.h

    while(openList.isNotEmpty()) {
        val current = openList.minBy { it -> it.f }
        if (current == goal) {
            return constructPath(current).toTypedArray()
        }
        openList.remove(current)
        closedList.add(current!!)
        for (neighbor in neighbors(tileMap, current)) {
            if (neighbor !in closedList) {
                neighbor.f = neighbor.g + neighbor.h
                if (neighbor !in openList) {
                    openList.add(neighbor.clone())
                } else {
                    val openNeighbor = openList.find { it -> it == neighbor }
                    if (neighbor.g < openNeighbor!!.g) {
                        openNeighbor.g = neighbor.g
                        openNeighbor.parent = neighbor.parent
                    }
                }
            }
        }
    }

    return mutableListOf<TileNode>().toTypedArray()
}