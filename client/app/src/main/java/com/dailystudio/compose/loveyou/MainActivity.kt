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

        val RANDOM: Random = Random
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Compose520Theme(){
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val map = remember { mutableStateListOf<Item>() }
                    val maxRow by remember { mutableStateOf(27) }
                    val maxCol by remember { mutableStateOf(15) }
                    var currGridIndex by remember { mutableStateOf(0)}
                    var data by remember { mutableStateOf(BoardData())}

                    val randomGen = {
                        map.clear()
                        for (i in 0 until RANDOM.nextInt(100, 200)) {
                            map.add(
                                Item(i,
                                    Point(
                                        RANDOM.nextInt(0, maxCol),
                                        RANDOM.nextInt(0, maxRow)),
                                    ALL_COLORS[RANDOM.nextInt(0, ALL_COLORS.size)]
                                ))
                        }
                    }

                    val dataGen: (items: Array<BoardItem>) -> Unit = { items ->
                        map.clear()

                        val occupiedSet = mutableSetOf<String>()
                        for (r in 0 until maxRow) {
                            for (c in 0 until maxCol) {
                                occupiedSet.add("${c}_$r")
                            }
                        }

                        val itemSize = items.size
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
                            occupiedSet.remove("${item.pos.x}_${item.pos.y}")
                        }

                        for ((i, posStr) in occupiedSet.withIndex()) {
                            val parts = posStr.split("_")

                            map.add(
                                Item(i + itemSize,
                                    Point(
                                        parts[0].toInt(),
                                        parts[1].toInt()),
                                    Color.Transparent)
                                )
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
                        showGrid = true,
                        debugPos = false,
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
