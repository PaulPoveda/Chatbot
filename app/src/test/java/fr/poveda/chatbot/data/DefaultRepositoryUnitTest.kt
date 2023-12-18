package fr.poveda.chatbot.data

import fr.poveda.chatbot.data.model.Author
import fr.poveda.chatbot.data.model.Message
import fr.poveda.chatbot.data.source.network.INetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultRepositoryUnitTest {
    private lateinit var defaultRepository: DefaultRepository
    private lateinit var fakeMockedNetworkServer: FakeMockedNetworkServer
    private val userAuthor = Author(Author.USER_NAME)
    private val message1 = Message(userAuthor, "ola")
    private val message2 = Message(userAuthor, "this is a test")
    private val message3 = Message(userAuthor, "are you a real person ?")
    private val messageList = listOf(message1, message2, message3)

    @ExperimentalCoroutinesApi
    val testDispatcher = StandardTestDispatcher()

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeMockedNetworkServer = FakeMockedNetworkServer()
        defaultRepository = DefaultRepository(fakeMockedNetworkServer)
    }

    @Test
    fun defaultRepository_getBotResponse_test() = runTest {
        val responseMessage = defaultRepository.getBotResponse(message1)
        advanceUntilIdle()
        Assert.assertNotNull(responseMessage)
    }

    @Test
    fun defaultRepository_multipleGetBotResponse_test() = runTest {
        for (message in messageList) {
            launch {
                val responseMessage = defaultRepository.getBotResponse(message)
                Assert.assertNotNull(responseMessage)
                Assert.assertNotNull(responseMessage.content)
                Assert.assertEquals(Author.BOT_NAME, responseMessage.author.name)
            }
        }
        advanceUntilIdle()
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDownDispatcher() {
        Dispatchers.resetMain()
    }
}

private class FakeMockedNetworkServer : INetworkDataSource {
    private val data = "fakeData"

    override suspend fun getResponse(messageToReplyTo: Message): Message {
        return coroutineScope {
            val botMessage = async(Dispatchers.Default) { Message(Author(Author.BOT_NAME), data) }
            botMessage.await()
        }
    }
}