package com.doodlecat.studypet

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.*
import android.widget.TextView
import androidx.core.app.NotificationCompat
import kotlin.math.abs

class CatOverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var cat: TextView
    private lateinit var params: WindowManager.LayoutParams

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()

        startForeground(
            1,
            NotificationCompat.Builder(this, "cat_channel")
                .setContentTitle("낙서냥이 같이 있는 중")
                .setContentText("오늘도 같이 공부하는 중이다.")
                .setSmallIcon(android.R.drawable.ic_menu_edit)
                .build()
        )

        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        cat = TextView(this).apply {
            text = """
                 /\_/\
                ( •ᴗ• )
                / >📖
            """.trimIndent()

            textSize = 22f
            gravity = Gravity.CENTER
            setPadding(20, 20, 20, 20)
        }

        params = WindowManager.LayoutParams(
            250,
            250,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 100
            y = 700
        }

        windowManager.addView(cat, params)

        var startX = 0
        var startY = 0
        var touchX = 0f
        var touchY = 0f

        cat.setOnTouchListener { _, event ->

            when (event.action) {

                MotionEvent.ACTION_DOWN -> {
                    startX = params.x
                    startY = params.y
                    touchX = event.rawX
                    touchY = event.rawY
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    params.x = startX + (event.rawX - touchX).toInt()
                    params.y = startY + (event.rawY - touchY).toInt()

                    windowManager.updateViewLayout(cat, params)
                    true
                }

                MotionEvent.ACTION_UP -> {

                    val moved =
                        abs(event.rawX - touchX) +
                        abs(event.rawY - touchY)

                    if (moved < 20) {
                        react()
                    }

                    true
                }

                else -> false
            }
        }
    }

    private fun react() {

        val reactions = listOf(
            """
             /\_/\
            ( ^ᴗ^ )
            / >♡
            """.trimIndent(),

            """
             /\_/\
            ( -ω- )
            / >📖
            """.trimIndent(),

            """
             /\_/\
            ( •ω• )
            / >✎
            """.trimIndent(),

            """
             /\_/\
            ( >ᴗ< )
            / >○
            """.trimIndent()
        )

        cat.text = reactions.random()
    }

    private fun createNotificationChannel() {

        val channel = NotificationChannel(
            "cat_channel",
            "낙서냥",
            NotificationManager.IMPORTANCE_LOW
        )

        val manager =
            getSystemService(NotificationManager::class.java)

        manager.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (::cat.isInitialized) {
            windowManager.removeView(cat)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
