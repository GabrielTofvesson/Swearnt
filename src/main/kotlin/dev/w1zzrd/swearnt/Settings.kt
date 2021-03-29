package dev.w1zzrd.swearnt

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import java.io.FileOutputStream

fun loadSettings(settingsFile: String = "settings.json"): Settings {
    val file = File(settingsFile)
    if (file.isFile) return ObjectMapper().readValue(file, Settings::class.java)

    return Settings()
}

class Settings {
    @JsonProperty
    var commandTrigger = "bruh"

    @JsonProperty
    var botCleanupDelay = 5000L

    @JsonProperty
    var adminConf = "admin.conf"

    @JsonProperty
    var filterConf = "filter.conf"

    @JsonProperty
    var parrotConf = "parrot.conf"

    @JsonProperty
    var swearsConf = "swears.conf"

    fun save(fileName: String = "settings.json") {
        val file = File(fileName)
        if (file.isFile && (!file.delete() || !file.createNewFile()))
            System.err.println("Failed to save settings")
        else FileOutputStream(file).use {
            ObjectMapper().writeValue(it, this)
        }
    }
}