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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

/**
 * @param count item count
 * @param modifier swipe container modifier
 * @param state the state object to be used to control or observe the swiper's state.
 * @param thresholdConfig see [ThresholdConfig]
 * @param onSwiped swiped item, it's called when the swipe animation ends.
 * @param content swipe child item composable
 */
@ExperimentalMaterialApi
@Composable
fun Swiper(
    count: Int,
    modifier: Modifier = Modifier,
    state: SwiperState = rememberSwiperState(),
    thresholdConfig: (Float, Float) -> ThresholdConfig = { _, _ -> FractionalThreshold(0.3f) },
    onSwiped: (() -> Unit)? = null,
    content: @Composable SwiperScope.(index: Int) -> Unit
) {
    require(count >= 0) { "swipeCount must be >= 0" }

    state.onAnimationEnd = {
        onSwiped?.invoke()
    }

    LaunchedEffect(count) {
        state.totalCount = count - 1
        state.currentIndex = 0
    }

    val scope = remember(state) { SwiperScopeImpl(state) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.swipe(state, thresholdConfig)
    ) {
        for (index in count - 1 downTo state.currentIndex) {
            val animated = state.currentIndex == index
            Box(
                modifier = Modifier
                    .offset {
                        if (animated) IntOffset(
                            state.offset.x.roundToInt(),
                            state.offset.y.roundToInt()
                        ) else IntOffset(0, 0)
                    }
                    .graphicsLayer {
                        rotationZ = if (animated) state.rotate else 0f
                        scaleX = if (!animated) state.scale else 1f
                        scaleY = if (!animated) state.scale else 1f
                    }
            ) {
                scope.content(index)
            }
        }
    }
}

@Stable
interface SwiperScope {
    val index: Int
}

private class SwiperScopeImpl(
    state: SwiperState
) : SwiperScope {

    override val index: Int = state.currentIndex
}
