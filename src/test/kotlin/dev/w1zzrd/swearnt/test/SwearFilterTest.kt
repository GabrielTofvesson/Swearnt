package dev.w1zzrd.swearnt.test

import dev.w1zzrd.swearnt.SwearFilter
import org.junit.jupiter.api.Test

val acceptableWords = arrayOf("frick", "heck", "darn", "hello", "cringe")
val veryBadWords = arrayOf("dang", "cr4p", "stup!d")

class SwearFilterTest {
    private val filter = SwearFilter(mutableListOf("dang", "crap", "stupid"), "filter")

    private fun testStrings(vararg words: String, shouldAllow: Boolean) =
        words.forEach { assert(filter shouldFilter it != shouldAllow) { it } }


    @Test
    fun testAcceptableStrings() = testStrings(*acceptableWords, shouldAllow = true)

    @Test
    fun testVeryBadStrings() = testStrings(*veryBadWords, shouldAllow = false)
}