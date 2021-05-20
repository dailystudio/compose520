package com.dailystudio.compose.loveyou

import android.graphics.Point
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.dailystudio.compose.loveyou.ui.Board
import com.dailystudio.compose.loveyou.ui.theme.Compose520Theme
import kotlin.random.Random

class MainActivity : ComponentActivity() {

    companion object {
        val ALL_COLORS = arrayOf(
            Color.Red,
            Color.Blue,
            Color.Green,
            Color.Cyan,
            Color.Black,
            Color.Magenta,
            Color.Gray,
        )
    }
    val r: Random = Random

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Compose520Theme(){
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val map = remember {
                        mutableStateListOf<Item>()
                    }

                    val maxRow = 24
                    val maxCol = 20

                    val generateSeed = {
                        map.clear()
                        for (i in 0 until r.nextInt(100, 200)) {
                            map.add(
                                Item(i,
                                    Point(
                                        r.nextInt(0, maxCol),
                                        r.nextInt(0, maxRow)),
                                    ALL_COLORS[r.nextInt(0, ALL_COLORS.size)]
                                    ))
                        }
                    }

                    generateSeed()

                    val interactionSource = remember { MutableInteractionSource() }
                    Board(
                        row = maxRow, col = maxCol,
                        data = map,
                        modifier = Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            generateSeed()
                        }
                    )
                }
            }
        }
    }
}
