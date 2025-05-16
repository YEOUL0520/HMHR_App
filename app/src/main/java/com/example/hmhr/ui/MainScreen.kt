import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.foundation.shape.CircleShape
import com.example.hmhr.R
import com.example.hmhr.ui.theme.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun MainScreen() {
    var selectedTabIndex by remember { mutableStateOf(1) }

    Scaffold(
        bottomBar = { BottomNavigationBar(selectedTabIndex) { selectedTabIndex = it } }
    ) { innerPadding ->

        val modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 16.dp, vertical = 16.dp)

        when (selectedTabIndex) {
            0 -> AttendanceScreen(modifier)
            1 -> HomeScreenContent(modifier)
            2 -> ProfileScreen(modifier)
        }
    }
}

@Composable
fun HomeScreenContent(modifier: Modifier = Modifier) {
    // 현재 날짜는 고정
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

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("출퇴근관리시스템", fontFamily = BoldLabelFont)
            Text("AJ대학교병원", fontFamily = BoldLabelFont)
        }

        Spacer(modifier = Modifier.height(16.dp))

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
                Text("근로부서명", fontFamily = ParagraphFont)
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

        Text(currentDate, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                //.background(color = InactiveColor)
                .padding(16.dp)
        ) {
            Row {
                // 왼쪽 8dp 너비 SubColor 박스
                Box(
                    modifier = Modifier
                        .width(8.dp)
                        .height(104.dp)
                        .background(SubColor)
                )

                Spacer(modifier = Modifier.width(14.dp))

                // 나머지 내용
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

                    Text("09:00 - 18:00", fontSize = 32.sp, fontFamily = BoldLabelFont)

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
            //colors = ButtonDefaults.buttonColors(containerColor = InactiveColor2)
        ) {
            Text("퇴근 시간 등록하기", color = InactiveColor, fontFamily = BoldLabelFont, fontSize = 16.sp)
        }
    }
}

@Composable
fun AttendanceScreen(modifier: Modifier = Modifier) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Text("출근부 화면입니다", fontSize = 20.sp)
    }
}

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Text("개인 정보 화면입니다", fontSize = 20.sp)
    }
}

@Composable
fun BottomNavigationBar(selectedIndex: Int, onItemSelected: (Int) -> Unit) {
    Column {
        Divider(color = InactiveColor2, thickness = 1.dp, modifier = Modifier.padding(start = 16.dp, end = 16.dp))  // 구분선
        NavigationBar(
            containerColor = Color.Transparent  // 이렇게 파라미터로 넣어야 함
        ) {
            NavigationBarItem(
                selected = selectedIndex == 0,
                onClick = { onItemSelected(0) },
                icon = { Image(painter = painterResource(id = R.drawable.calendar_month), contentDescription = "출근부") },
                label = { Text("출근부", fontFamily = BoldLabelFont, fontSize = 12.sp) }
            )
            NavigationBarItem(
                selected = selectedIndex == 1,
                onClick = { onItemSelected(1) },
                icon = { Image(painter = painterResource(id = R.drawable.edit_calendar), contentDescription = "등록") },
                label = { Text("등록", fontFamily = BoldLabelFont, fontSize = 12.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor = MainColor
                )
            )
            NavigationBarItem(
                selected = selectedIndex == 2,
                onClick = { onItemSelected(2) },
                icon = { Image(painter = painterResource(id = R.drawable.assignment_ind), contentDescription = "개인") },
                label = { Text("개인", fontFamily = BoldLabelFont, fontSize = 12.sp) }
            )
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
