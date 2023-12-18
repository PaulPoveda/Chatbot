package fr.poveda.chatbot.data.source.network

import fr.poveda.chatbot.data.model.Message
import kotlinx.coroutines.flow.Flow

/**
 * Main entry point for accessing bot message responses from the network.
 */
interface INetworkDataSource {
    suspend fun getResponse(message: Message): Message
    suspend fun getResponses(): Flow<Message>
}
