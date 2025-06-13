package com.example.hmhr.util

import android.util.Log
import com.example.hmhr.network.GyodaeSetting
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object ShiftCalculator {
    data class ShiftInfo(
        val name: String,
        val startTime: String,
        val endTime: String
    )

    fun getTodayShift(
        setting: GyodaeSetting,
        attendCode: String,
        startDate: String,
        date: LocalDate = LocalDate.now()
    ): ShiftInfo? {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val baseDate = try {
            LocalDate.parse(startDate, formatter)
        } catch (e: Exception) {
            return null
        }

        val diffDays = ChronoUnit.DAYS.between(baseDate, date).toInt()

        Log.d("ShiftCalc", "Shift 계산 시작: $attendCode / $startDate / target=$date / diff=$diffDays")

        val shiftworkcd = setting.shiftworkcd  // 상근 여부
        val isNonShift = shiftworkcd == "Y"

        Log.d("ShiftCalc", "Shiftworkcd 확인: $shiftworkcd")

        return if (isNonShift) {
            // 상근자용: 요일 판단 (1=월~7=일)
            when (date.dayOfWeek.value) {
                in 1..5 -> {
                    if (setting.i_time.isNotEmpty()) {
                        ShiftInfo("주간", setting.i_time, setting.o_time)
                    } else {
                        ShiftInfo("비번", "09:00", "09:00")
                    }
                }
                6 -> {
                    if (setting.satyn == "Y") {
                        ShiftInfo("주간(토)", setting.sat_i_time, setting.sat_o_time)
                    } else {
                        ShiftInfo("비번", "09:00", "09:00")
                    }
                }
                7 -> {
                    if (setting.holiyn == "Y") {
                        ShiftInfo("주간(공휴일)", setting.hol_i_time, setting.hol_o_time)
                    } else {
                        ShiftInfo("비번", "09:00", "09:00")
                    }
                } // 공휴일 경우 임시로 일요일 기준 설정해둠
                else -> null
            }
        } else {
            // 교대근무자용: 반복 주기 계산
            val shiftList = listOf(
                Triple(setting.shiftgubuncd1, setting.i_time_1, setting.o_time_1),
                Triple(setting.shiftgubuncd2, setting.i_time_2, setting.o_time_2),
                Triple(setting.shiftgubuncd3, setting.i_time_3, setting.o_time_3),
                Triple(setting.shiftgubuncd4, setting.i_time_4, setting.o_time_4),
                Triple(setting.shiftgubuncd5, setting.i_time_5, setting.o_time_5)
            )

            val validShifts = shiftList.filter { (name, start, end) ->
                listOf(name, start, end).all {
                    !it.isNullOrBlank() && it.lowercase() != "null"
                }
            }
            val rotationSize = validShifts.size

            Log.d("ShiftCalc", "validShifts 확인: $validShifts")
            Log.d("ShiftCalc", "rotationSize 확인: $rotationSize")

            if (rotationSize == 0) return null

            val index = diffDays % rotationSize

            val selected = validShifts[index]

            Log.d("ShiftCalc", "index 확인: $index")
            Log.d("ShiftCalc", "selected 확인: $selected")

            val (name, start, end) = selected

            if (name != null && start != null && end != null) {
                ShiftInfo(name, start, end)
            } else null
        }
    }

    fun getMonthlyShifts(
        setting: GyodaeSetting,
        attendCode: String,
        baseDate: String,
        year: Int,
        month: Int
    ): Map<LocalDate, ShiftInfo> {
        val shiftMap = mutableMapOf<LocalDate, ShiftInfo>()
        val startDate = LocalDate.of(year, month, 1)
        val lastDay = startDate.lengthOfMonth()

        for (day in 1..lastDay) {
            val date = LocalDate.of(year, month, day)
            val shift = getTodayShift(setting, attendCode, baseDate, date)
            if (shift != null) {
                shiftMap[date] = shift
            }
        }

        return shiftMap
    }

    fun calculateVerdict(iotype: String, now: LocalTime, shift: ShiftCalculator.ShiftInfo?): String {
        if (shift == null) return "정상"  // 근무정보 없으면 무조건 정상 처리

        return when (iotype) {
            "i" -> {
                val expected = shift.startTime?.takeIf { it.isNotBlank() } ?: "00:00"
                val expectedTime = LocalTime.parse(expected)
                if (now.isAfter(expectedTime)) "지각" else "정상"
            }

            "o" -> {
                val expected = shift.endTime?.takeIf { it.isNotBlank() } ?: "23:59"
                val expectedTime = LocalTime.parse(expected)
                if (now.isBefore(expectedTime)) "조퇴" else "정상"
            }

            else -> "정상"
        }
    }


}
