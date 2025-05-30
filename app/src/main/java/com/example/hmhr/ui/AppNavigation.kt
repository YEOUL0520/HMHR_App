package com.example.hmhr.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.hmhr.viewmodel.LoginViewModel
import com.example.hmhr.ui.components.BottomNavigationBar
import com.example.hmhr.ui.LoginScreen
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp



@Composable
fun AppNavigation(loginViewModel: LoginViewModel) {
    val isLoggedIn by loginViewModel.isLoggedIn.collectAsState()

    if (isLoggedIn) {
        MainScreenNavigation(loginViewModel)
    } else {
        LoginScreen(loginViewModel)
    }
}

@Composable
fun MainScreenNavigation(loginViewModel: LoginViewModel) {
    var selectedTabIndex by remember { mutableStateOf(1) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(selectedTabIndex) { selectedTabIndex = it }
        }
    ) { innerPadding ->
        val modifier = Modifier
            .fillMaxSize()
            .padding(
                WindowInsets.systemBars
                    .only(WindowInsetsSides.Top + WindowInsetsSides.Bottom)
                    .asPaddingValues()
            )
            .padding(horizontal = 16.dp, vertical = 16.dp)

        when (selectedTabIndex) {
            0 -> AttendanceScreen(modifier, loginViewModel)
            1 -> MainScreen(modifier, loginViewModel)
            2 -> ProfileScreen(modifier, loginViewModel)
        }
    }
}
