package ua.com.info.data

import android.util.Log

import java.util.Calendar
import java.util.Date

/**
 * Створено oor 15.03.2018.
 */

class Catalogue private constructor(private val procedure: String, private var parameters: Parameters?) {
    private var readData: Date? = null
    private var table: Table? = null

    val values: Table?
        get() {

            if (readData == null)
                read()
            return table
        }

    private fun read() {
        if (commonParameter != null)
            if (parameters == null) {
                parameters = Parameters()
                parameters!!.add(commonParameter)
            } else if (!parameters!!.contains(commonParameter))
                parameters!!.add(commonParameter)

        val result = DataBase.db.getTable(procedure, parameters)
        if (result.isOk) {
            readData = Calendar.getInstance().time
            table = result.table
        }

    }

    fun getName(code: Int): String {
        val tbl = values
        if (tbl != null) {
            try {
                for (row in tbl) {
                    if (row.getValue(App.app.getString(R.string.code)) as Int == code) {
                        return row.getValue(App.app.getString(R.string.name)) as String
                    }
                }
                return "?"
            } catch (e: Exception) {
                Log.e("Catalogue", e.message)
            }

        }
        return "???"
    }

    companion object {
        var catalogues: Catalogues? = null
        val commonParameter: Parameter? = null

        fun containsCatalogue(name: String): Boolean {
            return catalogues != null && catalogues!!.containsKey(name)
        }

        fun getCatalogue(name: String): Catalogue? {
            return if (containsCatalogue(name)) catalogues!![name] else null
        }

        fun register(name: String, procedure: String) {
            register(name, procedure, null)
        }

        private fun register(name: String, procedure: String, parameters: Parameters?) {
            if (catalogues == null)
                catalogues = Catalogues()

                if (catalogues!!.containsKey(name))
                    return
                val catalogue = Catalogue(procedure, parameters)
                catalogues!![name] = catalogue
        }
    }

}



