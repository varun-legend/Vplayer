package com.varunlegend.vplayer.utils

import android.graphics.Matrix
import android.view.ScaleGestureDetector
import android.view.View
import android.view.MotionEvent

/** Handles pinch-to-zoom and pan on PlayerView */
class ZoomPanListener(private val view: View) : View.OnTouchListener {
    private var scale = 1f
    private val matrix = Matrix()
    private val detector = ScaleGestureDetector(view.context,
        object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(det: ScaleGestureDetector): Boolean {
                scale = (scale * det.scaleFactor).coerceIn(1f, 4f)
                matrix.setScale(scale, scale, det.focusX, det.focusY)
                (view as? androidx.media3.ui.PlayerView)?.videoSurfaceView?.matrix = matrix
                return true
            }
        }
    )
    override fun onTouch(v: View, event: MotionEvent) = detector.onTouchEvent(event)
}
