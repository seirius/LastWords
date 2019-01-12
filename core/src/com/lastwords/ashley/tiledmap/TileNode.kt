package com.lastwords.ashley.tiledmap

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.Vector2
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.ObjectMapper
import org.json.JSONArray
import org.json.JSONObject


class TileNode(
    var x: Int = 0,
    var y: Int = 0,
    var h: Int = 0,
    var g: Float = 0f,
    var f: Float = 0f,
    var tileType: TileType = TileType.NOT_DEFINED,
    var origin: Boolean = false,
    var target: Boolean = false,
    @JsonIgnore
    var parent: TileNode? = null,
    var path: Boolean = false
) {
    fun clone(): TileNode {
        return TileNode(x, y, h, g, f, tileType, origin, target, parent, path)
    }

    override fun equals(other: Any?): Boolean {
        return other != null && other is TileNode && other.x == x && other.y == y
    }

    fun centeredPosition(): Vector2 {
        return Vector2(
                (this.x * TiledMapComponent.TILE_SIZE + TiledMapComponent.HALF_TILE_SIZE).toFloat(),
                (this.y * TiledMapComponent.TILE_SIZE + TiledMapComponent.HALF_TILE_SIZE).toFloat()
        )
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + h
        result = 31 * result + g.hashCode()
        result = 31 * result + f.hashCode()
        result = 31 * result + tileType.hashCode()
        result = 31 * result + origin.hashCode()
        result = 31 * result + target.hashCode()
        result = 31 * result + (parent?.hashCode() ?: 0)
        result = 31 * result + path.hashCode()
        return result
    }
}

class NodeMap(
    var tileNodes: Array<Array<TileNode>>
) {

    fun toJson(): String {
        return ObjectMapper().writeValueAsString(this)
    }

}

fun TiledMap.createNodeMap(layerName: String): NodeMap {
    val map = mutableListOf<Array<TileNode>>()
    val aiNodes = this.layers.get(layerName) as TiledMapTileLayer
    for (x in (0 until aiNodes.width)) {
        val rowsList = mutableListOf<TileNode>()
        for (y in (0 until aiNodes.height)) {
            val cell = aiNodes.getCell(x, y)
            val tileNode = TileNode(x, y)
            if (cell != null) {
                tileNode.tileType = TileType.valueOf((cell.tile.properties["type"] as String))
            }
            rowsList.add(tileNode)
        }
        map.add(rowsList.toTypedArray())
    }
    return NodeMap(map.toTypedArray())
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
            if (cell != null) {
                tileNode.tileType = TileType.valueOf((cell.tile.properties["type"] as String))
            }
            rowsList.add(tileNode)
        }
        map.add(rowsList.toTypedArray())
    }

    val path = getPath(NodeMap(map.toTypedArray()), origin, target)

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

//    LastWords.SOCKET!!.emit("kotlin-tiles", jsonArray)

    return map.toTypedArray()
}

fun heuristic(position: TileNode, target: TileNode): Int {
    return Math.abs(position.x - target.x) + Math.abs(position.y - target.y)
}

fun constructPath(node: TileNode): List<TileNode> {
    var auxNode = node
    auxNode.path = true
    val path = mutableListOf(node)
    while (auxNode.parent != null) {
        auxNode = auxNode.parent!!
        auxNode.path = true
        path.add(auxNode)
    }
    return path
}

fun neighbors(tileMap: Array<Array<TileNode>>, node: TileNode): List<TileNode> {
    val neighbors = mutableListOf<TileNode>()
    val vhSublings = mutableListOf<TileNode>()
    val mapWidth = tileMap.size
    val mapHeight = tileMap[0].size

    val offsets = mutableListOf(
            intArrayOf(-1, 0),
            intArrayOf(1, 0),
            intArrayOf(0, -1),
            intArrayOf(0, 1),
            intArrayOf(-1, -1, 0),
            intArrayOf(1, -1, 0),
            intArrayOf(-1, 1, 0),
            intArrayOf(1, 1, 0)
    )
    for (offset in offsets) {
        val xOffSetd = node.x + offset[0]
        val yOffSetd = node.y + offset[1]
        if (isTileInRange(xOffSetd, yOffSetd, mapWidth, mapHeight)) {
            val neighbor = tileMap[xOffSetd][yOffSetd].clone()
            if (neighbor.tileType != TileType.WALL) {
                neighbor.parent = node
                if (offset.size == 2) {
                    neighbor.g += 1
                    neighbors.add(neighbor)
                } else {
                    var siblingHasWalls = false
                    var i = 0
                    while (i < vhSublings.size && !siblingHasWalls) {
                        val sibling = vhSublings[i]
                        siblingHasWalls = sibling.tileType == TileType.WALL && isDirectNeighbor(neighbor, sibling)
                        i++
                    }
                    if (!siblingHasWalls) {
                        neighbor.g += 1.43f
                        neighbors.add(neighbor)
                    }
                }
            }
            vhSublings.add(neighbor)
        }
    }

    return neighbors
}

fun isTileInRange(x: Int, y: Int, maxWidth: Int, maxHeight: Int): Boolean {
    return x > -1 && x < maxWidth - 1
            && y > -1 && y < maxHeight - 1
}

fun isDirectNeighbor(node: TileNode, toNode: TileNode): Boolean {
    val xOffset = Math.abs(node.x - toNode.x)
    val yOffset = Math.abs(node.y - toNode.y)
    return xOffset + yOffset == 1
}

fun getPath(nodeMap: NodeMap, origin: TileNode, target: TileNode): Array<TileNode> {
    val start = origin.clone()
    val goal = target.clone()

    val openList = mutableListOf(start)
    val closedList = mutableListOf<TileNode>()


    start.h = heuristic(start, goal)
    start.g = 0f
    start.f = start.g + start.h
    start.origin = true
    goal.target = true

    while(openList.isNotEmpty()) {
        val current = openList.minBy { it -> it.f }
        if (current == goal) {
            return constructPath(current).toTypedArray()
        }
        openList.remove(current)
        closedList.add(current!!)
        for (neighbor in neighbors(nodeMap.tileNodes, current)) {
            if (neighbor !in closedList) {
                neighbor.h = heuristic(neighbor, goal)
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

fun Array<TileNode>.toJson(): String {
    return ObjectMapper().writeValueAsString(this)
}