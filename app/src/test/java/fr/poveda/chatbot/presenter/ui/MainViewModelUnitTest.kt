package fr.poveda.chatbot.presenter.ui

import fr.poveda.chatbot.data.IRepository
import fr.poveda.chatbot.data.model.Author
import fr.poveda.chatbot.data.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelUnitTest {

    private lateinit var mockedRepository: MockedRepository
    private lateinit var viewModel: MainViewModel
    private lateinit var userTestMessage: String

    @ExperimentalCoroutinesApi
    val testDispatcher = StandardTestDispatcher()

    @ExperimentalCoroutinesApi
    @Before
    fun setupDispatcherAndViewModel() {
        Dispatchers.setMain(testDispatcher)
        mockedRepository = MockedRepository()
        viewModel = MainViewModel(mockedRepository)
        userTestMessage = "hello bot !"
    }

    @Test
    fun viewModel_init_test() = runTest {
        Assert.assertNotNull(viewModel.conversation.value)
        Assert.assertEquals(1, viewModel.conversation.value.messages.size)
        Assert.assertEquals(Author.BOT_NAME, viewModel.conversation.value.messages[0].author.name)
        Assert.assertEquals("Hello", viewModel.conversation.value.messages[0].content)
    }

    @Test
    fun viewModel_userSendOneMessage_test() = runTest {
        val viewModel = MainViewModel(MockedRepository())
        viewModel.updateConversation(userTestMessage)
        advanceUntilIdle()
        Assert.assertEquals(3, viewModel.conversation.value.messages.size)

        Assert.assertEquals(Author.USER_NAME, viewModel.conversation.value.messages[1].author.name)
        Assert.assertEquals(userTestMessage, viewModel.conversation.value.messages[1].content)
    }

    @Test
    fun viewModel_userSendOneMessageBotRespond_test() = runTest {
        val viewModel = MainViewModel(MockedRepository())
        viewModel.updateConversation(userTestMessage)
        advanceUntilIdle()
        Assert.assertEquals(3, viewModel.conversation.value.messages.size)

        Assert.assertEquals(Author.USER_NAME, viewModel.conversation.value.messages[1].author.name)
        Assert.assertEquals(userTestMessage, viewModel.conversation.value.messages[1].content)

        Assert.assertEquals(Author.BOT_NAME, viewModel.conversation.value.messages[2].author.name)
        Assert.assertEquals("fakeData", viewModel.conversation.value.messages[2].content)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDownDispatcher() {
        Dispatchers.resetMain()
    }
}

private class MockedRepository : IRepository {
    private val data = "fakeData"

    override suspend fun getBotResponse(messageToReplyTo: Message): Message {
        return Message(Author(Author.BOT_NAME), data)
    }
}