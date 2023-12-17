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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import fr.poveda.chatbot.R
import fr.poveda.chatbot.data.model.Author
import fr.poveda.chatbot.data.model.Conversation
import fr.poveda.chatbot.data.model.Message
import fr.poveda.chatbot.presenter.ui.MainViewModel
import fr.poveda.chatbot.presenter.ui.theme.ChatBotTheme
import fr.poveda.chatbot.presenter.ui.theme.md_theme_light_inversePrimary
import fr.poveda.chatbot.presenter.ui.theme.md_theme_light_secondaryContainer


@Composable
fun ChatBotScreen(modifier: Modifier = Modifier, viewModel: MainViewModel = hiltViewModel()) {
    val conversation by viewModel.conversation.collectAsState()
    ChatBotScreen(
        conversation = conversation,
        userMessageEnteredListener = { msg -> viewModel.updateConversation(msg) },
        modifier = Modifier)
}

@Composable
internal fun ChatBotScreen(
    conversation: Conversation,
    userMessageEnteredListener: (String) -> Unit,
    modifier: Modifier
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (messages, chatBox) = createRefs()

        val listState = rememberLazyListState()
        LaunchedEffect(conversation.messages.size) {
            listState.animateScrollToItem(conversation.messages.size)
        }
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(messages) {
                    top.linkTo(parent.top)
                    bottom.linkTo(chatBox.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                }
                .testTag(stringResource(id = R.string.chatItemsList)),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(conversation.messages) { item ->
                ChatItem(item)
            }
        }
        ChatBox(
            userMessageEnteredListener,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(chatBox) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
    }
}

@Composable
fun ChatItem(message: Message) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp)) {
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
                .background(md_theme_light_secondaryContainer)
                .padding(16.dp)
        ) {
            Text(text = message.content)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBox(
    onSendChatClickListener: (String) -> Unit,
    modifier: Modifier
) {
    var chatBoxValue by remember { mutableStateOf(TextFieldValue("")) }
    Row(modifier = modifier.padding(16.dp)) {
        TextField(
            value = chatBoxValue,
            onValueChange = { newText ->
                chatBoxValue = newText
            },
            modifier = Modifier
                .testTag(stringResource(id = R.string.userPromptTestTag))
                .weight(1f)
                .padding(4.dp),
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
                val msg = chatBoxValue.text
                if (msg.isBlank()) return@IconButton
                onSendChatClickListener(chatBoxValue.text)
                chatBoxValue = TextFieldValue("")
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
        ChatBotScreen(
            conversation = Conversation(listOf(
                Message(
                    Author(Author.USER_NAME),
                    "Hello bot"
                ),
                Message(
                    Author(Author.BOT_NAME),
                    "Hello fellow user",
                )),
            ),
            userMessageEnteredListener = {},
            modifier = Modifier
        )
    }
}