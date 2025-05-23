package com.example.hmhr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.hmhr.ui.theme.HmhrTheme
import LoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        actionBar?.hide()
        //supportActionBar?.hide()

        setContent {
            HmhrTheme {
                LoginScreen()
            }
        }
    }
}