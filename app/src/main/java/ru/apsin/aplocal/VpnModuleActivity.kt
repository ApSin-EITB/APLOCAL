package ru.apsin.aplocal

import android.app.Activity
import android.content.*
import android.net.VpnService
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class VpnModuleActivity : AppCompatActivity() {

    private lateinit var logView: TextView
    private lateinit var vpnStatusText: TextView

    private val configFileName = "wg-runtime.conf"
    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data ?: return@registerForActivityResult
            contentResolver.openInputStream(uri)?.use { input ->
                val file = File(filesDir, configFileName)
                file.outputStream().use { output -> input.copyTo(output) }
                appendLog("Конфигурация импортирована: ${file.absolutePath}")
                updateStatus("Конфигурация загружена")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vpn_module)

        logView = findViewById(R.id.logTextView)
        vpnStatusText = findViewById(R.id.vpnStatusText)

        findViewById<Button>(R.id.importConfigButton)?.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
            }
            filePickerLauncher.launch(intent)
        }

        findViewById<Button>(R.id.startVpnButton)?.setOnClickListener {
            startVpn()
        }

        findViewById<Button>(R.id.stopVpnButton)?.setOnClickListener {
            stopVpn()
        }

        appendLog("UI загружен, ожидаем действия пользователя")
        updateStatus("VPN отключен")
    }

    private fun startVpn() {
        val intent = VpnService.prepare(this)
        if (intent != null) {
            startActivityForResult(intent, 1)
        } else {
            onActivityResult(1, RESULT_OK, null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK) {
            appendLog("Разрешение на VPN получено")
            val ok = WgActLogic.startVpnWithConfig(this) { appendLog(it) }
            if (ok) {
                updateStatus("VPN включен")
            } else {
                updateStatus("Ошибка подключения")
            }
        } else {
            appendLog("VPN-соединение не разрешено пользователем")
            updateStatus("VPN не разрешён")
        }
    }

    private fun stopVpn() {
        stopService(Intent(this, MyVpnService::class.java))
        appendLog("Остановлен сервис VPN")
    }


    private fun appendLog(text: String) {
        val now = timeFormat.format(Date())
        logView.append("[$now] $text\n")
    }

    private fun updateStatus(status: String) {
        appendLog("Статус: $status")
        vpnStatusText.text = "Статус: $status"
    }
}
