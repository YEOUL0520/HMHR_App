package com.example.hmhr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.hmhr.ui.theme.HmhrTheme // ← 너가 설정한 테마 경로에 맞게 수정
import LoginScreen  // ← LoginScreen 경로에 맞게 수정

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HmhrTheme {
                LoginScreen()
            }
        }
    }
}
