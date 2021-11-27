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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import kotlin.math.roundToInt

@Composable
fun BeautifulSwipe(
    items: List<SwipeItem>
) {
    var offset: Offset by remember { mutableStateOf(Offset(0f, 0f)) }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.drag { _, dragAmount ->
            val x = offset.x + dragAmount.x
            val y = offset.y + dragAmount.y
            offset = Offset(x, y)
        }
    ) {
        items.forEach { item ->
            BeautifulSwipeItem(offset = offset, item = item)
        }
    }
}

@Composable
fun BeautifulSwipeItem(
    width: Dp = 150.dp,
    height: Dp = 200.dp,
    corner: Dp = 12.dp,
    elevation: Dp = 4.dp,
    padding: Dp = 10.dp,
    offset: Offset,
    item: SwipeItem
) {
    Card(
        modifier = Modifier
            .size(width = width, height = height)
            .padding(padding)
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                layout(placeable.width, placeable.height) {
                    println("layout : width=${placeable.width}, height=${placeable.height}/ offset: x=${offset.x}, y=${offset.y}")
                    placeable.placeRelative(offset.x.roundToInt(), offset.y.roundToInt())
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
