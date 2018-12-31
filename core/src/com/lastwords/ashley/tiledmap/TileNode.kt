package com.lastwords.ashley.tiledmap

import com.badlogic.gdx.maps.tiled.TiledMap

class TileNode(
    var g: Int,
    var h: Int = 0,
    var f: Int = 0,
    var parent: TileNode? = null
)

//fun getNodes(tiledMap: TiledMap): Array<Array<TileNode>> {
//    val map = mutableListOf<Array<TileNode>>()
//    tiledMap.
//    return map.toTypedArray()
//}