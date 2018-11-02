package ua.com.info.data

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException

import java.lang.reflect.Type
import java.util.ArrayList

class Table(columnCount: Int) : ArrayList<Row>(), IDataObject {
    var columns: ColumnCollection = ColumnCollection(columnCount)

    class Deserializer : JsonDeserializer<Table> {
        @Throws(JsonParseException::class)
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Table {
            val j = json.asJsonObject
            val cols = j.getAsJsonArray("columns")
            val columnsCount = cols.size()
            val ret = Table(columnsCount)
            for (e in cols) {
                val o = e.asJsonObject
                val name = o.get("name").asString
                val type = o.get("type").asInt
                val column = Column(name, type)
                ret.columns.add(column)
            }
            val rows = j.getAsJsonArray("rows")
            for (e in rows) {
                val a = e.asJsonArray
                if (a.size() == columnsCount) {
                    val row = Row(ret)
                    for (i in 0 until columnsCount) {
                        val o = getValue(a.get(i), ret.columns[i].type)
                        row.add(o)
                    }
                    ret.add(row)
                }
            }
            return ret
        }
    }

    internal fun columnIndex(columnName: String): Int {
        var loverName = columnName
        loverName = loverName.toLowerCase()
        for ((i, c) in columns.withIndex()) {
            if (c.name.toLowerCase() == loverName)
                return i
        }
        return -1
    }

    fun newRow(): Row {
        val row = Row(this)
        for (i in columns.indices) {
            row.add(null)
        }
        add(row)
        return row
    }

    fun aggregate(columnIndex: Int, function: Aggregate): Double {
        val count = size
        var res: Double
        res = when (function) {
            Aggregate.avg, Aggregate.sum -> 0.0
            Aggregate.count -> return count.toDouble()
            else -> java.lang.Double.NaN
        }

        for (i in 0 until count) {
            val row = get(i)
            val d = Convert.toDouble(row[columnIndex])
            when (function) {
                Aggregate.max -> if (res < d || java.lang.Double.isNaN(res)) res = d
                Aggregate.min -> if (res > d || java.lang.Double.isNaN(res)) res = d
                Aggregate.avg, Aggregate.sum -> res += d
                else -> {
                }
            }
        }
        return if (function == Aggregate.avg)
            res / count
        else
            res
    }

    companion object {

        private fun getValue(p: JsonElement, type: Int): Any {
            when (type) {
                Types.INT -> return p.asLong
                Types.SMALLINT -> return p.asInt
                Types.MONEY, Types.DECIMAL, Types.FLOAT -> return p.asDouble
                Types.BIT -> return p.asBoolean
            }
            return p.asString
        }
    }
}