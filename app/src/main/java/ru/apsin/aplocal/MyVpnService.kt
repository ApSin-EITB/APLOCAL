package ru.apsin.aplocal

import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import android.util.Log

class MyVpnService : VpnService() {

    private var vpnInterface: ParcelFileDescriptor? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val configPath = intent?.getStringExtra("config_path")
        if (configPath == null) {
            Log.e("MyVpnService", "Путь к конфигурации не передан")
            stopSelf()
            return START_NOT_STICKY
        }

        Thread {
            startVpnTunnel(configPath)
        }.start()

        return START_STICKY
    }

    private fun startVpnTunnel(configPath: String) {
        try {
            val builder = Builder()
            builder.setSession("WireGuard")
            builder.setMtu(1280)
            builder.addAddress("10.3.2.50", 32)
            builder.addDnsServer("1.1.1.1")
            builder.addRoute("0.0.0.0", 0)

            vpnInterface = builder.establish()
            if (vpnInterface == null) {
                Log.e("MyVpnService", "Не удалось создать VPN интерфейс")
                return
            }

            val pfd = ParcelFileDescriptor.dup(vpnInterface!!.fileDescriptor)
            val tunFd = pfd.detachFd() // ⚠️ detachFd передаёт право владения в Go
            val result = WireGuardBackend.startTunnelFromFile(configPath, tunFd)
            Log.i("MyVpnService", "startTunnel вернул: $result")

        } catch (e: Exception) {
            Log.e("MyVpnService", "Ошибка запуска VPN", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("MyVpnService", "Сервис уничтожается, останавливаем туннель")
        WireGuardBackend.stopTunnel()
        vpnInterface?.close()
    }
}
