package com.example.hmhr.network

import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import java.io.IOException
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.format.DateTimeFormatter

object LoginService {
    var savedAccessToken: String? = null
    var savedEmpno: String? = null

    fun loginRequest(phone: String, rrnFront: String, onResult: (Boolean, String?) -> Unit) {
        val client = OkHttpClient()

        val json = JSONObject().apply {
            put("mhmr_insa_001_mobno", phone)
            put("mhmr_insa_001_jmnoa", rrnFront)
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = json.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url("http://192.168.0.104:8000/UserMst/") // 추후 해당 주소 사설 주소로 변경
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onResult(false, "서버에 연결할 수 없습니다.")
            }

            override fun onResponse(call: Call, response: Response) {
                val bodyString = response.body?.string()
                if (response.isSuccessful && bodyString != null) {
                    try {
                        val json = JSONObject(bodyString)
                        val accessToken = json.optString("access_token", "")
                        val empno = json.optString("empno", "")
                        if (accessToken.isNotEmpty()) {
                            savedAccessToken = accessToken
                            savedEmpno = empno
                            onResult(true, null)
                        } else {
                            onResult(false, "로그인 정보가 올바르지 않습니다.")
                        }
                    } catch (e: Exception) {
                        onResult(false, "응답 처리 중 오류가 발생했습니다.")
                    }
                } else {
                    val statusCode = response.code
                    val message = when (statusCode) {
                        400 -> "요청 형식이 잘못되었습니다."
                        401 -> "아이디 또는 비밀번호가 올바르지 않습니다."
                        500 -> "서버에 문제가 발생했습니다. 잠시 후 다시 시도해주세요."
                        else -> "알 수 없는 오류가 발생했습니다. [오류 코드: $statusCode]"
                    }
                    onResult(false, message)
                }
            }
        })
    }

    fun getAttendanceInfo(onResult: (Boolean, AttendanceInfo?) -> Unit) {
        val client = OkHttpClient()
        val token = savedAccessToken ?: return onResult(false, null)

        val json = JSONObject().apply {
            put("Authorization", savedAccessToken)
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = json.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url("http://192.168.0.104:8000/AttendanceSetting") // 추후 해당 주소 사설 주소로 변경
            .post(requestBody)
            .addHeader("Authorization", "Bearer $token")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onResult(false, null)
            }

            override fun onResponse(call: Call, response: Response) {
                val bodyString = response.body?.string()
                //Method Not Allowed
                Log.d("LoginRequest", "응답 내용: $bodyString")
                if (response.isSuccessful && bodyString != null) {
                    try {
                        val jsonArray = JSONArray(bodyString)
                        if (jsonArray.length() > 0) {
                            val json = jsonArray.getJSONObject(0)
                            val info = AttendanceInfo(
                                saupjangInfo = json.optString("get_saupjang_info(mhmr_insa_001_cmpcd)", ""),
                                deptInfo = json.optString("get_dept_info(mhmr_insa_001_dept)", ""),
                                attendCode = json.optString("mhmr_insa_001_attendcd", ""),
                                guentaenm = if (json.optString("mhmr_attendance_001_guentaenm", "").length > 6){ json.optString("mhmr_attendance_001_guentaenm", "").substring(0, 6) }
                                else{json.optString("mhmr_attendance_001_guentaenm", "")}, // 글자수 조건 넣어서 3인 2교대(주야비) 경우에 3인 2교대 까지만 표시
                                empno = json.optString("mhmr_insa_001_empno", ""),
                                korname = json.optString("mhmr_insa_001_korname", ""),
                                jikjong = json.optString("mhmr_insa_001_jikjong", ""),
                                jikch = json.optString("mhmr_insa_001_jikch", ""),
                                jikwi = json.optString("mhmr_insa_001_jikwi", "")
                            )
                            onResult(true, info)
                        } else {
                            onResult(false, null)
                        }
                    } catch (e: Exception) {
                        onResult(false, null)
                    }
                } else {
                    // 일로
                    Log.e("AttendanceInfo", "응답 실패 또는 데이터 없음")
                    onResult(false, null)
                }
            }
        })
    }

    fun getGuentaeList(onResult: (Boolean, List<GuentaeManage>?) -> Unit) {
        val client = OkHttpClient()
        val token = savedAccessToken ?: return onResult(false, null)

        val json = JSONObject().apply{
            put("Authorization", savedAccessToken)
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = json.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url("http://192.168.0.104:8000/GuentaeManage")  // 추후 해당 주소 사설 주소로 변경
            .post(requestBody)
            .addHeader("Authorization", "Bearer $token")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("GuentaeList", "서버 연결 실패: ${e.message}")
                onResult(false, null)
            }

            override fun onResponse(call: Call, response: Response) {
                val bodyString = response.body?.string()
                if (response.isSuccessful && bodyString != null) {
                    try {
                        val jsonArray = JSONArray(bodyString)
                        val guentaelist = mutableListOf<GuentaeManage>()

                        val now = java.time.LocalDate.now()
                        val currentYearMonth = now.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMM"))

                        for (i in 0 until jsonArray.length()) {
                            val json = jsonArray.getJSONObject(i)
                            val workdat = json.optString("mhmr_attendance_003_workdat", "")
                            val worktime = json.optString("mhmr_attendance_003_worktime", "")
                            val iotype = json.optString("mhmr_attendance_003_iotype", "")

                            // 이번달 데이터만 추가
                            if (workdat.startsWith(currentYearMonth)) {
                                guentaelist.add(GuentaeManage(workdat, worktime, iotype))
                            }
                        }

                        onResult(true, guentaelist)
                    } catch (e: Exception) {
                        Log.e("GuentaeList", "응답 처리 중 오류: ${e.message}")
                        onResult(false, null)
                    }
                } else {
                    Log.e("GuentaeList", "응답 실패 또는 데이터 없음")
                    onResult(false, null)
                }
            }
        })
    }
}

data class AttendanceInfo(
    val saupjangInfo: String,
    val deptInfo: String,
    val attendCode: String, //g003이 이거임
    val guentaenm: String,
    val empno: String,
    val korname: String,
    val jikjong: String,
    val jikch: String,
    val jikwi: String
)

data class GuentaeManage(
    val workdat: String,
    val worktime: String,
    val iotype: String
)

data class BeaconRegister(
    val uuid: String,
    val major: String,
    val minor: String,
    val distance: Double
)

data class GodaeSetting(
    val shiftcd: String,
    val shiftgubuncd1: String,
    val i_time_1: String,
    val o_time_1: String,
    val shiftgubuncd2: String,
    val i_time_2: String,
    val o_time_2: String,
    val shiftgubuncd3: String,
    val i_time_3: String,
    val o_time_3: String,
    val shiftgubuncd4: String,
    val i_time_4: String,
    val o_time_4: String,
    val shiftgubuncd5: String,
    val i_time_5: String,
    val o_time_5: String
)