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

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalMaterialApi
@RunWith(AndroidJUnit4::class)
class SwipeTest {

    @get:Rule
    val rule = createComposeRule()

    @Before
    fun setUp() {
        val items = listOf(1, 2, 3)
        rule.setContent {
            Swiper(count = items.size) {
                Text(text = it.toString())
            }
        }
    }

    @Test
    fun swipeLeft() {
        rule.onNodeWithText("1").performTouchInput {
            this.swipe(
                start = this.center,
                end = Offset(this.center.x - 500, this.center.y),
                durationMillis = 200
            )
        }
        rule.onNodeWithText("2").assertIsDisplayed()
    }

    @Test
    fun swipeLeftAndReturnToCenter() {
        rule.onNodeWithText("1").performTouchInput {
            this.swipe(
                start = this.center,
                end = Offset(this.center.x - 100, this.center.y),
                durationMillis = 200
            )
        }
        rule.onNodeWithText("1").assertIsDisplayed()
    }

    @Test
    fun swipeRight() {
        rule.onNodeWithText("1").performTouchInput {
            this.swipe(
                start = this.center,
                end = Offset(this.center.x + 500, this.center.y),
                durationMillis = 200
            )
        }
        rule.onNodeWithText("2").assertIsDisplayed()
    }

    @Test
    fun swipeRightAndReturnToCenter() {
        rule.onNodeWithText("1").performTouchInput {
            this.swipe(
                start = this.center,
                end = Offset(this.center.x + 100, this.center.y),
                durationMillis = 200
            )
        }
        rule.onNodeWithText("1").assertIsDisplayed()
    }
}
