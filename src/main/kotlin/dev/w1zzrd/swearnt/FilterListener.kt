package dev.w1zzrd.swearnt

import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class FilterListener (
    private val settings: Settings,
    private val filter: SwearFilter,
    private val filterList: Collection<String>,
    private val parrotExemptionList: Collection<String>
): ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.author.id !in parrotExemptionList &&
            filter shouldFilter event.message.contentDisplay &&
            event.author.id in filterList) {

            event.message.delete().submit()
            event.channel.sendMessage(
                MessageBuilder()
                    .append("You said a naughty word, ")
                    .append(event.author)
                    .append('!')
                    .build()
            ).submitAndDelete(settings.botCleanupDelay)
        }
    }
}