package ua.com.info.data

import java.util.ArrayList

/**
 * Створив VM 26.07.2017.
 */

class Row internal constructor(private val table: Table) : ArrayList<Any?>(table.columns.size) {

    fun getValue(columnName: String): Any? {
        val i = table.columnIndex(columnName)
        return this[i]
    }

    fun getString(columnName: String): String {
        val i = table.columnIndex(columnName)
        return this[i].toString()
    }

    fun getInt(columnName: String): Int {
        val i = table.columnIndex(columnName)
        return this[i] as Int
    }

    fun setValue(col: Int, value: Any?) {
        this[col] = value
    }

    fun setValue(columnName: String, value: Any?) {
        this[table.columnIndex(columnName)] = value
    }
}