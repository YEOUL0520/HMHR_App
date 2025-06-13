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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.hmhr.R
import com.example.hmhr.ui.theme.*
import com.example.hmhr.viewmodel.LoginViewModel
import java.time.LocalDate
import java.time.YearMonth
import com.example.hmhr.util.ShiftCalculator
import com.example.hmhr.util.ShiftCategoryUtil
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun AttendanceScreen(modifier: Modifier = Modifier, loginViewModel: LoginViewModel) {
    AttendanceScreenContent(modifier, loginViewModel)
}

@Composable
fun AttendanceScreenContent(modifier: Modifier = Modifier, loginViewModel: LoginViewModel) {
    val today = LocalDate.now()
    val monthlyShiftMap by loginViewModel.monthlyShiftMap.collectAsState()
    val attendanceInfo by loginViewModel.attendanceInfo.collectAsState()

    var selectedShift by remember { mutableStateOf("전체") }
    val shiftTypes = listOf("전체", "주간", "야간", "비번")

    LaunchedEffect(Unit) {
        loginViewModel.calculateMonthlyShiftMap(today.year, today.monthValue)
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.Top) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("출퇴근관리시스템", fontFamily = BoldLabelFont)
            Text(attendanceInfo?.saupjangInfo ?: "(주)A4AI", fontFamily = BoldLabelFont)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = InactiveColor2, thickness = 1.dp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp))

        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.account_circle),
                contentDescription = null,
                contentScale = ContentScale.None,
                colorFilter = ColorFilter.tint(InactiveColor)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(attendanceInfo?.korname ?: "근로자명", fontFamily = LabelFont, fontSize = 20.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(4.dp).background(InactiveColor, CircleShape))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(attendanceInfo?.deptInfo ?: "근로부서명", fontFamily = ParagraphFont)
                }
            }
            Box(
                modifier = Modifier.size(80.dp, 32.dp).border(2.dp, SubColor, RoundedCornerShape(8.dp)).clip(RoundedCornerShape(8.dp)).background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(attendanceInfo?.guentaenm ?: "근무 형태", color = SubColor, fontFamily = BoldLabelFont)
            }
        }

        Divider(color = InactiveColor2, thickness = 1.dp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text("${today.year}년 ${today.monthValue}월", fontFamily = BoldLabelFont, fontSize = 24.sp, modifier = Modifier.padding(horizontal = 8.dp))
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            shiftTypes.forEach { type ->
                val isSelected = selectedShift == type
                Box(
                    modifier = Modifier.padding(end = 8.dp).clip(RoundedCornerShape(4.dp))
                        .background(if (isSelected) MainColor else Color.LightGray)
                        .clickable { selectedShift = type }.padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(text = type, color = if (isSelected) Color.White else Color.Black, fontFamily = BoldLabelFont, fontSize = 14.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        CalendarGrid(shiftMap = monthlyShiftMap, selectedFilter = selectedShift)
    }
}

@Composable
fun CalendarGrid(shiftMap: Map<LocalDate, ShiftCalculator.ShiftInfo>, selectedFilter: String) {
    val today = LocalDate.now()
    val yearMonth = YearMonth.of(today.year, today.month)
    val firstDayOfMonth = yearMonth.atDay(1)
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7

    val dates = buildList<LocalDate?> {
        repeat(firstDayOfWeek) { add(null) }
        for (day in 1..daysInMonth) add(yearMonth.atDay(day))
        while (size % 7 != 0) add(null)
    }

    val weeks = dates.chunked(7)

    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            listOf("일", "월", "화", "수", "목", "금", "토").forEach {
                Text(text = it, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontFamily = BoldLabelFont)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))

        weeks.forEachIndexed { index, week ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                week.forEach { date ->
                    Box(
                        modifier = Modifier.weight(1f).aspectRatio(1f).padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (date != null) {
                            val shift = shiftMap[date]
                            val category = ShiftCategoryUtil.classify(shift?.name)
                            val color = ShiftCategoryUtil.colorFor(category)

                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Top
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
                                Spacer(modifier = Modifier.height(4.dp)) // 👈 간격 추가해도 좋음

                                // 근무 형태 (필터 조건 만족 시만 표시)
                                if (shift != null && (selectedFilter == "전체" || shift.name == selectedFilter)) {
                                    Text(
                                        text = shift.name,
                                        fontSize = 10.sp,
                                        fontFamily = ParagraphFont,
                                        color = color
                                    )
                                }
                            }
                        }
                    }
                }
            }
            if (index < weeks.lastIndex) {
                Spacer(modifier = Modifier.height(4.dp))
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

