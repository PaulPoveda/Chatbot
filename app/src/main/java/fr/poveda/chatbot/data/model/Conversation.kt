package fr.poveda.chatbot.data.model

data class Conversation (val messages : List<Message>) {
    operator fun plus(conversation: Conversation): Conversation {
        return Conversation(messages.plus(conversation.messages))
    }
}