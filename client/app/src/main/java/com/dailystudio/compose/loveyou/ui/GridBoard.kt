package com.dailystudio.compose.loveyou.ui

import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.toSize
import com.dailystudio.compose.loveyou.Item
import com.dailystudio.devbricksx.development.Logger

@DslMarker
annotation class GridBoardScopeMarker

@Composable
fun GridBoard(modifier: Modifier = Modifier,
              row: Int = 20,
              col: Int = 24,
              data: List<Item>,
              showGrid: Boolean = true,
              debugPos: Boolean = false,
              content: @Composable GridBoardScope.() -> Unit
) {
    var gridWidth by remember { mutableStateOf(0f) }
    var gridHeight by remember { mutableStateOf(0f) }

    val transition = updateGridTransition(
        data,
        Size(gridWidth, gridHeight)
    )

    val scopeIml = GridBoardScopeIml(gridWidth, gridHeight, transition)

    Box(modifier = modifier.fillMaxSize()
        .onGloballyPositioned { coordinates ->
            val size = coordinates.size.toSize()
            gridWidth = (size.width / col)
            gridHeight = (size.height / row)
        },
        contentAlignment = Alignment.BottomCenter
    ) {
        Logger.debug("GT data[${data.hashCode()}].transition [${transition.hashCode()}]: $transition ")

        content(scopeIml)

        Canvas(modifier = Modifier.fillMaxSize()) {
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
        }

    }

}


@GridBoardScopeMarker
interface GridBoardScope {

    @Stable
    val gridWidth: Float

    @Stable
    val gridHeight: Float

    @Stable
    val transition: GridTransition

}

class GridBoardScopeIml(override val gridWidth: Float,
                        override val gridHeight: Float,
                        override val transition: GridTransition): GridBoardScope {

}
