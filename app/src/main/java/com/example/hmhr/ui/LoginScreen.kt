package com.example.hmhr.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hmhr.R
import com.example.hmhr.ui.MainScreen
import com.example.hmhr.ui.theme.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.collectAsState
import com.example.hmhr.viewmodel.*

@Composable
fun LoginScreen(loginViewModel: LoginViewModel = viewModel()) {
    // ViewModel 상태 관찰 (StateFlow 또는 LiveData 기반)
    val isLoggedIn by loginViewModel.isLoggedIn.collectAsState()
    val errorMessage by loginViewModel.errorMessage.collectAsState()

    var id by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(true) }

    if (isLoggedIn) {
        MainScreen(modifier = Modifier, loginViewModel = loginViewModel)
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    WindowInsets.systemBars
                        .only(WindowInsetsSides.Top + WindowInsetsSides.Bottom)
                        .asPaddingValues()
                )
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color(0xFF9B9B9B), shape = RoundedCornerShape(4.dp))
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "(주)A4AI",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF262626),
                        fontFamily = HeadingFont
                    )
                )
                Text(
                    text = "출퇴근관리시스템",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF262626),
                        fontFamily = HeadingFont
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = id,
                    onValueChange = { id = it },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_user),
                            contentDescription = "아이디 아이콘",
                            tint = Color(0xFF757575)
                        )
                    },
                    placeholder = {
                        Text("휴대폰번호", style = TextStyle(fontSize = 14.sp, color = InactiveColor, fontFamily = LabelFont))
                    },
                    modifier = Modifier
                        .width(256.dp)
                        .height(48.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MainColor,
                        unfocusedBorderColor = InactiveColor2,
                        unfocusedContainerColor = InactiveColor2,
                        focusedContainerColor = InactiveColor2
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_lock),
                            contentDescription = "비밀번호 아이콘",
                            tint = Color(0xFF757575)
                        )
                    },
                    placeholder = {
                        Text("주민번호 앞 6자리", style = TextStyle(fontSize = 14.sp, color = InactiveColor, fontFamily = LabelFont))
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .width(256.dp)
                        .height(48.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MainColor,
                        unfocusedBorderColor = InactiveColor2,
                        unfocusedContainerColor = InactiveColor2,
                        focusedContainerColor = InactiveColor2
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                AutoLoginCheckbox(
                    isChecked = isChecked,
                    onCheckedChange = { isChecked = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        loginViewModel.login(id, password) //사용자 입력 값(id,password)을 login 함수의 인자로 전달
                    },
                    modifier = Modifier
                        .width(256.dp)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MainColor),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "로그인하기",
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = LabelFont)
                    )
                }

                //errorMessage가 존재할 경우 it으로 받아와서 해당 Message를 출력
                errorMessage?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "⚠️ $it", color = Color.Red, fontFamily = BoldLabelFont)
                }
            }
        }
    }
}

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
