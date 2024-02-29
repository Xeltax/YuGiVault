package com.example.yugivault.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class OverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint: Paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    // Define the region of interest (ROI)
    private var roiLeft = 0f
    private var roiTop = 0f
    private var roiRight = 0f
    private var roiBottom = 0f

    fun setROI(left: Float, top: Float, right: Float, bottom: Float) {
        roiLeft = left
        roiTop = top
        roiRight = right
        roiBottom = bottom
        invalidate() // Trigger redraw
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(roiLeft, roiTop, roiRight, roiBottom, paint)
    }
}
