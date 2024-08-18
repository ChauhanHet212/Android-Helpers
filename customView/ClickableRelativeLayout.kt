package com.example.helper.customView

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import com.example.helper.R

class ClickableRelativeLayout: RelativeLayout {
    constructor(context: Context): super(context) {
        initView(null)
    }
    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        initView(attrs)
    }

    var rippleColor: Int = Color.TRANSPARENT // default ripple color
        set(@ColorInt value) {
            field = value
            updateRippleDrawable()
        }
    var rippleOpacity: Int = 100 // default ripple opacity
        set(value) {
            require(value in 0..100) { "rippleOpacity must be between 0 to 100" }
            field = value
            updateRippleDrawable()
        }
    var cornerRadius: Float = 0f // default corner radius
        set(value) {
            require(value >= 0) { "cornerRadius must be greater than 0" }
            field = value
            updateRippleDrawable()
        }
    private var topLeftCornerRadius: Float = 0f // default top left corner radius
    private var topRightCornerRadius: Float = 0f // default top right corner radius
    private var bottomLeftCornerRadius: Float = 0f // default bottom left corner radius
    private var bottomRightCornerRadius: Float = 0f // default bottom right corner radius
    var ripplePosition: RipplePosition = RipplePosition.BACKGROUND // default ripple on background
        set(value) {
            field = value
            updateRippleDrawable()
        }

    enum class RipplePosition {
        BACKGROUND, FOREGROUND
    }

    fun initView(attrs: AttributeSet?) {
        // get attributes from xml
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.ClickableRelativeLayout)
            rippleColor = typedArray.getColor(R.styleable.ClickableRelativeLayout_rippleColor, Color.MAGENTA)
            rippleOpacity = typedArray.getInteger(R.styleable.ClickableRelativeLayout_rippleOpacity, 100).takeIf { it in 0..100 } ?: 100
            cornerRadius = typedArray.getDimension(R.styleable.ClickableRelativeLayout_cornerRadius, 0f)
            topLeftCornerRadius = typedArray.getDimension(R.styleable.ClickableRelativeLayout_topLeftCornerRadius, 0f)
            topRightCornerRadius = typedArray.getDimension(R.styleable.ClickableRelativeLayout_topRightCornerRadius, 0f)
            bottomLeftCornerRadius = typedArray.getDimension(R.styleable.ClickableRelativeLayout_bottomLeftCornerRadius, 0f)
            bottomRightCornerRadius = typedArray.getDimension(R.styleable.ClickableRelativeLayout_bottomRightCornerRadius, 0f)
            ripplePosition =
                if (typedArray.getInteger(R.styleable.ClickableRelativeLayout_ripplePosition, 0) == 0) {
                    RipplePosition.BACKGROUND
                } else {
                    RipplePosition.FOREGROUND
                }
            typedArray.recycle()
        }

        updateRippleDrawable()
    }

    private fun updateRippleDrawable() {
        val background = getRippleDrawable(rippleColor, rippleOpacity, cornerRadius, topLeftCornerRadius, topRightCornerRadius, bottomLeftCornerRadius, bottomRightCornerRadius)
        if (ripplePosition == RipplePosition.BACKGROUND) {
            this.foreground = background
        } else {
            this.background = background
        }
    }

    private fun getRippleDrawable(rippleColor: Int, rippleOpacity: Int, cornerRadius: Float, topLeftCornerRadius: Float, topRightCornerRadius: Float, bottomLeftCornerRadius: Float, bottomRightCornerRadius: Float): RippleDrawable {
        val rippleDrawable = RippleDrawable(
            ColorStateList.valueOf(rippleColor).withAlpha(rippleOpacity),
            null,
            ShapeDrawable(
                RoundRectShape(
                if (cornerRadius > 0) {
                    floatArrayOf(
                        cornerRadius, cornerRadius,
                        cornerRadius, cornerRadius,
                        cornerRadius, cornerRadius,
                        cornerRadius, cornerRadius
                    )
                } else {
                    floatArrayOf(
                        topLeftCornerRadius, topLeftCornerRadius,
                        topRightCornerRadius, topRightCornerRadius,
                        bottomRightCornerRadius, bottomRightCornerRadius,
                        bottomLeftCornerRadius, bottomLeftCornerRadius
                    )
                }, null, null
            )
            )
        )
        return rippleDrawable
    }


    fun setCornerRadius(topLeft: Float, topRight: Float, bottomLeft: Float, bottomRight: Float) {
        require(topLeft >= 0) { "topLeftCornerRadius must be greater than 0" }
        require(topRight >= 0) { "topRightCornerRadius must be greater than 0" }
        require(bottomLeft >= 0) { "bottomLeftCornerRadius must be greater than 0" }
        require(bottomRight >= 0) { "bottomRightCornerRadius must be greater than 0" }
        topLeftCornerRadius = topLeft
        topRightCornerRadius = topRight
        bottomLeftCornerRadius = bottomLeft
        bottomRightCornerRadius = bottomRight
        updateRippleDrawable()
    }
}