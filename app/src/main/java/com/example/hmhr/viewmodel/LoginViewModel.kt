package com.example.hmhr.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hmhr.model.BeaconData
import com.example.hmhr.network.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.hmhr.util.ShiftCalculator
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter


class LoginViewModel : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _attendanceInfo = MutableStateFlow<AttendanceInfo?>(null)
    val attendanceInfo: StateFlow<AttendanceInfo?> = _attendanceInfo.asStateFlow()

    private val _guentaeList = MutableStateFlow<List<GuentaeManage>>(emptyList())
    val guentaeList: StateFlow<List<GuentaeManage>> = _guentaeList.asStateFlow()

    private val _beaconRecognitionResult = MutableStateFlow<String?>(null)
    val beaconRecognitionResult: StateFlow<String?> = _beaconRecognitionResult.asStateFlow()

    private val _beaconData = MutableStateFlow<BeaconData?>(null)
    val beaconData: StateFlow<BeaconData?> get() = _beaconData

    fun updateBeaconData(data: BeaconData) {
        _beaconData.value = data
    }

    fun login(id: String, password: String) {
        viewModelScope.launch {
            LoginService.loginRequest(id, password) { success, error ->
                _isLoggedIn.value = success
                _errorMessage.value = if (success) null else error
                if (success) {
                    fetchAttendanceInfo()
                }
            }
        }
    }

    private fun fetchAttendanceInfo() {
        LoginService.getAttendanceInfo { success, info ->
            if (success && info != null) {
                _attendanceInfo.value = info
                Log.d("ViewModel", "attendanceInfo 업데이트: $info")
            } else {
                _errorMessage.value = "근태 정보 가져오기 실패"
            }
        }
    }

    fun fetchGuentaeList() {
        viewModelScope.launch {
            LoginService.getGuentaeList { success, list ->
                if (success && list != null) {
                    _guentaeList.value = list
                    Log.d("ViewModel", "GuentaeList 업데이트: $list")
                } else {
                    Log.e("ViewModel", "GuentaeList 불러오기 실패")
                }
            }
        }
    }

    fun checkBeacon(uuid: String, major: String, minor: String, distance: Double) {
        viewModelScope.launch {
            LoginService.registerBeacon(uuid, major, minor, distance) { success, message ->
                _beaconRecognitionResult.value = when {
                    success -> "인식"
                    else -> message ?: "미인식"
                }
            }
        }
    }

    fun updateBeaconRecognitionResult(message: String) {
        _beaconRecognitionResult.value = message
    }

    private val _gyodaeSetting = MutableStateFlow<GyodaeSetting?>(null)
    val gyodaeSetting: StateFlow<GyodaeSetting?> = _gyodaeSetting

    private val _todayShift = MutableStateFlow<String?>(null)
    val todayShift: StateFlow<String?> = _todayShift

    private val _todayShiftTime = MutableStateFlow<String?>(null)
    val todayShiftTime: StateFlow<String?> = _todayShiftTime

    fun fetchGyodaeSetting() {
        LoginService.getGyodaeSetting { success, data ->
            if (success && data != null) {
                _gyodaeSetting.value = data
                calculateTodayShift(data)
            }
        }
    }

    //오늘 근무 정보 계산 - MainScreen.kt 와 연결
    fun calculateTodayShift(setting: GyodaeSetting) {
        val attendCode = attendanceInfo.value?.attendCode ?: return
        val startDate = attendanceInfo.value?.appdat ?: LocalDate.now().toString() // fallback
        val shiftInfo = ShiftCalculator.getTodayShift(setting, attendCode, startDate)

        if (shiftInfo != null) {
            _todayShift.value = shiftInfo.name
            _todayShiftTime.value = "${shiftInfo.startTime} - ${shiftInfo.endTime}"
        } else {
            _todayShift.value = "정보 없음"
            _todayShiftTime.value = "00:00 - 00:00"
        }
    }

    // 월간 근무표 계산 - AttendanceScreen.kt 와 연결
    private val _monthlyShiftMap = MutableStateFlow<Map<LocalDate, ShiftCalculator.ShiftInfo>>(emptyMap())
    val monthlyShiftMap: StateFlow<Map<LocalDate, ShiftCalculator.ShiftInfo>> = _monthlyShiftMap

    fun calculateMonthlyShiftMap(year: Int, month: Int) {
        val setting = _gyodaeSetting.value ?: return
        val attendCode = _attendanceInfo.value?.attendCode ?: return
        val startDate = _attendanceInfo.value?.appdat ?: return  // 입사일 (예: "2025-03-04")

        Log.d("ShiftViewModel", "계산 실행됨 - gyodae: $setting / attendCode: $attendCode / startDate: $startDate")

        val monthlyMap = ShiftCalculator.getMonthlyShifts(setting, attendCode, startDate, year, month)
        _monthlyShiftMap.value = monthlyMap
    }

    fun registerAttendance(iotype: String, time: LocalTime) {
        val nowDate = LocalDate.now()

        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

        val nowTimeStr = time.format(timeFormatter)
        val nowDateStr = nowDate.format(dateFormatter)

        sendAttendanceRecord(
            iotype = iotype,
            nowTimeStr = nowTimeStr,
            nowDateStr = nowDateStr
        )
    }

    fun sendAttendanceRecord(iotype: String, nowTimeStr: String, nowDateStr: String) {
        val attendance = _attendanceInfo.value ?: return
        val gyodae = _gyodaeSetting.value ?: return

        val now = LocalTime.now()
        val shift = ShiftCalculator.getTodayShift(gyodae, attendance.attendCode, attendance.appdat)
        val verdict = ShiftCalculator.calculateVerdict(iotype, now, shift)

        val empno = attendance.empno
        val dept = attendance.deptInfo
        val cmpcd = attendance.saupjangInfo
        val guentae = attendance.attendCode

        Log.d("AttendanceSend", "보낼 JSON → 날짜: $nowDateStr / 시간: $nowTimeStr / 구분: $iotype / 판별: $verdict")

        LoginService.sendAttendanceRecord(
            empno = empno,
            dept = dept,
            cmpcd = cmpcd,
            workdat = nowDateStr,
            worktime = nowTimeStr,
            iotype = iotype,
            verdicttime = verdict,
            guentae = guentae
        ) { success, message ->
            if (success) {
                Log.d("AttendanceSend", "서버 전송 성공")
            } else {
                Log.e("AttendanceSend", "서버 전송 실패: $message")
            }
        }
    }

}
