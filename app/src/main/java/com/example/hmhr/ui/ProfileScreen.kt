package com.example.hmhr.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hmhr.R
import com.example.hmhr.ui.components.BottomNavigationBar
import com.example.hmhr.ui.theme.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll


@Composable
fun ProfileScreen() {
    var selectedTabIndex by remember { mutableStateOf(2) }

    Scaffold(
        bottomBar = { BottomNavigationBar(selectedTabIndex) { selectedTabIndex = it } }
    ) { innerPadding ->
        val modifier = Modifier
            .fillMaxSize()
            //.padding(innerPadding)
            .padding(
                WindowInsets.systemBars
                    .only(WindowInsetsSides.Top + WindowInsetsSides.Bottom)
                    .asPaddingValues()
            )
            .padding(horizontal = 16.dp, vertical = 16.dp)

        when (selectedTabIndex) {
            0 -> AttendanceScreen()
            1 -> MainScreen()
            2 -> ProfileScreenContent(modifier)
        }
    }
}

@Composable
fun ProfileScreenContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
    ) {
        //최상단 시스템 안내부
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("출퇴근관리시스템", fontFamily = BoldLabelFont)
            Text("(주)A4AI", fontFamily = BoldLabelFont) //근로 장소 정보 받아올 곳
        }

        Spacer(modifier = Modifier.height(16.dp))

        //근로자 정보란
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = InactiveColor2, shape = RoundedCornerShape(8.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.account_circle),
                contentDescription = "image description",
                contentScale = ContentScale.None
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("근로자명", fontFamily = LabelFont, fontSize = 20.sp)
                Row{
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .background(color = InactiveColor, shape = CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("부서명", fontFamily = ParagraphFont)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .background(color = InactiveColor, shape = CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("직책", fontFamily = ParagraphFont)
                    }
                }
            }
            Box(
                modifier = Modifier
                    .size(80.dp, 32.dp)
                    .border(2.dp, SubColor, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text("3인 1교대", color = MainColor, fontFamily = BoldLabelFont)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 출퇴근 정보 확인
        Text("출근 정보 확인", fontFamily = BoldLabelFont, fontSize = 16.sp)

        Spacer(modifier = Modifier.height(8.dp))

        // 더미 데이터 목록
        val dummyData = listOf(
            Pair("4월 18일", "출근 09시 01분"),
            Pair("4월 18일", "퇴근 18시 01분"),
            Pair("4월 17일", "출근 09시 00분"),
            Pair("4월 17일", "퇴근 18시 00분"),
            Pair("4월 16일", "출근 08시 59분"),
            Pair("4월 16일", "퇴근 18시 03분")
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 80.dp) // 하단 바와 겹치지 않게
                .verticalScroll(rememberScrollState())
        ) {
            dummyData.forEach { (date, status) ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = date, fontFamily = LabelFont)
                        Text(
                            text = status,
                            fontFamily = LabelFont,
                            color = if (status.contains("출근")) MainColor else Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun ProfileScreenPreview() {
    HmhrTheme {
        ProfileScreen()
    }
}