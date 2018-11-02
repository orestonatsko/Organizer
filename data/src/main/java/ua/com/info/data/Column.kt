package ua.com.info.data

/**
 * Створив VM 26.07.2017.
 */

class Column @JvmOverloads constructor(val name: String, val type: Int, private val _title: String? = null) {
    var tag: Any? = null

    val title: String
        get() = _title ?: name
}
