package com.dailystudio.compose.loveyou

import android.graphics.Point
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.dailystudio.compose.loveyou.data.BoardData
import com.dailystudio.compose.loveyou.data.BoardItem
import com.dailystudio.compose.loveyou.ui.Board
import com.dailystudio.compose.loveyou.ui.theme.Compose520Theme
import com.dailystudio.devbricksx.development.Logger
import com.dailystudio.devbricksx.utils.JSONUtils
import kotlinx.coroutines.launch
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
                    val map = remember { mutableStateListOf<Item>() }
                    val maxRow by remember { mutableStateOf(24) }
                    val maxCol by remember { mutableStateOf(20) }
                    var currGridIndex by remember { mutableStateOf(0)}
                    var data by remember { mutableStateOf(BoardData())}

                    val randomGen = {
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

                    val dataGen: (items: Array<BoardItem>) -> Unit = { items ->
                        map.clear()

                        for ((i, item) in items.withIndex()) {
                            Logger.debug("gen item: $item")
                            val colorStr = data.colorSet[item.colorIndex]
                            Logger.debug("gen item: $item, color: $colorStr")

                            map.add(
                                Item(i,
                                    Point(
                                        item.pos.x,
                                        item.pos.y),
                                    Color(colorStr.toLong(radix = 16))
                                ))
                        }
                    }

                    val coroutineScope = rememberCoroutineScope()

                    coroutineScope.launch {
                        data = JSONUtils.fromAsset(this@MainActivity,
                            "data.json", BoardData::class.java) ?: BoardData()

                        Logger.debug("data: $data")

                        currGridIndex = 0
                        dataGen(data.grids[currGridIndex])
                    }

                    val interactionSource = remember { MutableInteractionSource() }
                    Board(
                        row = maxRow, col = maxCol,
                        data = map,
                        modifier = Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            currGridIndex = (currGridIndex + 1) % data.grids.size
                            dataGen(data.grids[currGridIndex])
                        }
                    )
                }
            }
        }
    }
}
