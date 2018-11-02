package ua.com.info.data

import java.util.ArrayList

/**
 * Створено v.m 06.12.2017.
 */

 class Variables : IDataObject {
     var values = ArrayList<Variable>()

    fun getValue(name: String): Any? {
        val lowerName = name.toLowerCase().replace("@", "")
        for (v in values)
            if (v.name.toLowerCase() == lowerName) return v.value
        return null
    }

    fun getInt(name: String): Int? {
        val value = getValue(name) ?: return null
        return value as? Int ?: ((value as? Double)?.toInt() ?: Integer.parseInt(value.toString()))
    }

    fun getString(name: String): String? {
        val value = getValue(name) ?: return null
        return value as? String ?: value.toString()
    }
}