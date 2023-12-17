package fr.poveda.chatbot.data.source.network

import fr.poveda.chatbot.data.model.Author
import fr.poveda.chatbot.data.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.random.Random

class MockedNetworkServer @Inject constructor(): INetworkDataSource {
    private var serviceLatencyInMillis = Random.nextLong(1000L, 2500L)
    private val bot = Author(Author.BOT_NAME)
    private val botOpeningReponses = mutableListOf(
        "Hello, how are you ?",
        "Why are you coming to see me?",
        "Please tell me what's been bothering you.",
        "Is something troubling you ?",
        "Why are you asking that ?",
        )
    private val botComputerAnswerReponses = mutableListOf(
        "Are you telling that because i'm a computer ?",
        "What do you think about machines ?",
        "You know, computer have feelings too",
        )
    private val botOtherResponses = mutableListOf(
        "That is interesting. Please continue.",
        "Tell me, how are you today ?",
        "If aliens exists, why they did not already contacted us ?"
    )

    private suspend fun getBotMessage(message: Message) : Message {
        delay(serviceLatencyInMillis)
        return when {
            message.content.contains("computer", ignoreCase = true) -> {
                Message(bot, botComputerAnswerReponses.random())
            }
            message.content.contains("hello", ignoreCase = true) -> {
                Message(bot, botOpeningReponses.random())
            }
            else -> {
                Message(bot, botOtherResponses.random())
            }
        }
    }

    override suspend fun getResponse(message: Message): Message {
        return coroutineScope {
            val botMessage = async(Dispatchers.Default) { getBotMessage(message) }
            botMessage.await()
        }
    }
}