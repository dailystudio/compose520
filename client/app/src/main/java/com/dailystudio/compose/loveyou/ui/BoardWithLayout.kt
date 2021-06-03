package com.dailystudio.compose.loveyou.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import com.dailystudio.compose.loveyou.Item
import kotlin.math.roundToInt


@Composable
fun BoardWithLayout(modifier: Modifier = Modifier,
                    row: Int = 20,
                    col: Int = 24,
                    data: List<Item>,
                    showGrid: Boolean = true,
                    debugPos: Boolean = false
) {
    GridBoard(modifier, row, col, data, showGrid, debugPos) {
        Layout(
            content = {
                for (i in data) {
                    Piece(color = i.color)
                }
            }
        ) { measurables, constraints ->
            val placeables = measurables.map { measurable ->
                val placeable = measurable.measure(
                    constraints.copy(
                        minWidth = gridWidth.roundToInt(),
                        maxWidth = gridWidth.roundToInt(),
                        minHeight = gridHeight.roundToInt(),
                        maxHeight = gridHeight.roundToInt(),
                    )
                ).also {
//                        Logger.debug("measured placeable: [${it.width} x ${it.height}]")
                }

                placeable
            }

            layout(constraints.maxWidth, constraints.maxHeight) {
                for ((index, placeable) in placeables.withIndex()) {
                    val x = transition.mapX[index]?.value
                    val y = transition.mapY[index]?.value

                    placeable.placeRelative(x = x!!, y = y!!)
                }
            }
        }

        Text("Powered by Layout",
            color = Color.Gray,
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.padding(4.dp)
        )
    }

}