package fr.poveda.chatbot.data

import fr.poveda.chatbot.data.model.Message
import fr.poveda.chatbot.data.source.network.INetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Default implementation of [IRepository]
 * Single entry point for managing conversation data.
 */
class DefaultRepository @Inject constructor(private val networkDataSource: INetworkDataSource) : IRepository {
    override suspend fun getBotResponse(messageToReplyTo: Message): Message {
        return withContext(Dispatchers.IO) {
            networkDataSource.getResponse(messageToReplyTo)
        }
    }
}