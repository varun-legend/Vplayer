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
import android.graphics.Matrix
import android.view.ScaleGestureDetector
import android.view.View
import android.view.MotionEvent
import android.view.TextureView
import androidx.media3.ui.PlayerView


class ZoomPanListener(private val playerView: PlayerView) : View.OnTouchListener {

    private var scale = 1f
    private val matrix = Matrix()

    private val scaleDetector = ScaleGestureDetector(playerView.context,
        object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                scale = (scale * detector.scaleFactor).coerceIn(1f, 4f)
                matrix.setScale(scale, scale, detector.focusX, detector.focusY)
                applyMatrix()
                return true
            }
        }
    )

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return scaleDetector.onTouchEvent(event)
    }

    fun resetZoom() {
        scale = 1f
        matrix.reset()
        applyMatrix()
    }

    private fun applyMatrix() {
        val textureView = playerView.videoSurfaceView as? TextureView
        textureView?.setTransform(matrix)
        textureView?.invalidate()
    }
}
