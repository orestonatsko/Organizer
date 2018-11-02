package ua.com.info.common

import android.content.Intent
import android.support.v4.app.FragmentActivity


/**
 * Створено v.m 18.06.2018.
 */

open class BaseActivity : FragmentActivity() {

    private var dialogResult: Int = -1

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        dialogResult = resultCode
    }
}