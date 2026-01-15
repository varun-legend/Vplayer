package com.varunlegend.vplayer.utils

import android.graphics.Matrix
import android.view.ScaleGestureDetector
import android.view.View
import android.view.MotionEvent
import android.view.TextureView
import androidx.media3.ui.PlayerView

/** Handles pinch-to-zoom and pan on PlayerView */
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
