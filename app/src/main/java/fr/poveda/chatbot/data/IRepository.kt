package fr.poveda.chatbot.data

import fr.poveda.chatbot.data.model.Message

/**
 * Main interface to get answers coming from repositories
 */
interface IRepository {
    /**
     * Get a response from a bot to a given message
     * @param messageToReplyTo: the message to respond to
     * @return a response [Message]
     */
    suspend fun getBotResponse(messageToReplyTo: Message) : Message
}