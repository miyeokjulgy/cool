package com.doodlecat.studypet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 80, 50, 50)
        }

        val title = TextView(this).apply {
            text = "낙서냥 공부친구"
            textSize = 28f
        }

        val description = TextView(this).apply {
            text = "고양이를 화면 위에 불러내서 같이 공부하자."
            textSize = 17f
        }

        val startButton = Button(this).apply {
            text = "고양이 불러오기"
            setOnClickListener {
                startCat()
            }
        }

        val stopButton = Button(this).apply {
            text = "고양이 집으로 보내기"
            setOnClickListener {
                stopService(Intent(this@MainActivity, CatOverlayService::class.java))
            }
        }

        layout.addView(title)
        layout.addView(description)
        layout.addView(startButton)
        layout.addView(stopButton)

        setContentView(layout)
    }

    private fun startCat() {
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
            return
        }

        startForegroundService(
            Intent(this, CatOverlayService::class.java)
        )
    }
}
