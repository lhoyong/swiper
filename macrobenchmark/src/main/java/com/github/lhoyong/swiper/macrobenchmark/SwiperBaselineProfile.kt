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
package com.github.lhoyong.swiper.macrobenchmark

import android.content.Context
import android.graphics.Point
import androidx.benchmark.macro.ExperimentalBaselineProfilesApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalBaselineProfilesApi::class)
class SwiperBaselineProfile {
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    private lateinit var context: Context
    private lateinit var device: UiDevice

    @Before
    fun setUp() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        context = ApplicationProvider.getApplicationContext()
        device = UiDevice.getInstance(instrumentation)
    }

    @Test
    fun baselineProfiles() {
        baselineProfileRule.collectBaselineProfile(
            packageName = PACKAGE_NAME,
        ) {
            pressHome()
            startActivityAndWait()
            // Find the ScrollView in the Showcase route
            val obj = device.findObject(By.desc("swiper"))
            repeat(4) {
                obj.drag(Point(if (it % 2 == 0) 500 else -500, 0), 2000)
                device.wait(Until.findObject(By.desc("COMPOSE-IDLE")), 3000)
            }
        }
    }

    companion object {
        private const val PACKAGE_NAME = "com.github.lhoyong.swiper.sample"
    }
}
