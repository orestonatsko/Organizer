package ua.com.info.data

import android.app.Application

/**
 * Створено oor 15.03.2018.
 */

open class App : Application() {
    init {
        app = this
    }

    companion object {
        lateinit var app: App
    }
}
