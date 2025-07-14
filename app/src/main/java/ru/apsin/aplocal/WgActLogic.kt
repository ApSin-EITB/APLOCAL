package ru.apsin.aplocal

import android.content.Context
import android.util.Log
import ru.apsin.aplocal.nativebridge.NativeBridge

object WgActLogic {
    private const val TAG = "VpnModuleActivity"

    fun startVpnWithConfig(context: Context, configPath: String, tunFd: Int): Boolean {
        Log.i(TAG, "Запуск WireGuard туннеля с конфигурацией: $configPath и TUN FD: $tunFd")
        val result = WireGuardBackend.startTunnel(configPath, tunFd)
        Log.i(TAG, "WireGuardBackend.startTunnel() вернул: $result")
        return result == 0
    }

    fun stopVpn(): Boolean {
        Log.i(TAG, "Остановка WireGuard туннеля")
        val result = WireGuardBackend.stopTunnel()
        Log.i(TAG, "WireGuardBackend.stopTunnel() вернул: $result")
        return result == 0
    }
}
