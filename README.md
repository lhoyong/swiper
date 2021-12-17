## Swiper for Android Jetpack Compose

<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://github.com/lhoyong/swiper/actions"><img alt="Build Status" src="https://github.com/lhoyong/swiper/actions/workflows/check.yml/badge.svg"/></a>
  <a href="https://jitpack.io/#lhoyong/swiper"><img alt="Jitpack" src="https://jitpack.io/v/lhoyong/swiper.svg"/></a>
</p>

Android Jetpack Compose swipe library.

## Download

<img src="https://github.com/lhoyong/swiper/blob/main/art/swiper-sample1.gif" align="right" width="32%"/>

Gradle

First Add it in your root project `build.gradle`
~~~gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
~~~

Second Add dependency
~~~gradle
dependencies {
    implementation 'com.github.lhoyong.swiper:1.0.0'
}
~~~

## Basic Example
Swipe next items simply using a composable function. 
~~~kt
Swiper(
        items = items,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray),
        onSwiped = { /* TODO */ }, // swipe end callback
     ) { item ->
        // composable swipe item
        SwipeItem(item)
    }  
~~~
Go to a simple [Example.](https://github.com/lhoyong/swiper/blob/main/sample/src/main/java/com/github/lhoyong/swiper/sample/MainActivity.kt)

## License
```xml
Copyright 2021 by lhoyong

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
