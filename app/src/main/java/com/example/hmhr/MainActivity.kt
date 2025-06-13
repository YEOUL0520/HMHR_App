package com.example.hmhr

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.RemoteException
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hmhr.ui.AppNavigation
import com.example.hmhr.ui.MainScreen
import com.example.hmhr.ui.theme.HmhrTheme
import com.example.hmhr.viewmodel.LoginViewModel

class MainActivity : ComponentActivity() {
    //private lateinit var beaconManager: BeaconManager
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()

        setContent {
            HmhrTheme {
                loginViewModel = viewModel()
                AppNavigation(loginViewModel)
            }
        }

        // 블루투스 활성화 요청
        if (BluetoothAdapter.getDefaultAdapter()?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivity(enableBtIntent)
        }
        requestPermissions()
    }

    private fun requestPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_SCAN)
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        }

        val launcher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            if (results.values.any { !it }) {
                Toast.makeText(this, "권한이 거부되어 비콘 인식 불가", Toast.LENGTH_LONG).show()
            }
        }

        launcher.launch(permissions.toTypedArray())
    }

}