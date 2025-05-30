package com.example.hmhr.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hmhr.network.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _attendanceInfo = MutableStateFlow<AttendanceInfo?>(null)
    val attendanceInfo: StateFlow<AttendanceInfo?> = _attendanceInfo.asStateFlow()

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

    // LoginViewModel.kt
    private val _guentaeList = MutableStateFlow<List<GuentaeManage>>(emptyList())
    val guentaeList: StateFlow<List<GuentaeManage>> = _guentaeList.asStateFlow()

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

}
