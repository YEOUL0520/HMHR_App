package com.example.hmhr.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.hmhr.R
import com.example.hmhr.ui.components.BottomNavigationBar
import com.example.hmhr.ui.theme.*
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.seconds

@Composable
fun MainScreen() {
    var selectedTabIndex by remember { mutableStateOf(1) }

    Scaffold(
        topBar = {},
        bottomBar = {
            BottomNavigationBar(selectedTabIndex) { selectedTabIndex = it }
        }
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
            1 -> MainScreenContent(modifier)
            2 -> ProfileScreen()
        }
    }
}

@Composable
fun MainScreenContent(modifier: Modifier = Modifier) {
    val currentDate = remember {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("M월 d일")
        today.format(formatter)
    }

    var currentTime by remember { mutableStateOf("") }
    var checkInTime by remember { mutableStateOf<String?>(null) }
    var checkOutTime by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        while (true) {
            val now = LocalTime.now()
            val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
            currentTime = now.format(formatter)
            delay(1.seconds)
        }
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.Top) {
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

        //오늘 날짜
        Spacer(modifier = Modifier.height(16.dp))
        Text(currentDate, fontSize = 24.sp, fontFamily = BoldLabelFont)
        Spacer(modifier = Modifier.height(8.dp))

        //근무 카드란
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            //좌측 색 상자
            Row {
                Box(
                    modifier = Modifier
                        .width(8.dp)
                        .height(104.dp)
                        .background(SubColor)
                )

                Spacer(modifier = Modifier.width(14.dp))

                //우측 글자
                Column {
                    Row {
                        Text(
                            text = "주간",
                            fontSize = 20.sp,
                            color = MainColor,
                            fontFamily = BoldLabelFont,
                            modifier = Modifier
                                .background(Color.White, RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "출근전",
                            fontSize = 20.sp,
                            color = InactiveColor,
                            fontFamily = BoldLabelFont,
                            modifier = Modifier
                                .background(Color.White, RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("09:00 - 18:00", fontSize = 40.sp, fontFamily = BoldLabelFont)

                    Spacer(modifier = Modifier.height(8.dp))

                   Row(
                      verticalAlignment = Alignment.CenterVertically
                  ) {
                      Box(
                          modifier = Modifier
                                .size(8.dp)
                                .background(color = InactiveColor, shape = CircleShape)
                       )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("근로부서명", fontFamily = ParagraphFont)
                        }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("현재시각 ", color = InactiveColor, fontFamily = ParagraphFont)
            Text("$currentTime", color = MainColor, fontFamily = LabelFont)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val now = LocalTime.now()
                val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
                checkInTime = now.format(formatter)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MainColor)
        ) {
            Text("출근 시간 등록하기", color = Color.White, fontFamily = BoldLabelFont, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = {
                val now = LocalTime.now()
                val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
                checkOutTime = now.format(formatter)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(10.dp),
        ) {
            Text("퇴근 시간 등록하기", color = InactiveColor, fontFamily = BoldLabelFont, fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun MainScreenPreview() {
    HmhrTheme {
        MainScreen()
    }
}
