package com.example.hmhr.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

private val LightColorScheme = lightColorScheme(
    primary = MainColor,
    secondary = SubColor,
    tertiary = SecondColor,
    background = Color.White,
    onPrimary = TextColor,
    onSecondary = TextColor,
    onBackground = TextColor,
)

private val CustomTypography = Typography(
    labelSmall = TextStyle(
        fontFamily = BoldLabelFont,
        fontSize = 12.sp
    )
)

@Composable
fun HmhrTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = CustomTypography,
        content = content
    )
}
