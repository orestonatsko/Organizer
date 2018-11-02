package ua.com.info.common

import android.content.Context
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageButton

/**
 * Створив VM 30.03.2018.
 */

class Expander(context: Context?, attrs: AttributeSet?) : ImageButton(context, attrs) {
    private var collapsed = true
    private var saveHeight: Int = 0
    private var expandedHeight: Int = 0

    init {
        setImageResource(R.drawable.down)
        setBackgroundResource(R.drawable.round_button)
        setOnClickListener({
            val layout = parent as ViewGroup
            TransitionManager.beginDelayedTransition(layout, TransitionSet()
                    .addTransition(ChangeBounds()))
            val params = layout.layoutParams
            if (saveHeight == 0) {
                saveHeight = params.height
                //val dmetrics = DisplayMetrics()
                //val heightPixels = dmetrics.heightPixels
                layout.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                expandedHeight = layout.measuredHeight
            }
            collapsed = !collapsed
            animate().rotation(if (collapsed) 0f else 180f)
            params.height = if (collapsed) saveHeight else expandedHeight
            layout.layoutParams = params
        })
    }
}