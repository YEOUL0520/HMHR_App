package com.example.hmhr.util

import androidx.compose.ui.graphics.Color
import com.example.hmhr.ui.theme.*

object ShiftCategoryUtil {
    enum class Category {
        주간, 야간, 비번, 기타
    }

    fun classify(name: String?): Category {
        return when {
            name == null -> Category.기타
            name.contains("주간") -> Category.주간
            name.contains("야간") -> Category.야간
            name.contains("비번") -> Category.비번
            else -> Category.기타
        }
    }

    fun colorFor(category: Category): Color {
        return when (category) {
            Category.주간 -> ThirdColor
            Category.야간 -> SubColor
            Category.비번 -> InactiveColor
            Category.기타 -> Color.Black
        }
    }
}
