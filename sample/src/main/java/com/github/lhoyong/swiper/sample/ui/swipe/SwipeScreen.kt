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
package com.github.lhoyong.swiper.sample.ui.swipe

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.github.lhoyong.swiper.Swiper

@ExperimentalMaterialApi
@Composable
fun SwipeScreen() {

    var items by remember { mutableStateOf(SwipeConst.initialItems) }

    Column {
        Swiper(
            items = items,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray)
                .weight(1f),
            onSwiped = {
                Log.i("Swiper", "end item : $it")
            }
        ) { item ->
            SwipeItem(item)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            TextButton(onClick = { items = SwipeConst.newItems }) {
                Text(text = "New Items")
            }
        }
    }
}

@Composable
fun SwipeItem(item: SwipeItem) {
    Card(
        modifier = Modifier.padding(start = 30.dp, top = 80.dp, end = 30.dp, bottom = 80.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = rememberImagePainter(item.imageUrl),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
    }
}

object SwipeConst {
    val initialItems = listOf(
        SwipeItem(imageUrl = "https://cdn.pixabay.com/photo/2022/01/12/07/57/bear-6932230_1280.jpg"),
        SwipeItem(imageUrl = "https://cdn.pixabay.com/photo/2021/12/23/03/05/iran-6888574__340.jpg"),
        SwipeItem(imageUrl = "https://cdn.pixabay.com/photo/2019/03/23/08/21/lioness-4074897__480.jpg"),
        SwipeItem("https://cdn.pixabay.com/photo/2021/09/12/18/07/robin-6619184__480.jpg")
    )

    val newItems = listOf(
        SwipeItem(imageUrl = "https://cdn.pixabay.com/photo/2021/10/18/19/04/mountains-6721870_1280.jpg"),
        SwipeItem(imageUrl = "https://cdn.pixabay.com/photo/2021/11/21/14/17/desert-6814275_1280.png"),
        SwipeItem(imageUrl = "https://cdn.pixabay.com/photo/2022/01/13/07/05/house-6934535_1280.jpg"),
        SwipeItem("https://cdn.pixabay.com/photo/2020/02/11/12/05/livigno-4839351_1280.jpg")
    )
}
