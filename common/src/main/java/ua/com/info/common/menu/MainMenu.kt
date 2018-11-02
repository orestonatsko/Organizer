package ua.com.info.common.menu

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ua.com.info.common.BaseActivity


import ua.com.info.common.R
import ua.com.info.common.Utils
import ua.com.info.data.Login


class MainMenu : Fragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.activity_main_menu, container, false)
        //        ButtonMenu menu = (ButtonMenu) v.findViewById(R.id.menu);
        val menu = v.findViewById<ButtonMenu>(R.id.menu)
        if (initializer != null) initializer!!.initialize((activity as BaseActivity?)!!)
        menu.setMenuDescription(menuDescription!!)
        menu.setItems()

        val info = v.findViewById<View>(R.id.info) as TextView
        info.text = getString(R.string.user) + Login.getUser().Name

        val versionText = v.findViewById<View>(R.id.versionText) as TextView
        versionText.text = "V" + Utils.VersionCode
        return v
    }

    companion object {

        var initializer: Initializer? = null

        var menuDescription: MenuDescription? = null
    }
}