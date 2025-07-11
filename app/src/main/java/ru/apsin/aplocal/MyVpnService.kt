package ru.apsin.aplocal

import android.app.Service
import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import android.util.Log
import java.io.IOException

class MyVpnService : VpnService() {
    private var vpnInterface: ParcelFileDescriptor? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Thread {
            startVpn()
        }.start()
        return Service.START_STICKY
    }

    private fun startVpn() {
        val builder = Builder()
        builder.setSession("WireGuardClient")
        builder.addAddress("10.3.2.50", 32)
        builder.addRoute("0.0.0.0", 0)
        builder.addDnsServer("1.1.1.1")
        try {
            vpnInterface = builder.establish()
            Log.i("MyVpnService", "VPN туннель поднят")
        } catch (e: Exception) {
            Log.e("MyVpnService", "Ошибка запуска VPN", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            vpnInterface?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        vpnInterface = null
        Log.i("MyVpnService", "VPN остановлен")
    }
}
