package com.dailystudio.compose.loveyou.ui

import android.graphics.Typeface
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.Layout
import com.dailystudio.compose.loveyou.Item
import com.dailystudio.compose.loveyou.Piece
import com.dailystudio.devbricksx.development.Logger
import kotlin.math.roundToInt


@Composable
fun BoardCanvas(
    modifier: Modifier = Modifier,
    row: Int = 20,
    col: Int = 24,
    data: List<Item>,
    showGrid: Boolean = true,
    debugPos: Boolean = false
) {
    var gridWidth by remember { mutableStateOf(0f) }
    var gridHeight by remember { mutableStateOf(0f) }

    val mapX = mutableMapOf<Int, State<Int>>()
    val mapY = mutableMapOf<Int, State<Int>>()

    for (i in data.indices) {
        mapX[i] = animateIntAsState(
            targetValue = (gridWidth * data[i].pos.x).toInt(),
        )
    }

    for (i in data.indices) {
        mapY[i] = animateIntAsState(
            targetValue = (gridHeight * data[i].pos.y).toInt(),
        )
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        gridWidth = (size.width / col)
        gridHeight = (size.height / row)

        if (showGrid) {
            for (i in 0 until col) {
                val startX = i * gridWidth
                val startY = 0f
                val endY = size.height
                drawLine(
                    Color.LightGray,
                    Offset(startX, startY),
                    Offset(startX, endY)
                )
            }

            for (i in 0 until row) {
                val startX = 0f
                val endX = size.width
                val startY = i * gridHeight
                drawLine(
                    Color.LightGray,
                    Offset(startX, startY),
                    Offset(endX, startY)
                )
            }
        }

        if (debugPos) {
            val paint = Paint().asFrameworkPaint()
            paint.apply {
                isAntiAlias = true
                textSize = 24f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }

            for (r in 0 until row) {
                for (c in 0 until col) {
                    val left = c * gridWidth
                    val top = r * gridHeight + gridHeight / 2
                    drawIntoCanvas {

                        it.nativeCanvas.drawText("$c.$r", left, top, paint)
                    }
                }
            }
        }

        for ((index, item) in data.withIndex()) {
            val x = mapX[index]!!.value.toFloat()
            val y = mapY[index]!!.value.toFloat()

            drawRect(item.color, Offset(x, y), Size(gridWidth, gridHeight))
        }
    }

}