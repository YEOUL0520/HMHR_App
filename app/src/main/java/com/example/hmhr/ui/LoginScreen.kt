import com.example.hmhr.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import com.example.hmhr.ui.theme.*

@Composable
fun LoginScreen() {
    var isChecked by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 프로필 이미지 자리
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color(0xFF9B9B9B), shape = RoundedCornerShape(4.dp))
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 제목 텍스트
        Text(
            text = "AJ대학교병원",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF262626),
                textAlign = TextAlign.Center,
                fontFamily = HeadingFont
            )
        )
        Text(
            text = "출퇴근관리시스템",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF262626),
                textAlign = TextAlign.Center,
                fontFamily = HeadingFont
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 아이디 입력창
        OutlinedTextField(
            value = "",
            onValueChange = {},
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_user),
                    contentDescription = "아이디 아이콘",
                    tint = Color(0xFF757575)
                )
            },
            placeholder = {
                Text(
                    text = "아이디",
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = InactiveColor,
                        fontFamily = LabelFont
                    )
                )
            },
            modifier = Modifier
                .width(256.dp)
                .height(48.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MainColor,
                unfocusedBorderColor = InactiveColor2,
                unfocusedContainerColor = InactiveColor2,
                focusedContainerColor = InactiveColor2,
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 비밀번호 입력창
        OutlinedTextField(
            value = "",
            onValueChange = {},
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_lock),
                    contentDescription = "비밀번호 아이콘",
                    tint = Color(0xFF757575)
                )
            },
            placeholder = {
                Text(
                    text = "비밀번호",
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = InactiveColor,
                        fontFamily = LabelFont
                    )
                )
            },
            modifier = Modifier
                .width(256.dp)
                .height(48.dp),
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MainColor,
                unfocusedBorderColor = InactiveColor2,
                unfocusedContainerColor = InactiveColor2,
                focusedContainerColor = InactiveColor2,
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 자동 로그인 체크박스
        AutoLoginCheckbox(
            isChecked = isChecked,
            onCheckedChange = { isChecked = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 로그인 버튼
        Button(
            onClick = {},
            modifier = Modifier
                .width(256.dp)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4758EA)),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "로그인하기",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = LabelFont,
                    letterSpacing = 0.08.sp
                )
            )
        }
    }
}

//체크박스 컴포넌트
@Composable
fun AutoLoginCheckbox(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .width(256.dp)
            .height(32.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF4758EA),
                uncheckedColor = Color.LightGray,
                checkmarkColor = Color.White
            )
        )
        Text(
            text = "자동 로그인",
            fontSize = 14.sp,
            color = InactiveColor,
            fontFamily = LabelFont
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun LoginScreenPreview() {
HmhrTheme {
    LoginScreen()
}
}