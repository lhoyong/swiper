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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@ExperimentalMaterialApi
@Composable
fun <T> BeautifulSwipe(
    items: List<T>,
    content: @Composable (T) -> Unit
) {
    var itemsInternal by remember { mutableStateOf(items) }
    val rememberSwipeState = rememberSwipeState()

    rememberSwipeState.onAnimationEnd = {
        itemsInternal = itemsInternal.drop(1)
    }

    val targetItems = itemsInternal.take(2).reversed()
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .swipe(rememberSwipeState)
            .padding(30.dp)
    ) {
        targetItems.forEachIndexed { index, item ->
            key(item) {
                BeautifulSwipeItem(
                    offset = rememberSwipeState.currentValue,
                    rotate = rememberSwipeState.rotate,
                    scale = rememberSwipeState.scale,
                    item = item,
                    isAnimated = if (targetItems.size == 1) true else index == 1,
                    content = content
                )
            }
        }
    }
}

@Composable
fun <T> BeautifulSwipeItem(
    corner: Dp = 12.dp,
    elevation: Dp = 4.dp,
    padding: Dp = 10.dp,
    offset: Offset,
    rotate: Float,
    scale: Float,
    item: T,
    isAnimated: Boolean,
    content: @Composable (T) -> Unit
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
                rotationZ = if (isAnimated) rotate else 0f
                scaleX = if (!isAnimated) scale else 1f
                scaleY = if (!isAnimated) scale else 1f
            },
        shape = RoundedCornerShape(corner),
        elevation = elevation
    ) {
        content(item)
    }
}
