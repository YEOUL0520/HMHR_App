package com.example.hmhr.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.example.hmhr.R

val HeadingFont = FontFamily(Font(R.font.pretendard_bold))
val LabelFont = FontFamily(Font(R.font.pretendard_semibold))
val BoldLabelFont = FontFamily(Font(R.font.pretendard_bold))
val ParagraphFont = FontFamily(Font(R.font.pretendard_regular))

val HmhrTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = HeadingFont,
        fontSize = 32.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = ParagraphFont,
        fontSize = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = LabelFont,
        fontSize = 16.sp
    ),
    labelMedium = TextStyle(
        fontFamily = BoldLabelFont,
        fontSize = 16.sp
    )
)
