package de.blumesladen.ui.diaryentry

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import de.blumesladen.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DiaryEntryEditScreenTest {


    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setup() {
        composeTestRule.setContent {
            DiaryEntryEditDialog(fakeDiaryUiState, onValueChange = {})
        }
    }

    @Test
    fun diaryEntryEditScreen_enter_value_for_myself() {
        composeTestRule.onRoot().printToLog("TAG")
        val labelString = composeTestRule.activity.getString(R.string.diary_entry_for_myself)
        val resultText = "result"
        val element = composeTestRule.onNodeWithText(labelString) // .onNodeWithContentDescription("textFieldContentDescription")
        // composeTestRule.awaitIdle()
        element.assertIsEnabled()
        element.performClick()
        element.performTextClearance()
        element.performTextInput(resultText)
        composeTestRule.waitForIdle() // Advances the clock until Compose is idle

        element.assertTextEquals(labelString, resultText, includeEditableText = true)
    }

    @Test
    fun diaryEntryEditScreen_enter_value_for_others() {
        val labelString = composeTestRule.activity.getString(R.string.diary_entry_for_others)
        val resultText = "result"
        composeTestRule.onNodeWithText(labelString).printToLog("OutlineTextBox Test")

        composeTestRule.onNodeWithText(labelString, useUnmergedTree = true).assertIsEnabled()
        composeTestRule.onNodeWithText(labelString).performTextInput(resultText)
        composeTestRule.waitForIdle() // Advances the clock until Compose is idle
        composeTestRule.onNodeWithText(labelString).printToLog("OutlineTextBox Test")

        composeTestRule.onNodeWithText(labelString).assertTextContains(resultText) //
    }

}