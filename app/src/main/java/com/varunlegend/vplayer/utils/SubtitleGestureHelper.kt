package com.varunlegend.vplayer.utils

import android.app.AlertDialog
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView

/** Detects flings to shift subtitle timing and handles subtitle settings */
class SubtitleGestureHelper(
    private val context: Context,
    private val player: Player
) : GestureDetector.SimpleOnGestureListener() {

    private val detector = GestureDetector(context, this)

    fun onTouch(view: PlayerView, e: MotionEvent): Boolean {
        return detector.onTouchEvent(e)
    }

    @Suppress("NOTHING_TO_OVERRIDE", "ACCIDENTAL_OVERRIDE")
    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        if (e1 == null || e2 == null) return false
        val dx = e2.x - e1.x
        val shiftMs = if (dx > 0) 500 else -500
        player.seekTo(player.currentPosition + shiftMs)
        return true
    }

    fun showSubtitleTimingDialog() {
        val labels = arrayOf("-2s", "-1s", "0s", "+1s", "+2s")
        val values = longArrayOf(-2000L, -1000L, 0L, 1000L, 2000L)

        AlertDialog.Builder(context)
            .setTitle("Subtitle Delay")
            .setItems(labels) { _, which ->
                val newPosition = player.currentPosition + values[which]
                player.seekTo(newPosition)
            }
            .show()
    }

    fun showSubtitleSizeDialog() {
        val sizes = arrayOf("Small", "Medium", "Large")
        val scaleValues = floatArrayOf(0.75f, 1f, 1.5f)

        AlertDialog.Builder(context)
            .setTitle("Subtitle Size")
            .setItems(sizes) { _, which ->
                // Future: connect this to a subtitle text view
                // subtitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f * scaleValues[which])
            }
            .show()
    }
}
