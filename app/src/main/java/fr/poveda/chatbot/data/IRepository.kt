package fr.poveda.chatbot.data

import fr.poveda.chatbot.data.model.Message
import kotlinx.coroutines.flow.Flow

interface IRepository {
    suspend fun getBotResponse(message: Message) : Message
    suspend fun getBotResponses() : Flow<Message>
}