package de.blumesladen.ui.diaryentry

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import de.blumesladen.R
import de.blumesladen.data.di.FakeDiaryEntryRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DiaryEntryEditScreenTest {
    var uiState by mutableStateOf(DiaryEntryUiState(DiaryEntryDetails()))

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @BindValue
    val diaryEntryViewModel = DiaryEntryViewModel(diaryEntryRepository = FakeDiaryEntryRepository())

    fun updateUIState(d : DiaryEntryDetails) {
        uiState = DiaryEntryUiState(d)
    }

    @Before
    fun setup() {
        hiltRule.inject()
        composeTestRule.setContent {
            DiaryEntryEditDialog(uiState, onValueChange = this::updateUIState )
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
        element.performTextInput(resultText)
        composeTestRule.waitForIdle() // Advances the clock until Compose is idle
        element.assertTextEquals(labelString, resultText, includeEditableText = true)
    }

    @Test
    fun diaryEntryEditScreen_enter_value_for_others() {
        val labelString = composeTestRule.activity.getString(R.string.diary_entry_for_others)
        val resultText = "result"
        val element = composeTestRule.onNodeWithText(labelString) // .onNodeWithContentDescription("textFieldContentDescription")
        // composeTestRule.awaitIdle()
        element.assertIsEnabled()
        element.performClick()
        element.performTextInput(resultText)
        composeTestRule.waitForIdle() // Advances the clock until Compose is idle
        element.assertTextEquals(labelString, resultText, includeEditableText = true)
    }

}