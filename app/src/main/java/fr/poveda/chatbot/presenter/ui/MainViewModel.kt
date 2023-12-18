package fr.poveda.chatbot.presenter.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.poveda.chatbot.data.IRepository
import fr.poveda.chatbot.data.model.Author
import fr.poveda.chatbot.data.model.Conversation
import fr.poveda.chatbot.data.model.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * MainViewModel descend from ViewModel.
 * Link between the [MainActivity] and the [IRepository]
 * Catch data from the [IRepository] and update UI accordingly
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: IRepository) : ViewModel() {
    private val botInitialMessage = Message(Author(Author.BOT_NAME), "Hello")
    // Private variable with mutableState
    private val _conversation = MutableStateFlow(
        Conversation(listOf(botInitialMessage))
    )
    // Exposed variable with read only state
    val conversation: StateFlow<Conversation> = _conversation

    fun updateConversation(message: String) {
        val userMessage = Message(Author(Author.USER_NAME), message)
        viewModelScope.launch {
            // Update UI with user message
            _conversation.emit(_conversation.value + Conversation(listOf(userMessage)))
            // Fetch bot response and update UI
            _conversation.emit(_conversation.value + Conversation(listOf(repository.getBotResponse(userMessage))))

            // Fetch a fixed number of response from a flow and display it
            /*repository.getBotResponses().take(3).collect {
                _conversation.emit(_conversation.value + Conversation(listOf(it)))
            }*/
        }
    }
}