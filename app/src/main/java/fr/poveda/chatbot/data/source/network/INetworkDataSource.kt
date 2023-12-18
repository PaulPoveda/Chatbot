package fr.poveda.chatbot.data.source.network

import fr.poveda.chatbot.data.model.Message

/**
 * Main interface to get answers coming from a network source
 */
interface INetworkDataSource {
    /**
     * Get a response from a distant network source
     * @param messageToReplyTo: the message to respond to
     * @return a response [Message]
     */
    suspend fun getResponse(messageToReplyTo: Message): Message
}
