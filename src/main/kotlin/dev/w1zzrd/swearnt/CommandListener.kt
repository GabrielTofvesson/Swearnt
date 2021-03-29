package dev.w1zzrd.swearnt

import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

private fun Message.getMentionedIDs() = mentionedMembers.map(net.dv8tion.jda.api.entities.ISnowflake::getId)
private fun MutableCollection<String>.addMentions(message: Message) = addAllNoDups(message.getMentionedIDs())
private fun MutableCollection<String>.removeMentions(message: Message) = removeAll(message.getMentionedIDs())

class CommandListener (
    private val settings: Settings,
    private val adminIDs: Config,
    private val filterList: Config,
    private val parrotList: Config,
    private val swearFilter: SwearFilter
): ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.author.id in adminIDs) {
            var check = event.message.contentDisplay.toLowerCase()

            if (!check.startsWith("${settings.commandTrigger} "))
                return

            check = check.substring(settings.commandTrigger.length + 1)

            when {
                check.startsWith("filter ") -> filterList.addMentions(event.message)
                check.startsWith("unfilter ") -> filterList.removeMentions(event.message)
                check.startsWith("parrot ") -> parrotList.addMentions(event.message)
                check.startsWith("unparrot ") -> parrotList.removeMentions(event.message)
                check.startsWith("disallow ") -> swearFilter += check.substring("disallow ".length)
                check.startsWith("allow ") -> swearFilter -= check.substring("allow ".length)
                else -> return
            }

            event.message.delete().submit()
            event.channel
                .sendMessage("Cool beans")
                .submitAndDelete(settings.botCleanupDelay)
        }
    }
}