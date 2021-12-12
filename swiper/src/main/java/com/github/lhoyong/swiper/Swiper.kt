/*
 * Copyright 2021 by lhoyong
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
package com.github.lhoyong.swiper

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.ThresholdConfig
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
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

/**
 * @param items swipe items
 * @param modifier swipe container modifier
 * @param thresholdConfig see [ThresholdConfig]
 * @param onSwiped swiped item, it's called when the swipe animation ends.
 * @param content swipe child item composable
 */
@ExperimentalMaterialApi
@Composable
fun <T> Swiper(
    items: List<T>,
    modifier: Modifier = Modifier,
    thresholdConfig: (Float, Float) -> ThresholdConfig = { _, _ -> FractionalThreshold(0.3f) },
    onSwiped: ((T) -> Unit)? = null,
    content: @Composable (T) -> Unit
) {
    var itemsInternal by remember { mutableStateOf(items) }
    val rememberSwipeState = rememberSwipeState()

    rememberSwipeState.onAnimationEnd = {
        val captureItem = itemsInternal[0]
        itemsInternal = itemsInternal.drop(1)
        onSwiped?.invoke(captureItem)
    }

    val targetItems = itemsInternal.take(2).reversed()
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .swipe(rememberSwipeState, thresholdConfig)
    ) {
        targetItems.forEachIndexed { index, item ->
            key(item) {
                SwiperItem(
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
private fun <T> SwiperItem(
    offset: Offset,
    rotate: Float,
    scale: Float,
    item: T,
    isAnimated: Boolean,
    content: @Composable (T) -> Unit
) {
    Box(
        modifier = Modifier
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
            }
    ) {
        content(item)
    }
}
