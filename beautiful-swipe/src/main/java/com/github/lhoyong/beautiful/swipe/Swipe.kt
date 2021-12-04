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

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.SwipeableDefaults.VelocityThreshold
import androidx.compose.material.ThresholdConfig
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.sign

internal class SwipeState(
    val screenWidth: Float,
    private val animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec
) {
    /**
     * Whether the state is currently animating.
     */
    var isAnimationRunning: Boolean by mutableStateOf(false)
        private set

    var currentValue: Offset by mutableStateOf(Offset(0f, 0f))

    var rotate: Float by mutableStateOf(0f)

    /**
     * Anchors
     */
    val right = Offset(screenWidth, 0f)
    val left = Offset(-screenWidth, 0f)
    val center = Offset(0f, 0f)

    /**
     * Threshold to start swiping
     */
    var threshold: Float = 0.0f

    var velocityThreshold by mutableStateOf(0f)

    var scale = Animatable(0f)

    internal suspend fun snapInternalTo(previous: Float, target: Float): Float {
        return Animatable(previous).apply {
            snapTo(target)
        }.value
    }

    private suspend fun animateInternalTo(
        previous: Float,
        target: Float,
        result: suspend (Float) -> Unit
    ) {
        isAnimationRunning = true
        try {
            coroutineScope {
                Animatable(previous).animateTo(target, animationSpec) {
                    launch { result(value) }
                }
            }
        } finally {
            isAnimationRunning = false
        }
    }

    suspend fun swipeLeft() {
        animateInternalTo(currentValue.x, -screenWidth) {
            val x = snapInternalTo(it, center.x)
            val y = snapInternalTo(currentValue.y, 0f)
            currentValue = Offset(x, y)
            rotate = snapInternalTo(rotate, 0f)
            scale.animateTo(0.8f)
        }
        scale.animateTo(1f, animationSpec)
    }

    suspend fun swipeRight() {
        animateInternalTo(currentValue.x, screenWidth) {
            val x = snapInternalTo(it, center.x)
            val y = snapInternalTo(currentValue.y, 0f)
            currentValue = Offset(x, y)
            rotate = snapInternalTo(rotate, 0f)
            scale.animateTo(0.8f)
        }
        scale.animateTo(1f, animationSpec)
    }

    suspend fun returnCenter() {
        var x = 0f
        var y = 0f
        animateInternalTo(currentValue.x, center.x) {
            x = it
        }
        animateInternalTo(currentValue.y, center.y) {
            y = it
        }
        currentValue = Offset(x, y)
        animateInternalTo(rotate, 0f) {
            rotate = it
        }
        scale.animateTo(0.8f, animationSpec)
    }
}

@Composable
internal fun rememberSwipeState(
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec
): SwipeState {
    val width = with(LocalConfiguration.current) {
        screenWidthDp.toFloat()
    }

    return remember {
        SwipeState(width, animationSpec)
    }
}

@ExperimentalMaterialApi
internal fun Modifier.swipe(
    state: SwipeState,
    thresholdConfig: (Float, Float) -> ThresholdConfig = { _, _ -> FractionalThreshold(0.2f) },
    velocityThreshold: Dp = VelocityThreshold
): Modifier = composed {
    val density = LocalDensity.current
    LaunchedEffect(state) {
        val thresholds = { from: Float, to: Float ->
            with(thresholdConfig(from, to)) {
                density.computeThreshold(from, to)
            }
        }
        state.threshold = thresholds(state.center.x, state.right.x)
        with(density) {
            state.velocityThreshold = velocityThreshold.toPx()
        }
    }

    drag(
        onEnd = { velocity ->
            if (state.currentValue.x <= 0f) {
                if (velocity.x <= -state.velocityThreshold) {
                    state.swipeLeft()
                } else {
                    if (state.currentValue.x > -state.threshold) {
                        state.returnCenter()
                    } else {
                        state.swipeLeft()
                    }
                }
            } else {
                if (velocity.x >= state.velocityThreshold) {
                    state.swipeRight()
                } else {
                    if (state.currentValue.x < state.threshold) {
                        state.returnCenter()
                    } else {
                        state.swipeRight()
                    }
                }
            }
        },
        onDrag = { _, dragAmount ->
            if (state.isAnimationRunning.not()) {

                val x = state.snapInternalTo(
                    state.currentValue.x,
                    state.currentValue.x + dragAmount.x
                )
                val y = state.snapInternalTo(
                    state.currentValue.y,
                    state.currentValue.y + dragAmount.y
                )
                state.currentValue = Offset(x, y)
                val targetRotation = normalize(
                    state.center.x,
                    state.right.x,
                    abs(state.currentValue.x),
                    0f,
                    10f
                )
                state.rotate =
                    state.snapInternalTo(state.rotate, targetRotation * -state.currentValue.x.sign)

                state.scale.snapTo(
                    normalize(
                        state.center.x,
                        state.right.x / 3,
                        abs(state.currentValue.x),
                        0.8f
                    )
                )
            }
        }
    )
}

internal fun Modifier.drag(
    onEnd: suspend (velocity: Offset) -> Unit,
    onDrag: suspend (change: PointerInputChange, dragAmount: Offset) -> Unit
): Modifier = composed {
    var velocity by remember { mutableStateOf(Offset(0f, 0f)) }
    val scope = rememberCoroutineScope()
    Modifier.pointerInput(Unit) {
        detectDragGestures(
            onDragCancel = { scope.launch { onEnd(velocity) } },
            onDragEnd = { scope.launch { onEnd(velocity) } },
            onDrag = { change, dragAmount ->
                velocity = dragAmount
                scope.launch { onDrag(change, dragAmount) }
            }
        )
    }
}

internal fun normalize(
    min: Float,
    max: Float,
    v: Float,
    startRange: Float = 0f,
    endRange: Float = 1f
): Float {
    require(startRange < endRange) {
        "Start range is greater than End range"
    }
    val value = v.coerceIn(min, max)
    return (value - min) / (max - min) * (endRange - startRange) + startRange
}
