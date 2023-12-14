package com.east.east_utils.widgets.textview

import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.LineHeightSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *
 * @description:无内边距TextView控件
 * @author: East
 * @date: 2019-09-24
 * |---------------------------------------------------------------------------------------------------------------|
 */
class NoPaddingTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        includeFontPadding = false
    }


    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(getCustomText(text), type)
    }

    private fun getCustomText(text: CharSequence?): SpannableStringBuilder? {
        if (text == null) {
            return null
        }

        return SpannableStringBuilder(text).apply {
            setSpan(
                object : LineHeightSpan {
                    override fun chooseHeight(
                        text: CharSequence,
                        start: Int,
                        end: Int,
                        spanstartv: Int,
                        lineHeight: Int,
                        fm: Paint.FontMetricsInt
                    ) {
                        val rect = Rect()
                        paint.getTextBounds(text.toString(), 0, text.length, rect)

                        val viewHeight = fm.descent - fm.ascent
                        val textHeight = kotlin.math.max(textSize.toInt(), rect.bottom - rect.top)

                        val paddingTop = kotlin.math.abs(fm.ascent - rect.top)
                        val paddingBottom = fm.descent - rect.bottom

                        val minPadding = kotlin.math.min(paddingTop, paddingBottom)
                        val avgPadding = (viewHeight - textHeight) / 2

                        when {
                            avgPadding < minPadding -> {
                                fm.ascent += avgPadding
                                fm.descent -= avgPadding
                            }
                            paddingTop < paddingBottom -> {
                                fm.ascent = rect.top
                                fm.descent = textHeight + fm.ascent
                            }
                            else -> {
                                fm.descent = rect.bottom
                                fm.ascent = fm.descent - textHeight
                            }
                        }
                    }
                },
                0,
                text.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

}

