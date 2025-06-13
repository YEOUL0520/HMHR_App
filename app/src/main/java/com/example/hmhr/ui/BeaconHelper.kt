package com.example.hmhr.ui

import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.RemoteException
import android.util.Log
import android.widget.Toast
import com.example.hmhr.model.BeaconData
import com.example.hmhr.viewmodel.LoginViewModel
import org.altbeacon.beacon.*

class BeaconHelper(
    private val context: Context,
    private val loginViewModel: LoginViewModel,
    private val onBeaconDetected: (BeaconData) -> Unit
) : BeaconConsumer {

    private var beaconManager: BeaconManager? = null

    fun BeaconCreate() {
        beaconManager = BeaconManager.getInstanceForApplication(context).apply {
            beaconParsers.clear()
            beaconParsers.add(
                BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")
            )
        }
        beaconManager?.bind(this)
    }

    override fun onBeaconServiceConnect() {
        val region = Region("myBeaconRegion", null, null, null)
        beaconManager?.addRangeNotifier { beacons, _ ->
            val validBeacon = beacons.firstOrNull { it.distance > 0 && it.distance < 1.5 }

            if (validBeacon != null) {
                val data = BeaconData(
                    uuid = validBeacon.id1.toString(),
                    major = validBeacon.id2.toString(),
                    minor = validBeacon.id3.toString(),
                    distance = validBeacon.distance
                )
                onBeaconDetected(data)  // 인식 성공 콜백
            } else {
                loginViewModel.updateBeaconRecognitionResult("미인식 (1.5m 초과)")  // 👈 이걸 ViewModel에 새로 추가해도 됨
            }
        }
        try {
            beaconManager?.startRangingBeacons(region)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    override fun getApplicationContext() = context.applicationContext
    override fun unbindService(serviceConnection: ServiceConnection) = context.unbindService(serviceConnection)
    override fun bindService(intent: Intent?, serviceConnection: ServiceConnection, flags: Int) =
        intent?.let { context.bindService(it, serviceConnection, flags) } ?: false
}