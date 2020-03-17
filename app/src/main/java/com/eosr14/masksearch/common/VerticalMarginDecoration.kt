package com.eosr14.masksearch.common

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalMarginDecoration(private val context: Context) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            3.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}