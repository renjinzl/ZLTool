package com.renjinzl.zltool.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.FrameLayout

class RoundLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    FrameLayout(context, attrs, defStyleAttr) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    private val outerPath = Path()
    private val innerPath = Path()

    private val outerRectF = RectF()
    private val innerRectF = RectF()
    private val roundRadii = floatArrayOf(0f,0f,0f,0f,0f,0f,0f,0f)

    var topLeftRadius = 20f
    var topRightRadius = 20f
    var bottomRightRadius = 20f
    var bottomLeftRadius = 20f
    var strokeWidth = 0f

    private val paint = Paint()
    private val xFerMode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)

    init {
        onRadiusChanged();
    }

    fun onRadiusChanged() {
        roundRadii[0] = topLeftRadius - strokeWidth
        roundRadii[1] = topLeftRadius - strokeWidth
        roundRadii[2] = topRightRadius - strokeWidth
        roundRadii[3] = topRightRadius - strokeWidth
        roundRadii[4] = bottomRightRadius - strokeWidth
        roundRadii[5] = bottomRightRadius - strokeWidth
        roundRadii[6] = bottomLeftRadius - strokeWidth
        roundRadii[7] = bottomLeftRadius - strokeWidth
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (topLeftRadius == 0f && topRightRadius == 0f && bottomRightRadius == 0f && bottomLeftRadius == 0f)
            return

        paint.reset()
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.xfermode = xFerMode

        outerPath.reset()
        innerPath.reset()
        outerPath.addRect(outerRectF, Path.Direction.CCW)
        innerPath.addRoundRect(innerRectF, roundRadii, Path.Direction.CCW)
        outerPath.op(innerPath, Path.Op.DIFFERENCE)
        canvas.drawPath(outerPath,paint)
        paint.xfermode = null
        canvas.restore()
    }
}