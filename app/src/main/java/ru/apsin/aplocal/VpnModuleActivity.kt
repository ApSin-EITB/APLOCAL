package ru.apsin.aplocal

import android.app.Activity
import android.content.*
import android.net.VpnService
import android.os.*
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.*

class VpnModuleActivity : AppCompatActivity() {

    private lateinit var logView: TextView
    private val TAG = "VpnModuleActivity"
    private val configFileName = "wg-runtime.conf"

    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data ?: return@registerForActivityResult
            contentResolver.openInputStream(uri)?.use { input ->
                val file = File(filesDir, configFileName)
                file.outputStream().use { output -> input.copyTo(output) }
                appendLog("Конфигурация импортирована: ${file.absolutePath}")
            }
        }
    }

    private fun appendLog(text: String) {
        logView.append("[$TAG] $text\n")
        Log.i(TAG, text)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vpn_module)

        logView = findViewById(R.id.logTextView)

        findViewById<Button>(R.id.importConfigButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
            }
            filePickerLauncher.launch(intent)
        }

        findViewById<Button>(R.id.startVpnButton).setOnClickListener {
            startVpn()
        }

        findViewById<Button>(R.id.stopVpnButton).setOnClickListener {
            stopVpn()
        }
    }

    private fun startVpn() {
        val intent = VpnService.prepare(this)
        if (intent != null) {
            startActivityForResult(intent, 1)
        } else {
            onActivityResult(1, Activity.RESULT_OK, null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val vpnIntent = Intent(this, MyVpnService::class.java)
            startService(vpnIntent)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun stopVpn() {
        stopService(Intent(this, MyVpnService::class.java))
        WgActLogic.stopVpn()
    }
}
