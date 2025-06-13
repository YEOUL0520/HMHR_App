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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.hmhr.R
import com.example.hmhr.model.BeaconData
import com.example.hmhr.ui.theme.*
import com.example.hmhr.util.ShiftCategoryUtil
import com.example.hmhr.viewmodel.LoginViewModel
import kotlinx.coroutines.delay
import org.altbeacon.beacon.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.seconds


@Composable
fun MainScreen(modifier: Modifier = Modifier, loginViewModel: LoginViewModel) {
    val context = LocalContext.current

    val latestCallback = rememberUpdatedState<(BeaconData) -> Unit> {
        loginViewModel.updateBeaconData(it)
    }

    val beaconHelper = remember {
        BeaconHelper(context, loginViewModel) { beaconData ->
            latestCallback.value(beaconData)
        }
    }

    MainScreenContent(
        modifier = modifier,
        loginViewModel = loginViewModel,
        beaconHelper = beaconHelper
    )
}

@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel,
    beaconHelper: BeaconHelper
) {
    val context = LocalContext.current

    val currentDate = remember {
        LocalDate.now().format(DateTimeFormatter.ofPattern("M월 d일"))
    }
    var currentTime by remember { mutableStateOf("") }
    var showPopup by remember { mutableStateOf(false) }
    var popupMessage by remember { mutableStateOf("") }
    var shouldShowPopup by remember { mutableStateOf(false) }

    val attendanceInfo by loginViewModel.attendanceInfo.collectAsState()
    val beaconData by loginViewModel.beaconData.collectAsState()
    val beaconResult by loginViewModel.beaconRecognitionResult.collectAsState()
    val todayShift by loginViewModel.todayShift.collectAsState()
    val todayShiftTime by loginViewModel.todayShiftTime.collectAsState()

    val category = ShiftCategoryUtil.classify(todayShift)
    val shiftColor = ShiftCategoryUtil.colorFor(category)

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            delay(1.seconds)
        }
    }

    LaunchedEffect(beaconData) {
        val data = beaconData
        if (data != null && shouldShowPopup) {
            popupMessage = """
                출근 등록 완료: 비콘 인식됨
                UUID: ${data.uuid}
                Major: ${data.major}
                Minor: ${data.minor}
                Distance: ${data.distance}
            """.trimIndent()
            showPopup = true
            shouldShowPopup = false
        }
    }

    LaunchedEffect(attendanceInfo) {
        if (attendanceInfo != null) {
            loginViewModel.fetchGyodaeSetting()
        }
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

        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                    Box(
                        modifier = Modifier.size(4.dp).background(color = InactiveColor, shape = CircleShape)
                    )
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
        Text(currentDate, fontSize = 24.sp, fontFamily = BoldLabelFont, modifier = Modifier.padding(horizontal = 8.dp))
        Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row {
                Box(
                    modifier = Modifier.width(8.dp).height(104.dp).background(shiftColor)
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Row {
                        Box(
                            modifier = Modifier.background(Color.White, RoundedCornerShape(4.dp))
                                .border(2.dp, shiftColor, RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = todayShift ?: "근무 형태 없음",
                                fontSize = 20.sp,
                                color = shiftColor,
                                fontFamily = BoldLabelFont
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier.background(Color.White, RoundedCornerShape(4.dp))
                                .border(2.dp, InactiveColor, RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "출근전",
                                fontSize = 20.sp,
                                color = InactiveColor,
                                fontFamily = BoldLabelFont
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = todayShiftTime ?: "시간 정보 없음",
                        fontSize = 40.sp,
                        fontFamily = BoldLabelFont
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(8.dp).background(color = InactiveColor, shape = CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(attendanceInfo?.jikch ?: "직책", fontFamily = ParagraphFont)
                        Spacer(modifier = Modifier.width(16.dp))
                        Box(
                            modifier = Modifier.size(8.dp).background(color = InactiveColor, shape = CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(attendanceInfo?.jikwi ?: "직위", fontFamily = ParagraphFont)
                    }
                }
            }
        }

        Divider(color = InactiveColor2, thickness = 1.dp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp))
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("현재시각 ", color = InactiveColor, fontFamily = ParagraphFont)
            Text(currentTime, color = MainColor, fontFamily = LabelFont)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 출근 버튼
        Button(
            onClick = {
                val shiftName = todayShift ?: ""
                if (shiftName.isBlank() || shiftName.contains("비번")) {
                    popupMessage = "등록된 출근 정보가 없습니다."
                    showPopup = true
                } else {
                    shouldShowPopup = true
                    val now = LocalTime.now()
                    loginViewModel.registerAttendance("i", now) // 출근 등록
                    beaconHelper.BeaconCreate()
                }
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MainColor)
        ) {
            Text("출근 등록하기", color = Color.White, fontFamily = BoldLabelFont, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 퇴근 버튼
        OutlinedButton(
            onClick = {
                val shiftName = todayShift ?: ""
                if (shiftName.isBlank() || shiftName.contains("비번")) {
                    popupMessage = "등록된 출근 정보가 없습니다."
                    showPopup = true
                } else {
                    val now = LocalTime.now()
                    loginViewModel.registerAttendance("o", now) // 퇴근 등록
                    popupMessage = "퇴근 등록 완료: $now"
                    showPopup = true
                }
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(10.dp),
            enabled = beaconData != null
        ) {
            Text("퇴근 시간 등록하기", color = InactiveColor, fontFamily = BoldLabelFont, fontSize = 16.sp)
        }

        // 알림 팝업
        if (showPopup) {
            AlertDialog(
                onDismissRequest = { showPopup = false },
                confirmButton = {
                    TextButton(onClick = { showPopup = false }) {
                        Text("확인")
                    }
                },
                title = { Text("알림") },
                text = { Text(popupMessage) }
            )
        }

    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun MainScreenPreview() {
    HmhrTheme {
        val dummyViewModel = LoginViewModel()
        MainScreen(loginViewModel = dummyViewModel)
    }
}
