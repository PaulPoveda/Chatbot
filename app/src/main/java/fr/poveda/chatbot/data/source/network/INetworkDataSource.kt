package fr.poveda.chatbot.data.source.network

import fr.poveda.chatbot.data.model.Message

/**
 * Main entry point for accessing bot message responses from the network.
 */
interface INetworkDataSource {
    suspend fun getResponse(message: Message): Message
}
