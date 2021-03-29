package dev.w1zzrd.swearnt

open class Config(
    val content: MutableCollection<String> = ArrayList(),
    private val fileName: String,
    sort: Boolean = true
): MutableCollection<String> by content {
    init {
        load()

        if (sort) {
            val sorted = content.sorted()
            content.clear()
            content.addAll(sorted)
        }
    }

    fun load() = content.load(fileName)
    fun save() = content.save(fileName)
}
