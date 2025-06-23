package com.varunlegend.vplayer.utils
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.media3.ui.PlayerView

class SubtitleGestureHelper(private val playerView: PlayerView,
    private val onShift: (Int) -> Unit, private val onTextSizeChange: (Int) -> Unit)
  : GestureDetector.SimpleOnGestureListener() {
    override fun onFling(e1: MotionEvent, e2: MotionEvent, vx: Float, vy: Float) = true.also {
        if (kotlin.math.abs(e2.x - e1.x) > kotlin.math.abs(e2.y - e1.y))
            onShift(if (e2.x > e1.x) 250 else -250)
        else onTextSizeChange(if (e2.y < e1.y) 1 else -1)
    }
  }
