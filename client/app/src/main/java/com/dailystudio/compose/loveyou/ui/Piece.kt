package com.dailystudio.compose.loveyou.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dailystudio.compose.loveyou.ui.theme.Compose520Theme

@Composable
fun Piece(color: Color,
          modifier: Modifier = Modifier
              .width(52.1.dp)
              .height(52.1.dp)
) {
    Canvas(modifier = modifier) {
        drawRect(color, Offset.Zero, size)
    }
}

@Preview
@Composable
fun PiecePreview() {
    Compose520Theme {
        Piece(color = Color.Red)
    }
}