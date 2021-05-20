package com.dailystudio.compose.loveyou.data

import android.graphics.Point

class BoardItem {
    var pos: Point = Point(0, 0)
    var colorIndex: Int = 0

    override fun toString(): String {
        return buildString {
            append("p: $pos,")
            append("c: $colorIndex,")
        }
    }
}

class BoardData {

    var colorSet: Array<String> = arrayOf()
    var grids: Array<Array<BoardItem>> = arrayOf()

    override fun toString(): String {
        return buildString {
            append("\nColor set:")
            for (c in colorSet) {
                append("\ncolor: $c")
            }
            append("\nGrids:")
            for ((i, grid) in grids.withIndex()) {
                append("\n[$i]:")
                for (g in grid) {
                    append("\ngrid: $g}")
                }
            }
        }
    }

}