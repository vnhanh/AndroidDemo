package com.vnhanh.common.android.view.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.abs
import kotlin.math.min


class AppZoomableImageView(context: Context, attr: AttributeSet?) :
    AppCompatImageView(context, attr) {
    private var _matrix: Matrix = Matrix()

    var mode: Int = NONE

    private var last: PointF = PointF()
    private var start: PointF = PointF()
    var minScale: Float = 1f
    var maxScale: Float = 4f
    var m: FloatArray

    var redundantXSpace: Float = 0f
    var redundantYSpace: Float = 0f
    var width: Float = 0f
    var height: Float = 0f
    var saveScale: Float = 1f
    var right: Float = 0f
    var bottom: Float = 0f
    var origWidth: Float = 0f
    var origHeight = 0f
    private var bmWidth: Float = 0f
    private var bmHeight: Float = 0f

    private var _scaleDetector: ScaleGestureDetector
    private var context: Context

    init {
        super.setClickable(true)
        this.context = context
        _scaleDetector = ScaleGestureDetector(context, ScaleListener())
        _matrix.setTranslate(1f, 1f)
        m = FloatArray(9)
        imageMatrix = _matrix
        scaleType = ScaleType.MATRIX

        setOnTouchListener { _, event ->
            _scaleDetector.onTouchEvent(event)
            _matrix.getValues(m)
            val x = m[Matrix.MTRANS_X]
            val y = m[Matrix.MTRANS_Y]
            val curr = PointF(event.x, event.y)

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    last[event.x] = event.y
                    start.set(last)
                    mode = DRAG
                }

                MotionEvent.ACTION_POINTER_DOWN -> {
                    last[event.x] = event.y
                    start.set(last)
                    mode = ZOOM
                }

                MotionEvent.ACTION_MOVE ->
                    //if the mode is ZOOM or
                    //if the mode is DRAG and already zoomed
                    if (mode == ZOOM || (mode == DRAG && saveScale > minScale)) {
                        var deltaX = curr.x - last.x // x difference
                        var deltaY = curr.y - last.y // y difference
                        val scaleWidth = Math.round(origWidth * saveScale)
                            .toFloat() // width after applying current scale
                        val scaleHeight = Math.round(origHeight * saveScale)
                            .toFloat() // height after applying current scale
                        //if scaleWidth is smaller than the views width
                        //in other words if the image width fits in the view
                        //limit left and right movement
                        if (scaleWidth < width) {
                            deltaX = 0f
                            if (y + deltaY > 0) deltaY = -y
                            else if (y + deltaY < -bottom) deltaY = -(y + bottom)
                        } else if (scaleHeight < height) {
                            deltaY = 0f
                            if (x + deltaX > 0) deltaX = -x
                            else if (x + deltaX < -right) deltaX = -(x + right)
                        } else {
                            if (x + deltaX > 0) deltaX = -x
                            else if (x + deltaX < -right) deltaX = -(x + right)

                            if (y + deltaY > 0) deltaY = -y
                            else if (y + deltaY < -bottom) deltaY = -(y + bottom)
                        }
                        //move the image with the matrix
                        _matrix.postTranslate(deltaX, deltaY)
                        //set the last touch location to the current
                        last[curr.x] = curr.y
                    }

                MotionEvent.ACTION_UP -> {
                    mode = NONE
                    val xDiff = abs((curr.x - start.x).toDouble()).toInt()
                    val yDiff = abs((curr.y - start.y).toDouble()).toInt()
                    if (xDiff < CLICK && yDiff < CLICK) performClick()
                }

                MotionEvent.ACTION_POINTER_UP -> mode = NONE
            }
            imageMatrix = _matrix
            invalidate()
            true
        }
    }

    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)
        bmWidth = bm.width.toFloat()
        bmHeight = bm.height.toFloat()
    }

    fun setMaxZoom(value: Float) {
        maxScale = value
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        width = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        height = MeasureSpec.getSize(heightMeasureSpec).toFloat()
        //Fit to screen.
        val scale: Float
        val scaleX = width / bmWidth
        val scaleY = height / bmHeight
        scale = min(scaleX.toDouble(), scaleY.toDouble()).toFloat()
        _matrix.setScale(scale, scale)
        imageMatrix = _matrix
        saveScale = 1f

        // Center the image
        redundantYSpace = height - (scale * bmHeight)
        redundantXSpace = width - (scale * bmWidth)
        redundantYSpace /= 2f
        redundantXSpace /= 2f

        _matrix.postTranslate(redundantXSpace, redundantYSpace)

        origWidth = width - 2 * redundantXSpace
        origHeight = height - 2 * redundantYSpace
        right = width * saveScale - width - (2 * redundantXSpace * saveScale)
        bottom = height * saveScale - height - (2 * redundantYSpace * saveScale)
        imageMatrix = _matrix
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            mode = ZOOM
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            var scaleFactor = detector.scaleFactor
            val origScale = saveScale
            saveScale *= scaleFactor
            if (saveScale > maxScale) {
                saveScale = maxScale
                scaleFactor = maxScale / origScale
            } else if (saveScale < minScale) {
                saveScale = minScale
                scaleFactor = minScale / origScale
            }
            right = width * saveScale - width - (2 * redundantXSpace * saveScale)
            bottom = height * saveScale - height - (2 * redundantYSpace * saveScale)
            if (origWidth * saveScale <= width || origHeight * saveScale <= height) {
                _matrix.postScale(scaleFactor, scaleFactor, width / 2, height / 2)

                // TODO: what happened if scaleFactor >= 1
                if (scaleFactor < 1) {
                    _matrix.getValues(m)
                    val x = m[Matrix.MTRANS_X]
                    val y = m[Matrix.MTRANS_Y]

                    if (Math.round(origWidth * saveScale) < width) {
                        if (y < -bottom) _matrix.postTranslate(0f, -(y + bottom))
                        else if (y > 0) _matrix.postTranslate(0f, -y)
                    } else {
                        if (x < -right) _matrix.postTranslate(-(x + right), 0f)
                        else if (x > 0) _matrix.postTranslate(-x, 0f)
                    }
                }
            } else {
                _matrix.postScale(scaleFactor, scaleFactor, detector.focusX, detector.focusY)
                _matrix.getValues(m)
                val x = m[Matrix.MTRANS_X]
                val y = m[Matrix.MTRANS_Y]
                if (scaleFactor < 1) {
                    if (x < -right) _matrix.postTranslate(-(x + right), 0f)
                    else if (x > 0) _matrix.postTranslate(-x, 0f)
                    if (y < -bottom) _matrix.postTranslate(0f, -(y + bottom))
                    else if (y > 0) _matrix.postTranslate(0f, -y)
                }
            }
            return true
        }
    }

    companion object {
        const val NONE: Int = 0
        const val DRAG: Int = 1
        const val ZOOM: Int = 2
        const val CLICK: Int = 3
    }
}
