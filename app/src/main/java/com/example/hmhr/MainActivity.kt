package com.example.hmhr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hmhr.ui.AppNavigation
import com.example.hmhr.ui.theme.HmhrTheme
import com.example.hmhr.viewmodel.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()

        setContent {
            HmhrTheme {
                val loginViewModel: LoginViewModel = viewModel()  // 💡 Activity 스코프 ViewModel
                AppNavigation(loginViewModel)
            }
        }
    }
}
