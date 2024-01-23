package fr.poveda.chatbot.presenter.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBarDefaults.containerColor
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import fr.poveda.chatbot.R
import fr.poveda.chatbot.data.model.Author
import fr.poveda.chatbot.data.model.Conversation
import fr.poveda.chatbot.data.model.Message
import fr.poveda.chatbot.presenter.ui.MainViewModel
import fr.poveda.chatbot.presenter.ui.theme.ChatBotTheme
import fr.poveda.chatbot.presenter.ui.theme.md_theme_light_inversePrimary
import fr.poveda.chatbot.presenter.ui.theme.md_theme_light_primaryContainer
import fr.poveda.chatbot.presenter.ui.theme.md_theme_light_secondaryContainer


@Composable
fun ChatBotScreen(viewModel: MainViewModel = hiltViewModel()) {
    val conversation by viewModel.conversation.collectAsState()
    val chatBoxValue by viewModel.chatBoxValue

    ChatBotScreen(
        conversation = conversation,
        chatBoxValue = chatBoxValue,
        onValueChange = viewModel::onChatBoxValueChanged,
        onSendChatClickListener = viewModel::onSendMessage,
        modifier = Modifier
    )
}

@Composable
internal fun ChatBotScreen(
    conversation: Conversation,
    chatBoxValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onSendChatClickListener: () -> Unit,
    modifier: Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        val listState = rememberLazyListState()

        LaunchedEffect(conversation.messages.size) {
            listState.animateScrollToItem(conversation.messages.size)
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .testTag(stringResource(id = R.string.chatItemsList)),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(conversation.messages) { item ->
                ChatItem(item)
            }
        }

        ChatBox(
            chatBoxValue,
            onValueChange = onValueChange,
            onSendChatClickListener = onSendChatClickListener,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ChatItem(message: Message) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .testTag(stringResource(id = R.string.chatItemTestTag))
                .align(if (message.isBotTheAuthorOfTheMessage()) Alignment.End else Alignment.Start)
                .clip(
                    RoundedCornerShape(
                        topStart = 48f,
                        topEnd = 48f,
                        bottomStart = if (message.isBotTheAuthorOfTheMessage()) 48f else 0f,
                        bottomEnd = if (message.isBotTheAuthorOfTheMessage()) 0f else 48f
                    )
                )
                .background(if (message.isBotTheAuthorOfTheMessage()) md_theme_light_secondaryContainer else md_theme_light_primaryContainer)
                .padding(16.dp)
        ) {
            Text(text = message.content)
        }
    }
}

@Composable
fun ChatBox(
    chatBoxValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onSendChatClickListener: () -> Unit,
    modifier: Modifier
) {
    Row(modifier = modifier.padding(16.dp)) {
        TextField(
            value = chatBoxValue,
            onValueChange = onValueChange,
            modifier = Modifier
                .weight(1f)
                .padding(4.dp)
                .testTag(stringResource(id = R.string.userPromptTestTag)),
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                disabledContainerColor = containerColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            placeholder = {
                Text(text = "Message Bot")
            }
        )
        IconButton(
            onClick = {
                onSendChatClickListener()
            },
            modifier = Modifier
                .clip(CircleShape)
                .background(color = md_theme_light_inversePrimary)
                .align(Alignment.CenterVertically)
                .testTag(stringResource(id = R.string.enterPromptButtonTestTag))
        ) {
            Icon(
                imageVector = Icons.Filled.Send,
                contentDescription = "Send",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            )
        }
    }
}

/**
 * Screen preview only for dev purpose
 */
@Preview(showSystemUi = true)
@Composable
fun ChatBotScreenPreview() {
    ChatBotTheme {
        val mockConversation = Conversation(
            messages = mutableListOf(
                Message(
                    author = Author.USER,
                    content = "Hello bot"
                ),
                Message(
                    author = Author.BOT,
                    content = "Hello fellow user"
                )
            )
        )

        val mockChatBoxValue = TextFieldValue("")

        ChatBotScreen(
            conversation = mockConversation,
            chatBoxValue = mockChatBoxValue,
            onValueChange = {},
            onSendChatClickListener = {},
            modifier = Modifier
        )
    }
}