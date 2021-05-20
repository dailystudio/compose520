package com.dailystudio.compose.loveyou

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.dailystudio.compose.loveyou.ui.theme.Compose520Theme

@Composable
fun Piece(color: Color) {

    Canvas(modifier = Modifier) {
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