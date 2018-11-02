package ua.com.info.data

import com.google.gson.*
import java.lang.reflect.Type

/**
 * Створено v.m 27.07.2017.
 */
fun error(error : String) : Result {
    return Result(Status.Error, error)
}
class Result(var status: Status, private var obj: Any?) {

    val variables: Variables?
        get() {
            if (obj == null) return null
            if (obj is Variables) return obj as Variables?
            return if (obj is Data) (obj as Data).variables else null
        }

    val table: Table?
        get() {
            if (obj is Table)
                return obj as Table?
            if (obj is Data) {
                val data = obj as Data?
                return data!!.table
            }
            return null
        }

    val data: Data?
    get() {
        return obj as? Data
    }

    val isOk: Boolean
        get() = status == Status.Ok

    class Deserializer : JsonDeserializer<Result> {

        private fun getObject(jsonObject: JsonObject): IDataObject? {
            var e: JsonElement? = jsonObject.get("text")
            if (e != null) return Message(e.asString)
            e = jsonObject.get("variables")
            if (e != null)
                return JsonHelper.DeserializeObject<Variables>(e, Variables::class.java)
            e = jsonObject.get("table")
            return if (e != null) JsonHelper.DeserializeObject<Variables>(e, Table::class.java) else null
        }

        private fun getObject(jsonElement: JsonElement): IDataObject? {
            val o = jsonElement as JsonObject
            return getObject(o)
        }

        @Throws(JsonParseException::class)
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Result {
            val j = json.asJsonObject
            var e: JsonElement? = j.get("status")
            var i = 0
            if (e != null) i = e.asInt
            var o: Any? = null
            val a = j.getAsJsonArray("data")
            if (a != null) {
                val d = Data(a.size())
                for (element in a) {
                    val dataObject = getObject(element)
                    d.add(dataObject)
                }
                o = d
            } else {
                e = j.get("text")
                if (e != null)
                    o = e.asString
                else {
                    e = j.get("variables")
                    if (e != null)
                        o = JsonHelper.DeserializeObject<Variables>(e, Variables::class.java)
                }
            }
            return Result(Status.fromInt(i), o)
        }
    }

    fun setText(txt: String) {
        obj = txt
    }

    fun text(): String? {
        return if (obj is String) obj as String? else null
    }

    fun error(): String? {
         when (status) {
            Status.Ok -> { }
            Status.Error -> return text()
            Status.Timeout -> return "Перевищено час очікування."
        }
        return ""
    }

    companion object {
        val Ok by lazy { Result(Status.Ok, null) }
    }
}



