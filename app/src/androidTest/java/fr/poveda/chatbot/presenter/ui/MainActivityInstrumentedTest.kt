package fr.poveda.chatbot.presenter.ui

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import fr.poveda.chatbot.R
import fr.poveda.chatbot.data.DefaultRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class MainActivityInstrumentedTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    private val activity get() = composeTestRule.activity

    @Inject
    lateinit var defaultRepository: DefaultRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("fr.poveda.chatbot", appContext.packageName)
    }

    @Test
    fun startMainActivity() {
        // only for dev purpose
        // composeTestRule.onRoot().printToLog("fr.poveda.chatbot")
        composeTestRule.onNodeWithText(activity.getString(R.string.app_name)).assertIsDisplayed()
    }

    @Test
    fun checkBotInitialMessageAppeared() {
        composeTestRule.onNodeWithText("Hello").assertIsDisplayed()
    }

    @Test
    fun checkUserEnteredMessageAppeared() {
        composeTestRule.onNodeWithTag(activity.getString(R.string.userPromptTestTag)).performTextInput("This is a test")
        composeTestRule.onNodeWithTag(activity.getString(R.string.enterPromptButtonTestTag)).performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("This is a test").assertIsDisplayed()
        composeTestRule.onNodeWithTag(activity.getString(R.string.chatItemsList))
            .onChildren()
            .assertCountEquals(2)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun checkUserEnteredMessageAndBotResponseAppeared() {
        composeTestRule.onNodeWithTag(activity.getString(R.string.userPromptTestTag))
            .performTextInput("This is a test")
        composeTestRule.onNodeWithTag(activity.getString(R.string.enterPromptButtonTestTag)).performClick()
        composeTestRule.waitUntilExactlyOneExists(
            hasText("This is a test")
        )
        composeTestRule.waitUntil(4000L) {
            composeTestRule.onNodeWithTag(activity.getString(R.string.chatItemsList))
                .fetchSemanticsNode().children.size == 3
        }
        composeTestRule.onNodeWithTag(activity.getString(R.string.chatItemsList))
            .onChildren()
            .assertCountEquals(3)
    }
}