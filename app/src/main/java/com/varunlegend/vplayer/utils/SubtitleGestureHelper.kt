package com.varunlegend.vplayer.utils

import android.view.GestureDetector
import android.view.MotionEvent
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import android.content.context

/** Detects flings to shift subtitle timing */
class SubtitleGestureHelper(
    context: Context,
    private val player: Player
) : GestureDetector.SimpleOnGestureListener() {
    private val detector = GestureDetector(context, this)
    fun onTouch(view: PlayerView, e: MotionEvent) = detector.onTouchEvent(e)

    override fun onFling(e1: MotionEvent, e2: MotionEvent, vx: Float, vy: Float): Boolean {
        val dx = e2.x - e1.x
        val shiftMs = if (dx > 0) 500 else -500
        player.seekTo(player.currentPosition + shiftMs)
        return true
    }
}
