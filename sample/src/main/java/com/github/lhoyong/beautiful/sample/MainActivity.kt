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
package com.github.lhoyong.beautiful.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberImagePainter
import com.github.lhoyong.beautiful.sample.ui.swipe.SwipeItem
import com.github.lhoyong.beautiful.sample.ui.theme.BeautifulTheme
import com.github.lhoyong.beautiful.swipe.BeautifulSwipe

class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val items = listOf(
            SwipeItem(imageUrl = "https://www.collinsdictionary.com/images/full/apple_158989157.jpg"),
            SwipeItem(imageUrl = "https://www.cookingclassy.com/wp-content/uploads/2020/04/bread-recipe-1.jpg"),
            SwipeItem(imageUrl = "https://www.maangchi.com/wp-content/uploads/2018/02/roasted-chicken-1.jpg"),
            SwipeItem("https://upload.wikimedia.org/wikipedia/commons/f/f6/15-09-26-RalfR-WLC-0098.jpg")
        )
        setContent {
            BeautifulTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    BeautifulSwipe(items) { item ->
                        Image(
                            painter = rememberImagePainter(item.imageUrl),
                            contentDescription = "",
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}
