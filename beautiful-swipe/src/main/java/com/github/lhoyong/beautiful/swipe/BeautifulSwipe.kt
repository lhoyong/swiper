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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

@Composable
fun BeautifulSwipe(
    items: List<SwipeItem>
) {
    Box {
        items.forEach { item ->
            BeautifulSwipeItem(item = item)
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
    item: SwipeItem
) {
    Card(
        modifier = Modifier
            .size(width = width, height = height)
            .padding(padding),
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
