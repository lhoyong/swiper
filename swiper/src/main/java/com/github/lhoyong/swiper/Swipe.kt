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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.sign

internal class SwipeState(
    private val screenWidth: Float,
    private val animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec
) {
    /**
     * Whether the state is currently animating.
     */
    var isAnimationRunning: Boolean by mutableStateOf(false)
        private set

    var currentValue: Offset by mutableStateOf(Offset(0f, 0f))

    var rotate: Float by mutableStateOf(0f)

    var scale: Float by mutableStateOf(0f)

    val right = Offset(screenWidth, 0f)
    val center = Offset(0f, 0f)

    var threshold: Float = 0.0f

    var velocityThreshold by mutableStateOf(0f)

    var onAnimationEnd: () -> Unit = {}

    private suspend fun snapInternalTo(previous: Float, target: Float): Float {
        return Animatable(previous).apply {
            snapTo(target)
        }.value
    }

    private suspend fun animateInternalTo(
        previous: Float,
        target: Float,
        result: suspend (Float) -> Unit,
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

    suspend fun drag(dragAmount: Offset) {
        coroutineScope {
            launch {
                val x = snapInternalTo(
                    currentValue.x,
                    currentValue.x + dragAmount.x
                )
                val y = snapInternalTo(
                    currentValue.y,
                    currentValue.y + dragAmount.y
                )
                currentValue = Offset(x, y)
            }
            launch {
                val targetRotation = normalize(
                    center.x,
                    right.x,
                    abs(currentValue.x),
                    0f,
                    10f
                )
                rotate =
                    snapInternalTo(rotate, targetRotation * -currentValue.x.sign)
            }
            launch {
                scale = snapInternalTo(
                    scale,
                    normalize(
                        center.x,
                        right.x / 3,
                        abs(currentValue.x),
                        0.8f
                    )
                )
            }
        }
    }

    suspend fun runAnimation(direction: Direction) {
        when (direction) {
            Direction.Left -> swipeLeft()
            Direction.Right -> swipeRight()
            Direction.Center -> swipeCenter()
        }
    }

    private suspend fun swipeLeft() {
        isAnimationRunning = true
        try {
            coroutineScope {
                launch {
                    val distance = -screenWidth - (threshold / 2)
                    animateInternalTo(currentValue.x, distance) {
                        currentValue = Offset(it, currentValue.y)
                    }
                }
                launch {
                    animateInternalTo(scale, 1f) {
                        scale = it
                    }
                }
            }
            onAnimationEnd()
            coroutineScope {
                launch {
                    val x = snapInternalTo(currentValue.x, 0f)
                    val y = snapInternalTo(currentValue.y, 0f)
                    currentValue = Offset(x, y)
                }
                launch { rotate = snapInternalTo(rotate, 0f) }
            }
        } finally {
            isAnimationRunning = false
        }
    }

    private suspend fun swipeRight() {
        isAnimationRunning = true
        try {
            coroutineScope {
                launch {
                    val distance = screenWidth + (threshold / 2)
                    animateInternalTo(currentValue.x, distance) {
                        currentValue = Offset(it, currentValue.y)
                    }
                }
                launch {
                    animateInternalTo(scale, 1f) {
                        scale = it
                    }
                }
            }
            onAnimationEnd()
            coroutineScope {
                launch {
                    val x = snapInternalTo(currentValue.x, center.x)
                    val y = snapInternalTo(currentValue.y, 0f)
                    currentValue = Offset(x, y)
                }
                launch { rotate = snapInternalTo(rotate, 0f) }
            }
        } finally {
            isAnimationRunning = false
        }
    }

    private suspend fun swipeCenter() {
        isAnimationRunning = true
        try {
            coroutineScope {
                launch {
                    animateInternalTo(currentValue.x, center.x) {
                        currentValue = currentValue.copy(x = it)
                    }
                }
                launch {
                    animateInternalTo(currentValue.y, center.y) {
                        currentValue = currentValue.copy(y = it)
                    }
                }
                launch {
                    animateInternalTo(rotate, 0f) {
                        rotate = it
                    }
                }
                launch {
                    animateInternalTo(scale, 0.8f) {
                        scale = it
                    }
                }
            }
        } finally {
            isAnimationRunning = false
        }
    }
}

/* Swipe Direction */
internal enum class Direction {
    Left, Right, Center
}

@Composable
internal fun rememberSwipeState(
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec
): SwipeState {
    val screenWidth = with(LocalConfiguration.current) {
        LocalDensity.current.run { screenWidthDp.dp.toPx() }
    }
    return remember {
        SwipeState(screenWidth, animationSpec)
    }
}

@ExperimentalMaterialApi
internal fun Modifier.swipe(
    state: SwipeState,
    thresholdConfig: (Float, Float) -> ThresholdConfig = { _, _ -> FractionalThreshold(0.3f) },
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
                    state.runAnimation(Direction.Left)
                } else {
                    if (state.currentValue.x > -state.threshold) {
                        state.runAnimation(Direction.Center)
                    } else {
                        state.runAnimation(Direction.Left)
                    }
                }
            } else {
                if (velocity.x >= state.velocityThreshold) {
                    state.runAnimation(Direction.Right)
                } else {
                    if (state.currentValue.x < state.threshold) {
                        state.runAnimation(Direction.Center)
                    } else {
                        state.runAnimation(Direction.Right)
                    }
                }
            }
        },
        onDrag = { _, dragAmount ->
            if (state.isAnimationRunning.not()) {
                state.drag(dragAmount)
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
            onDragCancel = {
                scope.launch { onEnd(velocity) }
            },
            onDragEnd = {
                scope.launch { onEnd(velocity) }
            },
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
        "startRange must be less than endRange."
    }
    val value = v.coerceIn(min, max)
    return (value - min) / (max - min) * (endRange - startRange) + startRange
}
