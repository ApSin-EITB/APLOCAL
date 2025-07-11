package ru.apsin.aplocal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.vpnModuleButton).setOnClickListener {
            startActivity(Intent(this, VpnModuleActivity::class.java))
        }
    }
}
