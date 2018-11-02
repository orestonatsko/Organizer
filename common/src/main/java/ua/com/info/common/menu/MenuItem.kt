package ua.com.info.common.menu

import android.content.Context
import android.view.View
import ua.com.info.common.R

/**
 * Створено v.m 02.08.2017.
 */
interface IMenuItemIcon {
    val iconId: Int
}

open class MenuItem() {
    open val iconId: Int = R.drawable.document_icon
    lateinit var name: String
    var requestCode = 0
    var action: Class<*>? = null
    var onClickListener: View.OnClickListener? = null;

    constructor(action: Class<*>) : this() {
        this.action = action
    }

    constructor(onClickListener: View.OnClickListener) : this() {
        this.onClickListener = onClickListener
    }

    open fun onClick(context: Context): Boolean {
        return false
    }

//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(name)
//        parcel.writeInt(requestCode)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<MenuItem> {
//        override fun createFromParcel(parcel: Parcel): MenuItem {
//            return MenuItem(parcel)
//        }
//
//        override fun newArray(size: Int): Array<MenuItem?> {
//            return arrayOfNulls(size)
//        }
//    }
}
