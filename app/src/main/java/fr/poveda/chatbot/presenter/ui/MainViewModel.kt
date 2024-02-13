package fr.poveda.chatbot.presenter.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.poveda.chatbot.data.IRepository
import fr.poveda.chatbot.data.model.Author
import fr.poveda.chatbot.data.model.Conversation
import fr.poveda.chatbot.data.model.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val botInitialMessage = Message(Author.BOT, "Hello")
    // Private variable with mutableState
    private val _conversation = MutableStateFlow(
        Conversation(listOf(botInitialMessage))
    )
    // Exposed variable with read only state
    val conversation = _conversation.asStateFlow()

    private val _chatBoxValue = mutableStateOf(TextFieldValue(""))
    val chatBoxValue: MutableState<TextFieldValue> = _chatBoxValue

    fun onChatBoxValueChanged(newValue: TextFieldValue) {
        _chatBoxValue.value = newValue
    }

    fun onSendMessage() {
        val msg = chatBoxValue.value.text
        if (msg.isNotBlank()) {
            updateConversation(msg)
            onChatBoxValueChanged(TextFieldValue(""))
        }
    }

    fun updateConversation(message: String) {
        val userMessage = Message(Author.USER, message)
        _conversation.value = Conversation(_conversation.value.messages + userMessage)
        viewModelScope.launch {
            // StateFlow emits only when the whole object (here a Conversation) is modified
            // and not if it is just a reference inside
            // cf. https://stackoverflow.com/questions/72760708/kotlin-stateflow-not-emitting-updates-to-its-collectors
            // emit() is thread safe
            // _conversation.emit(Conversation(_conversation.value.messages + userMessage))
            try {
                val botResponse = repository.getBotResponse(userMessage) // in Dispatcher.IO
                _conversation.emit(Conversation(_conversation.value.messages + botResponse))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}