/*
 MIT License
 
 Copyright (c) 2026 Varun Prasath
 
 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:
 
 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.
 
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
*/

package com.varunlegend.vplayer.utils
import android.app.AlertDialog
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView


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
                
                
            }
            .show()
    }
}
