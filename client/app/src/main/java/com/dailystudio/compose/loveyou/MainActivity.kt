package com.dailystudio.compose.loveyou

import android.graphics.Point
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.dailystudio.compose.loveyou.data.BoardData
import com.dailystudio.compose.loveyou.data.BoardItem
import com.dailystudio.compose.loveyou.ui.BoardWithCanvas
import com.dailystudio.compose.loveyou.ui.BoardWithLayout
import com.dailystudio.compose.loveyou.ui.theme.Compose520Theme
import com.dailystudio.devbricksx.development.Logger
import com.dailystudio.devbricksx.utils.JSONUtils
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val map = remember { mutableStateListOf<Item>() }
            val maxRow by remember { mutableStateOf(27) }
            val maxCol by remember { mutableStateOf(15) }
            var currGridIndex by remember { mutableStateOf(-1)}
            var data by remember { mutableStateOf(BoardData())}

            var useCanvasRender by remember {
                mutableStateOf(true)
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
                    val colorStr = data.colorSet[item.colorIndex]

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

            Compose520Theme(){
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = stringResource(id = R.string.app_name))
                            },
                            actions = {
                                IconButton(onClick = {
                                    useCanvasRender = !useCanvasRender
                                }) {
                                    if (useCanvasRender) {
                                        Icon(Icons.Default.ViewCompact,
                                            "Layout")
                                    } else {
                                        Icon(Icons.Default.AppRegistration, "Canvas")
                                    }
                                }
                            }
                        )
                    }
                ) {
                    Surface(color = MaterialTheme.colors.background) {
                        val interactionSource = remember { MutableInteractionSource() }
                        if (useCanvasRender) {
                            BoardWithCanvas(
                                row = maxRow, col = maxCol,
                                data = map,
                                showGrid = true,
                                debugPos = false,
                                modifier = Modifier.clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    currGridIndex = (currGridIndex + 1) % data.grids.size
                                    Logger.debug("click: index = $currGridIndex")
                                    dataGen(data.grids[currGridIndex])
                                }
                            )
                        } else {
                            BoardWithLayout(
                                row = maxRow, col = maxCol,
                                data = map,
                                showGrid = true,
                                debugPos = false,
                                modifier = Modifier.clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    currGridIndex = (currGridIndex + 1) % data.grids.size
                                    Logger.debug("click: index = $currGridIndex")
                                    dataGen(data.grids[currGridIndex])
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
