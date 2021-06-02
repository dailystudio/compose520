package com.dailystudio.compose.loveyou.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.dailystudio.compose.loveyou.Item

@Composable
fun BoardWithCanvas(
    modifier: Modifier = Modifier,
    row: Int = 20,
    col: Int = 24,
    data: List<Item>,
    showGrid: Boolean = true,
    debugPos: Boolean = false
) {
    GridBoard(modifier, row, col, data, showGrid, debugPos) {
        Canvas(modifier = modifier.fillMaxSize()) {
            for ((index, item) in data.withIndex()) {
                //            Logger.debug("GT: $transition")
                val x = transition.mapX[index]?.value?.toFloat() ?: 0f
                val y = transition.mapY[index]?.value?.toFloat() ?: 0f

                drawRect(item.color, Offset(x, y), Size(gridWidth, gridHeight))
            }
        }

        Text("Powered by Canvas")
    }
}