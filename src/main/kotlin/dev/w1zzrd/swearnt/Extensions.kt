package dev.w1zzrd.swearnt

import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.requests.RestAction
import java.io.File
import java.util.concurrent.TimeUnit

fun <T> MutableCollection<T>.addAllNoDups(elements: Collection<T>): Boolean =
    addAll(elements.filterNot(this::contains))

fun RestAction<Message>.submitAndDelete(
    delay: Long = BOT_MESSAGE_CLEANUP_DELAY,
    timeUnit: TimeUnit = TimeUnit.MILLISECONDS
) = submit().thenAccept { it.delete().submitAfter(delay, timeUnit) }

fun <T> T.load(fileName: String): T where T: MutableCollection<String> {
    val file = File(fileName)
    if (file.isFile) addAllNoDups(file.readLines())

    return this
}

fun Iterable<String>.save(fileName: String) {
    val file = File(fileName)
    if (file.isFile && (!file.delete() || !file.createNewFile()))
        System.err.println("Failed to save file \"$fileName\"")
    else
        file.writeText(reduce { acc, s -> "$acc\n$s" })
}