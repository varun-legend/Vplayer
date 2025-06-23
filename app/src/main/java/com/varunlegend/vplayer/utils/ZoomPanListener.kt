package com.varunlegend.vplayer.utils
import android.graphics.Matrix
import android.view.ScaleGestureDetector
import android.view.View
import android.view.MotionEvent

class ZoomPanListener(private val target: View) : View.OnTouchListener {
    private var scaleFactor = 1f
    private val matrix = Matrix()
    private val scaleDetector = ScaleGestureDetector(target.context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector) = true.also {
            scaleFactor = (scaleFactor * detector.scaleFactor).coerceIn(1f, 4f)
            matrix.setScale(scaleFactor, scaleFactor, detector.focusX, detector.focusY)
            target.imageMatrix = matrix
        }
    })
    override fun onTouch(v: View, event: MotionEvent) = scaleDetector.onTouchEvent(event)
}
