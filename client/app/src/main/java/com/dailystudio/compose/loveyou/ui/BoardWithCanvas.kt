package com.dailystudio.compose.loveyou.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dailystudio.compose.loveyou.Item
import com.dailystudio.compose.loveyou.R

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
                val x = transition.mapX[index]?.value?.toFloat() ?: 0f
                val y = transition.mapY[index]?.value?.toFloat() ?: 0f

                drawRect(item.color, Offset(x, y), Size(gridWidth, gridHeight))
            }
        }

        Text(
            stringResource(id = R.string.power_by_canvas),
            color = Color.Gray,
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.padding(4.dp)
        )
    }
}