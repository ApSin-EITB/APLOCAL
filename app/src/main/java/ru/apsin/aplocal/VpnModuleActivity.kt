package ru.apsin.aplocal

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class VpnModuleActivity : AppCompatActivity() {

    private lateinit var logView: TextView
    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    private lateinit var startButton: Button
    private lateinit var stopButton: Button

    private val wifiReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val ssid = getCurrentSsid()
            appendLog("Обнаружено изменение сети. Текущий SSID: $ssid")

            if (ssid == "\"privatka\"") {
                appendLog("Подключен к доверенной сети — отключаем VPN")
                stopVpn()
            } else {
                appendLog("Неизвестная или отсутствующая Wi-Fi сеть — включаем VPN")
                startVpn()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vpn_module)

        logView = findViewById(R.id.vpnLogView)
        startButton = findViewById(R.id.vpnStartButton)
        stopButton = findViewById(R.id.vpnStopButton)

        startButton.setOnClickListener {
            appendLog("Кнопка запуска VPN нажата")
            startVpn()
        }

        stopButton.setOnClickListener {
            appendLog("Кнопка остановки VPN нажата")
            stopVpn()
        }

        registerReceiver(wifiReceiver, IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION))
        appendLog("Приёмник изменений Wi-Fi зарегистрирован")

        appendLog("UI готов. Ждём событий")
        updateNotification("VPN отключен")
    }

    private fun getCurrentSsid(): String? {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifiManager.connectionInfo?.ssid
    }

    private fun startVpn() {
        val ok = WgActLogic.startVpnWithConfig(this) { appendLog(it) }
        if (ok) updateNotification("VPN включен") else appendLog("Не удалось включить VPN")
    }

    private fun stopVpn() {
        val ok = WgActLogic.stopVpn { appendLog(it) }
        if (ok) updateNotification("VPN отключен") else appendLog("Не удалось отключить VPN")
    }

    private fun appendLog(text: String) {
        val now = timeFormat.format(Date())
        logView.append("[$now] $text\n")
    }

    private fun updateNotification(status: String) {
        appendLog("Обновление уведомления: $status")
        NotificationHelper.show(this, 101, NotificationHelper.createStatusNotification(this, status))
        findViewById<TextView>(R.id.vpnStatusText)?.text = "Статус: $status"
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(wifiReceiver)
        appendLog("Приёмник Wi-Fi удалён")
    }
}
