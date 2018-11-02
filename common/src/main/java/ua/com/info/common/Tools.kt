package ua.com.info.common

import android.content.Context
import android.graphics.Point
import android.view.WindowManager

/**
 *  Створено oor 05.06.2018.
 */
fun getScreenSize(context: Context): Point {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val size = Point()
    display.getSize(size)

    return size
}