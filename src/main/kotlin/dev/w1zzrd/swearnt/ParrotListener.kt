package dev.w1zzrd.swearnt

import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.RestAction
import java.util.concurrent.CompletableFuture

class ParrotListener(
    private val settings: Settings,
    private val filter: SwearFilter,
    private val parrotList: Collection<String>,
    private val submissionPolicy: RestAction<Message>.() -> CompletableFuture<*> = { submitAndDelete(settings.botCleanupDelay) }
): ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.author.id in parrotList && filter shouldFilter event.message.contentDisplay)
            event.channel.sendMessage(MessageBuilder(event.message.contentRaw.toUpperCase()).build())
                .submissionPolicy()
    }
}