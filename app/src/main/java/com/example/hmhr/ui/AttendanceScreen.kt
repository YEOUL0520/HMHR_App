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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.hmhr.R
import com.example.hmhr.ui.components.BottomNavigationBar
import com.example.hmhr.ui.theme.*
import com.example.hmhr.viewmodel.LoginViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@Composable
fun AttendanceScreen(modifier: Modifier = Modifier, loginViewModel: LoginViewModel) {
    AttendanceScreenContent(modifier, loginViewModel)
}

@Composable
fun AttendanceScreenContent(modifier: Modifier = Modifier, loginViewModel: LoginViewModel) {

    val attendanceInfo by loginViewModel.attendanceInfo.collectAsState()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
    ) {
        // 최상단 시스템 안내부
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

        // 달력 타이틀
        val today = LocalDate.now()
        Text(
            "${today.year}년 ${today.monthValue}월",
            fontFamily = BoldLabelFont,
            fontSize = 24.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 근무 타입 선택 탭 (예: 주간/야간/비번)
        var selectedShift by remember { mutableStateOf(0) }
        val shiftTypes = listOf("주간", "야간", "비번")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            shiftTypes.forEachIndexed { index, type ->
                val isSelected = selectedShift == index
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isSelected) MainColor else Color.LightGray)
                        .clickable { selectedShift = index }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = type,
                        color = if (isSelected) Color.White else Color.Black,
                        fontFamily = BoldLabelFont,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 달력
        CalendarGrid()
    }
}

@Composable
fun CalendarGrid() {
    val today = LocalDate.now()
    val yearMonth = YearMonth.of(today.year, today.month)
    val firstDayOfMonth = yearMonth.atDay(1)
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // 일요일 = 0

    val dates = buildList<LocalDate?> {
        repeat(firstDayOfWeek) { add(null) }
        for (day in 1..daysInMonth) {
            add(yearMonth.atDay(day))
        }
        while (size % 7 != 0) add(null) // 나머지 채우기
    }

    val weeks = dates.chunked(7)

    Column {
        // 요일 표시
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("일", "월", "화", "수", "목", "금", "토").forEach {
                Text(
                    text = it,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    fontFamily = BoldLabelFont
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // 날짜 셀
        weeks.forEachIndexed { index, week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                week.forEach { date ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (date != null) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "${date.dayOfMonth}",
                                    fontSize = 14.sp,
                                    fontFamily = LabelFont,
                                    color = if (date == LocalDate.now()) MainColor else Color.Black
                                )
                                if (date == LocalDate.now()) {
                                    Box(
                                        modifier = Modifier
                                            .width(20.dp)
                                            .height(2.dp)
                                            .background(MainColor)
                                    )
                                }
                            }
                        }
                    }
                }
            }
            if (index < weeks.lastIndex) {
                Spacer(modifier = Modifier.height(4.dp)) // 줄 간 간격 추가
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun AttendanceScreenPreview() {
    HmhrTheme {
        val dummyViewModel = LoginViewModel()  // 임시 ViewModel 생성
        AttendanceScreen(loginViewModel = dummyViewModel)
    }
}

