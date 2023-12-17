package fr.poveda.chatbot.data

import fr.poveda.chatbot.data.model.Author
import fr.poveda.chatbot.data.model.Message
import fr.poveda.chatbot.data.source.network.MockedNetworkServer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MockedNetworkServerUnitTest {
    private lateinit var mockedNetworkServer: MockedNetworkServer
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
        mockedNetworkServer = MockedNetworkServer()
    }

    @Test
    fun mockedNetworkServer_getResponse_test() = runTest {
        val responseMessage = mockedNetworkServer.getResponse(message1)
        advanceUntilIdle()
        assertNotNull(responseMessage)
    }

    @Test
    fun mockedNetworkServer_multipleGetResponse_test() = runTest {
        for (message in messageList) {
            launch {
                val responseMessage = mockedNetworkServer.getResponse(message)
                assertNotNull(responseMessage)
                assertNotNull(responseMessage.content)
                assertEquals(Author.BOT_NAME, responseMessage.author.name)
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