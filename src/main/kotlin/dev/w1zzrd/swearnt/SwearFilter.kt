package dev.w1zzrd.swearnt

private fun Char.similarChars(): String =
    arrayOf(
        "!1il",
        "o0ö",
        "aåä4",
        "b8",
        "e3",
        "t7",
        "5s"
    ).firstOrNull { it.contains(this.toLowerCase()) } ?: toString()

private fun String.makeSwearFilter(start: Boolean = false, end: Boolean = false) = Regex (
    (if (start) "^.*?" else "^") +
    map(Char::similarChars)
        .map { if (it.length==1) it else "[$it]" }
        .reduce(String::plus) +
        (if (end) ".*?$" else "$")
)

private fun String.makeStartSwearFilter() = makeSwearFilter(start = true, end = false)
private fun String.makeEndSwearFilter() = makeSwearFilter(start = false, end = true)

private fun String.filteredBy(filter: Filter) = this in filter

data class Filter(val text: String) {
    private val filterPre = text.makeStartSwearFilter()
    private val filterPost = text.makeEndSwearFilter()

    operator fun contains(text: String) = this.text == text || filterPre.matches(text) || filterPost.matches(text)
}

class SwearFilter(content: MutableCollection<String> = ArrayList(), fileName: String): Config(content, fileName) {
    private val filters = map(::Filter).toMutableList()

    infix fun shouldFilter(phrase: String) =
        phrase.toLowerCase()
            .replace(Regex("[.,?*;:<>|]"), "") // Remove some punctuation and stuff
            .split("\n", " ", "\t")
            .firstOrNull { word -> filters.firstOrNull(word::filteredBy) != null } != null

    override operator fun contains(element: String) = this shouldFilter element
    override fun add(element: String) =
        if (element !in this) {
            filters += Filter(element) // Whack
            super.add(element)
            true
        } else {
            false
        }

    override fun remove(element: String) =
        if (element in this) {
            filters.removeIf(element::filteredBy)
            super.remove(element)
            true
        } else {
            false
        }
}