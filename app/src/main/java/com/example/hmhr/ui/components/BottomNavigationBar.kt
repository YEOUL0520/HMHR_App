package com.example.hmhr.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hmhr.R
import com.example.hmhr.ui.theme.*

@Composable
fun BottomNavigationBar(selectedIndex: Int, onItemSelected: (Int) -> Unit) {
    Column {
        Divider(
            color = InactiveColor2,
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        NavigationBar(
            containerColor = Color.Transparent,
            modifier = Modifier
        ) {
            NavigationBarItem(
                selected = selectedIndex == 0,
                onClick = { onItemSelected(0) },
                icon = {
                    Image(
                        painter = painterResource(id = R.drawable.calendar_month),
                        contentDescription = "출근부",
                        colorFilter = ColorFilter.tint(if (selectedIndex == 0) MainColor else InactiveColor)
                    )
                },
                label = { Text("출근부", fontFamily = BoldLabelFont, fontSize = 12.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor = MainColor,
                    unselectedTextColor = InactiveColor,
                    indicatorColor = Color.Transparent
                )
            )
            NavigationBarItem(
                selected = selectedIndex == 1,
                onClick = { onItemSelected(1) },
                icon = {
                    Image(
                        painter = painterResource(id = R.drawable.edit_calendar),
                        contentDescription = "등록",
                        colorFilter = ColorFilter.tint(if (selectedIndex == 1) MainColor else InactiveColor)
                    )
                },
                label = { Text("등록", fontFamily = BoldLabelFont, fontSize = 12.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor = MainColor,
                    unselectedTextColor = InactiveColor,
                    indicatorColor = Color.Transparent
                )
            )
            NavigationBarItem(
                selected = selectedIndex == 2,
                onClick = { onItemSelected(2) },
                icon = {
                    Image(
                        painter = painterResource(id = R.drawable.assignment_ind),
                        contentDescription = "개인",
                        colorFilter = ColorFilter.tint(if (selectedIndex == 2) MainColor else InactiveColor)
                    )
                },
                label = { Text("개인", fontFamily = BoldLabelFont, fontSize = 12.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor = MainColor,
                    unselectedTextColor = InactiveColor,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
