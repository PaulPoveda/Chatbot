package fr.poveda.chatbot.data.model

data class Message(
    var author: Author,
    var content: String
) {
    fun isBotTheAuthorOfTheMessage() : Boolean {
        return author == Author.BOT
    }
}