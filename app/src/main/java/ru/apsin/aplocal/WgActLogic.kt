package ru.apsin.aplocal

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object WgActLogic {

    fun readConfigFromAssets(context: Context): String? {
        return try {
            val input = context.assets.open("wg0.conf")
            val reader = BufferedReader(InputStreamReader(input))
            val result = reader.readText()
            reader.close()
            result
        } catch (e: Exception) {
            null
        }
    }
    private var logCallback: ((String) -> Unit)? = null


    fun log(text: String) {
        val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        logCallback?.invoke("[$time] $text")
    }

    fun startVpnWithConfig(context: Context, log: (String) -> Unit): Boolean {
        val config = readConfigFromAssets(context)
        if (config == null) {
            log("Ошибка чтения конфигурации WireGuard")
            return false
        }

        log("Конфигурация загружена. Запуск туннеля...")
        val result = WireGuardBackend.startTunnel(config)
        log("WireGuardBackend.startTunnel() вернул: $result")
        return result
    }

    fun stopVpn(log: (String) -> Unit): Boolean {
        log("Остановка туннеля WireGuard...")
        val result = WireGuardBackend.stopTunnel()
        log("WireGuardBackend.stopTunnel() вернул: $result")
        return result
    }

    // Можно установить логгер глобально, если нужно
    fun setLogger(log: (String) -> Unit) {
        // если понадобится централизованный логгер
    }
}
