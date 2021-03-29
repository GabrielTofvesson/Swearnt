package dev.w1zzrd.swearnt

import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.requests.RestAction
import java.util.*


const val BOT_MESSAGE_CLEANUP_DELAY = 5000L

fun main(vararg args: String) {
    val settings = loadSettings()
    val adminIDs = Config(fileName = settings.adminConf)
    val filteredUIDS = Config(fileName = settings.filterConf)
    val parrotList = Config(fileName = settings.parrotConf)
    val swearList = SwearFilter(fileName = settings.swearsConf)


    Runtime.getRuntime().addShutdownHook(Thread {
        println("Saving configs...")
        settings.save()
        adminIDs.save()
        filteredUIDS.save()
        parrotList.save()
        swearList.save()
        println("Configs saved!")
    })

    val token: String = if (args.isEmpty()) {
        print("Enter discord bot token: ")
        Scanner(System.`in`).nextLine()
    }
    else args[0]


    JDABuilder.createDefault(token)
        .setBulkDeleteSplittingEnabled(false)
        .setActivity(Activity.playing("with ur mom"))
        .addEventListeners(
            CommandListener(settings, adminIDs, filteredUIDS, parrotList, swearList),
            ParrotListener(settings, swearList, parrotList.content, RestAction<Message>::submitAndDelete),
            FilterListener(settings, swearList, filteredUIDS.content, parrotList.content)
        )
        .build()
}