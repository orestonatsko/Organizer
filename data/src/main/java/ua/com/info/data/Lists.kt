package ua.com.info.data

/**
 * Створено v.m 02.04.2018.
 */

val Lists: HashMap<String, ArrayList<String>> by lazy { HashMap<String, ArrayList<String>>() }

fun getList(name: String): java.util.ArrayList<String>? {
    return if (Lists.containsKey(name)) Lists[name] else null
}

fun registerList(name: String, list: java.util.ArrayList<String>) {
    Lists[name] = list
}
