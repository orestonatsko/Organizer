package ua.com.info.common.menu

import android.app.Activity
import ua.com.info.common.BaseActivity

/**
 * Створено v.m 23.03.2015.
 */

abstract class Initializer {

    private var initialized: Boolean = false

    fun initialize(activity: BaseActivity) {
        if (!initialized) init(activity)
        initialized = true
    }

    fun clear() {
        initialized = false
    }

    abstract fun init(activity: BaseActivity)
}
