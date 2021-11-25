package com.github.lhoyong.beautiful

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.github.lhoyong.beautiful.sample.ui.theme.BeautifulTheme
import com.github.lhoyong.beautiful.swipe.BeautifulSwipe

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BeautifulTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    BeautifulSwipe()
                }
            }
        }
    }
}