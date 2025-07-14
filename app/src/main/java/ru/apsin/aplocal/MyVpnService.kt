package ru.apsin.aplocal

import android.app.Service
import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import android.util.Log
import ru.apsin.aplocal.nativebridge.NativeBridge
import java.io.File
import java.io.FileOutputStream

class MyVpnService : VpnService() {

    private var vpnInterface: ParcelFileDescriptor? = null

    fun start(config: String): Boolean {
        try {
            val builder = Builder()
            builder.setSession("APLocal")
                .addAddress("10.3.2.50", 32)
                .addDnsServer("10.0.0.200")
                .addRoute("0.0.0.0", 0)
                .setMtu(1280)

            vpnInterface = builder.establish()
            if (vpnInterface == null) {
                Log.e("VpnModuleActivity", "Не удалось создать VPN-интерфейс")
                return false
            }

            val configFile = File(filesDir, "wg-runtime.conf")
            FileOutputStream(configFile).use { it.write(config.toByteArray()) }

            val fd = vpnInterface!!.fd
            Log.i("VpnModuleActivity", "Вызов NativeBridge.startTunnel($fd)")
            val result = NativeBridge.startTunnel(configFile.absolutePath, fd)
            Log.i("VpnModuleActivity", "WireGuard старт завершён с кодом: $result")
            return result == 0

        } catch (e: Exception) {
            Log.e("VpnModuleActivity", "Ошибка запуска VPN: ${e.message}", e)
            return false
        }
    }

    override fun onDestroy() {
        NativeBridge.stopTunnel()
        vpnInterface?.close()
        vpnInterface = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?) = null
}
