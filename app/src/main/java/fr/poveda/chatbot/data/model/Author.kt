package fr.poveda.chatbot.data.model

data class Author(val name: String) {
    companion object {
        const val BOT_NAME = "bot"
        const val USER_NAME = "user"
    }
}