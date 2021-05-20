package com.dailystudio.compose.loveyou.ui

import android.graphics.Point
import android.util.Size
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import com.dailystudio.compose.loveyou.Item
import com.dailystudio.compose.loveyou.Piece
import com.dailystudio.devbricksx.development.Logger
import kotlin.math.roundToInt


@Composable
fun Board(modifier: Modifier = Modifier,
          row: Int = 20,
          col: Int = 24,
          data: List<Item>
) {
    var gridWidth by remember { mutableStateOf(0f) }
    var gridHeight by remember { mutableStateOf(0f) }
    
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            gridWidth = (size.width / col)
            gridHeight = (size.height / row)
//            Logger.debug("grid: $gridWidth * $gridHeight")

            drawRect(Color.Yellow, Offset.Zero, size)

            for (i in 0 until  col) {
                val startX = i * gridWidth
                val startY = 0f
                val endY = size.height
                drawLine(Color.Black,
                    Offset(startX, startY),
                    Offset(startX, endY)
                )
            }

            for (i in 0 until  row) {
                val startX = 0f
                val endX = size.width
                val startY = i * gridHeight
                drawLine(Color.Black,
                    Offset(startX, startY),
                    Offset(endX, startY)
                )
            }
        }

        val mapX = mutableMapOf<Int, State<Int>>()
        val mapY = mutableMapOf<Int, State<Int>>()

        for (i in data.indices) {
            mapX[i] = animateIntAsState(targetValue = (gridWidth * data[i].pos.x).roundToInt())
        }

        for (i in data.indices) {
            mapY[i] = animateIntAsState(targetValue = (gridHeight * data[i].pos.y).roundToInt())
        }

        Layout(
            modifier = modifier
                .fillMaxSize(),
            content = {
                for (i in data) {
                    Piece(color = i.color)
                }
            }) { measurables, constraints ->

                val placeables = measurables.map { measurable ->
                    measurable.parentData
                    val placeable = measurable.measure(
                        // You need set the minWidth/Height of the constraints
                        // passed in. Otherwise, your children's measurement
                        // will be affected by the parent's modifier.
                        // (e.g Modifier.fillMaxHeight())
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
                        val x = mapX[index]?.value
                        val y = mapY[index]?.value

                        placeable.placeRelative(x = x!!, y = y!!)
                    }
                }
        }
    }

}