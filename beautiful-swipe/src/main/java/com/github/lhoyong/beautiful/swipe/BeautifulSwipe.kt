/*
 * Designed and developed by 2021 lhoyong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.lhoyong.beautiful.swipe

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sign

@Composable
fun BeautifulSwipe(
    items: List<SwipeItem>
) {
    var offset: Offset by remember { mutableStateOf(Offset(0f, 0f)) }
    var rotate: Float by remember { mutableStateOf(0f) }
    val width = with(LocalConfiguration.current) {
        screenWidthDp.toFloat()
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .drag { _, dragAmount ->
                val x = offset.x + dragAmount.x
                val y = offset.y + dragAmount.y
                val target = normalize(
                    0f,
                    width,
                    abs(offset.x),
                    0f,
                    10f
                )
                rotate = target * -x.sign
                offset = Offset(x, y)
            }
    ) {
        items.forEachIndexed { index, item ->
            BeautifulSwipeItem(
                offset = offset,
                rotate = rotate,
                item = item,
                isAnimated = index == items.lastIndex
            )
        }
    }
}

@Composable
fun BeautifulSwipeItem(
    corner: Dp = 12.dp,
    elevation: Dp = 4.dp,
    padding: Dp = 10.dp,
    offset: Offset,
    rotate: Float,
    item: SwipeItem,
    isAnimated: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .offset {

                if (isAnimated) IntOffset(
                    offset.x.roundToInt(),
                    offset.y.roundToInt()
                ) else IntOffset(0, 0)
            }
            .graphicsLayer {
                if (isAnimated) {
                    rotationZ = rotate
                }
            },
        shape = RoundedCornerShape(corner),
        elevation = elevation
    ) {
        Image(
            painter = rememberImagePainter(item.imageUrl),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
    }
}
