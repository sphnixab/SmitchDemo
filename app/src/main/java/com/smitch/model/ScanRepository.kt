package com.smitch.model

import android.bluetooth.BluetoothManager
import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.net.InetAddress
import javax.inject.Inject

class MyRepository @Inject constructor(private var dnsSdManager: NsdManager) {
    private var nsdServiceInfo: NsdServiceInfo? = null
    suspend fun scan(): List<ScanResult> = withContext(Dispatchers.IO) {
        val results = mutableListOf<ScanResult>()

        val discoveryListener = object : NsdManager.DiscoveryListener {
            override fun onDiscoveryStarted(serviceType: String) {}

            override fun onServiceFound(serviceInfo: NsdServiceInfo) {
                if (serviceInfo.serviceType == "_http._tcp.") {
                    val ipAddress = serviceInfo.host?.hostAddress ?: ""
                    results.add(
                        ScanResult(
                            serviceName = serviceInfo.serviceName,
                            serviceType = serviceInfo.serviceType,
                            ipAddress = ipAddress,
                            port = serviceInfo.port
                        )
                    )
                }
            }

            override fun onServiceLost(serviceInfo: NsdServiceInfo) {}

            override fun onDiscoveryStopped(serviceType: String) {}

            override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {}

            override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {}
        }

        dnsSdManager.discoverServices("_http._tcp.", NsdManager.PROTOCOL_DNS_SD, discoveryListener)
        delay(SCAN_DURATION_MS)
        dnsSdManager.stopServiceDiscovery(discoveryListener)

        results
    }

    suspend fun publish() = withContext(Dispatchers.IO) {

        val serviceName = "MyService"
        val serviceType = "_http._tcp."
        val port = 80

        nsdServiceInfo?.apply {
            this.serviceName = "$serviceName$INSTANCE_SUFFIX"
            this.serviceType = serviceType
            this.port = port
        } ?: run {
            nsdServiceInfo = NsdServiceInfo().apply {
                this.serviceName = "$serviceName$INSTANCE_SUFFIX"
                this.serviceType = serviceType
                this.port = port
            }
        }
        Log.e(TAG, "nsdServiceInfo: $nsdServiceInfo")

        try {
            dnsSdManager.registerService(nsdServiceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener)
            Log.e(TAG, "Successfully registered service: $nsdServiceInfo")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to register service: $e")
        }
    }

    suspend fun findBLE() = withContext(Dispatchers.IO) {
    }

    companion object {
        private const val TAG = "MyRepository"
        private const val INSTANCE_SUFFIX = ".001"
        private const val SCAN_DURATION_MS = 2000L
    }

    private val registrationListener = object : NsdManager.RegistrationListener {
        override fun onServiceRegistered(serviceInfo: NsdServiceInfo) {
            nsdServiceInfo = serviceInfo
            Log.e(TAG, "Success to register service: $nsdServiceInfo")

        }

        override fun onRegistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            Log.e(TAG, "Failed to register service: $errorCode")
        }

        override fun onServiceUnregistered(serviceInfo: NsdServiceInfo) {
            nsdServiceInfo = null
        }

        override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            Log.e(TAG, "Failed to unregister service: $errorCode")
        }
    }
}

data class ScanResult(
    val serviceName: String,
    val serviceType: String,
    val ipAddress: String,
    val port: Int
)