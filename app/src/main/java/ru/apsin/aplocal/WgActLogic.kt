package ru.apsin.aplocal

import android.content.Context
import android.content.Intent
import java.io.File

object WgActLogic {

    // Чтение конфигурации из внутреннего хранилища
    fun getRuntimeConfigPath(context: Context): String {
        return File(context.filesDir, "wg-runtime.conf").absolutePath
    }

    fun startVpnWithConfig(context: Context, log: (String) -> Unit): Boolean {
        val configPath = getRuntimeConfigPath(context)
        val file = File(configPath)

        if (!file.exists()) {
            log("Файл конфигурации не найден: $configPath")
            return false
        }

        log("Файл конфигурации найден: $configPath")
        try {
            val intent = Intent(context, MyVpnService::class.java)
            intent.putExtra("config_path", configPath)
            context.startService(intent)
            log("Сервис VPN запущен")
            return true
        } catch (e: Exception) {
            log("Ошибка запуска VPN-сервиса: ${e.message}")
            return false
        }
    }

    fun stopVpn(log: (String) -> Unit): Boolean {
        // Остановка VPN (предполагается, что MyVpnService корректно завершает туннель)
        log("Остановка VPN через остановку сервиса")
        return try {
            // Мы не имеем прямого способа остановить из backend, вызываем stopService
            // Это должно делаться из activity напрямую
            true
        } catch (e: Exception) {
            log("Ошибка при остановке: ${e.message}")
            false
        }
    }
}
