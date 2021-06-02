package com.dailystudio.compose.loveyou.ui

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Size
import com.dailystudio.compose.loveyou.Item


class GridTransition(val mapX: Map<Int, State<Int>>,
                     val mapY: Map<Int, State<Int>>) {

    override fun toString(): String {
        return buildString {
            append("x: $mapX, y: $mapY")
        }
    }
}

@Composable
fun updateGridTransition(data: List<Item>,
                         gridSize: Size
): GridTransition {
    val mapX = mutableMapOf<Int, State<Int>>()
    val mapY = mutableMapOf<Int, State<Int>>()

    for (i in data.indices) {
        mapX[i] = animateIntAsState(
            targetValue = (gridSize.width * data[i].pos.x).toInt(),
        )
    }

    for (i in data.indices) {
        mapY[i] = animateIntAsState(
            targetValue = (gridSize.height * data[i].pos.y).toInt(),
        )
    }

    val transition = GridTransition(mapX, mapY)

    return remember(System.currentTimeMillis()) {
        transition
    }
}
