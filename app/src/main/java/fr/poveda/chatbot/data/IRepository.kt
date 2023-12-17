package fr.poveda.chatbot.data

import fr.poveda.chatbot.data.model.Message

interface IRepository {
    suspend fun getBotResponse(message: Message) : Message
}