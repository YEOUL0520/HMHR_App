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
import androidx.compose.material3.Divider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hmhr.viewmodel.LoginViewModel
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.ui.graphics.ColorFilter


@Composable
fun ProfileScreen(modifier: Modifier = Modifier, loginViewModel: LoginViewModel) {
    ProfileScreenContent(modifier, loginViewModel)
}

@Composable
fun ProfileScreenContent(modifier: Modifier = Modifier, loginViewModel: LoginViewModel) {
    val attendanceInfo by loginViewModel.attendanceInfo.collectAsState()
    val guentaeList by loginViewModel.guentaeList.collectAsState()

    // 화면 진입 시 fetch 호출
    LaunchedEffect(Unit) {
        loginViewModel.fetchGuentaeList()
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.Top) {

        //최상단 시스템 안내부
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("출퇴근관리시스템", fontFamily = BoldLabelFont)
            Text(attendanceInfo?.saupjangInfo ?: "(주)A4AI", fontFamily = BoldLabelFont)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider(
            color = InactiveColor2,
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
        )

        //근로자 정보란
        Row(
            modifier = Modifier
                .fillMaxWidth()
                //.background(color = InactiveColor2, shape = RoundedCornerShape(8.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.account_circle),
                contentDescription = "image description",
                contentScale = ContentScale.None,
                colorFilter = ColorFilter.tint(InactiveColor)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(attendanceInfo?.korname ?: "근로자명", fontFamily = LabelFont, fontSize = 20.sp)
                Row {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .background(color = InactiveColor, shape = CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(attendanceInfo?.deptInfo ?: "근로부서명", fontFamily = ParagraphFont)
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
                Text(
                    attendanceInfo?.guentaenm ?: "근무 형태",
                    color = SubColor,
                    fontFamily = BoldLabelFont
                )
            }
        }

        Divider(
            color = InactiveColor2,
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 출퇴근 정보 확인
        Text("출근 정보 확인", fontFamily = BoldLabelFont, fontSize = 24.sp, modifier = Modifier.padding(horizontal = 8.dp))

        Spacer(modifier = Modifier.height(16.dp))

        // guentaeList를 UI에 표시
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 80.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (guentaeList.isNotEmpty()) {
                // guentaeList 데이터 표시
                guentaeList.forEach { item ->
                    val dateFormatted = item.workdat?.let {
                        "${it.substring(4, 6)}월 ${it.substring(6)}일"
                    } ?: "날짜없음"

                    val status = if (item.iotype == "i") {
                        "출근 ${item.worktime}"
                    } else {
                        "퇴근 ${item.worktime}"
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                            .background(InactiveColor2, RoundedCornerShape(8.dp))
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = dateFormatted, fontFamily = LabelFont)
                            Text(
                                text = status,
                                fontFamily = LabelFont,
                                color = if (status.contains("출근")) MainColor else InactiveColor
                            )
                        }
                    }
                }
            } else {
                // 🔥 데이터가 없을 경우 대체 박스
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                        .background(InactiveColor2, RoundedCornerShape(8.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "이번 달 등록된 출근 정보가 없습니다.",
                        fontFamily = LabelFont,
                        color = InactiveColor
                    )
                }
            }
        }

    }
}


@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun ProfileScreenPreview() {
    HmhrTheme {
        val dummyViewModel = LoginViewModel()  // 임시 ViewModel 생성
        ProfileScreen(loginViewModel = dummyViewModel)
    }
}