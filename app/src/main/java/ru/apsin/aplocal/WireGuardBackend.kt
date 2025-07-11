package ru.apsin.aplocal

import ru.apsin.aplocal.nativebridge.NativeBridge
import ru.apsin.aplocal.WgActLogic.log

object WireGuardBackend {

    init {
        System.loadLibrary("wg-go")
    }

    fun startTunnel(configPath: String): Boolean {
        log("WireGuardBackend.startTunnel() вызван: $configPath")
        val result = NativeBridge.startTunnel(configPath)
        log("WireGuardBackend.startTunnel() вернул: $result")
        return result == 0
    }

    fun stopTunnel(): Boolean {
        log("WireGuardBackend.stopTunnel() вызван")
        val result = NativeBridge.stopTunnel()
        log("WireGuardBackend.stopTunnel() вернул: $result")
        return result == 0
    }
}
