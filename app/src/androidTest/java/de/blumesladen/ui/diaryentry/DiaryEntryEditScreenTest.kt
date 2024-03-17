package de.blumesladen.ui.diaryentry

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import de.blumesladen.R
import de.blumesladen.data.local.database.DiaryEntry
import de.blumesladen.data.local.di.DiaryEntryViewModel
import de.blumesladen.data.local.di.FakeDiaryEntryRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.Calendar

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@MediumTest
class DiaryEntryEditScreenTest {
    // var uiState by mutableStateOf(DiaryEntryUiState(diaryEntryDetails =  DiaryEntryDetails()))

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @BindValue
    val diaryEntryViewModel = DiaryEntryViewModel(
        diaryEntryRepository = FakeDiaryEntryRepository(),
        savedStateHandle = SavedStateHandle()
    )

    fun updateUIState(diaryEntry: DiaryEntry) {
        diaryEntryViewModel.diaryEntry.value = diaryEntry
    }

    @Before
    fun setup() {
        hiltRule.inject()
        composeTestRule.setContent {
            DiaryEntryEditor(diaryEntryViewModel.diaryEntry.value, onValueChange = this::updateUIState )
        }
    }

    /**
     * Helper function to automate the boilerplate
     */
    fun helper_test_input_into_text_input_field_with_label(label : String, testInputText : String) {
        val element = composeTestRule.onNodeWithText(label) // .onNodeWithContentDescription("textFieldContentDescription")
        element.assertIsEnabled()
        element.performClick()
        element.performTextClearance()
        element.performTextInput(testInputText)
        composeTestRule.waitForIdle() // Advances the clock until Compose is idle
        element.assertTextEquals(label, testInputText, includeEditableText = true)
    }

    @Test
    fun diaryEntryEditScreen_enter_value_entry_date() {
        val labelString = composeTestRule.activity.getString(R.string.diary_entry_for_myself)
        val resultText = "2024-02-26"
        helper_test_input_into_text_input_field_with_label(labelString, resultText)
    }

    @Test
    fun diaryEntryEditScreen_clicking_calendar_month_button_opens_dialog() {
        val labelString = composeTestRule.activity.getString(R.string.calendar_button_content_description)
        val cancelString = composeTestRule.activity.getString(R.string.cancel_action)
        val element = composeTestRule.onNodeWithContentDescription(labelString)

        element.assertIsEnabled()
        element.performClick()
        composeTestRule.waitForIdle() // the dialog to open.
        // TODO: translate "Selected date" to the current language
        composeTestRule.onNodeWithText("Selected date").assertExists("Date Picker Dialog didn't open")
        composeTestRule.onNodeWithText(cancelString).performClick()
        composeTestRule.waitForIdle() // the dialog to close
        composeTestRule.onNodeWithText("Selected date").assertDoesNotExist()
    }

    @Test
    fun diaryEntryEditScreen_clicking_calendar_month_in_dialog() {
        val labelString = composeTestRule.activity.getString(R.string.calendar_button_content_description)
        val okString = composeTestRule.activity.getString(R.string.ok_action)
        val cal: Calendar = Calendar.getInstance()
        val today = SimpleDateFormat("yyyy-MM-dd").format(cal.getTime())
        cal.add(Calendar.DATE, -1)
        // format like DatePickerDialog uses internally
        val yesterdayDatePicker = SimpleDateFormat("EEEE, MMMM dd, yyyy").format(cal.getTime())
        // format how the input field is using it
        val yesterdayInput = SimpleDateFormat("yyyy-MM-dd").format(cal.getTime())
        // open the dialog
        composeTestRule.onNodeWithContentDescription(labelString).performClick()
        // wait for it to be drawn
        composeTestRule.waitForIdle()
        // click on another date:
        if (today.substring(0,6).equals(yesterdayDatePicker.substring(0,6))) {
            composeTestRule.onNodeWithText(yesterdayDatePicker, useUnmergedTree = true).performClick()
            composeTestRule.onNodeWithText(okString).performClick()
            composeTestRule.waitForIdle()
            composeTestRule.onNodeWithText("Date:")
                .assertTextEquals("Date:", yesterdayInput, includeEditableText = true)
        } else {
            // TODO test if yday is the first of the month (which means we have to click once "<" at the top)
            // https://stackoverflow.com/questions/77111152/jetpack-compose-ui-test-with-material3-datepickerdialog
            // we have to use ContentDescription for that.
            // DatePickerSwitchToPreviousMonth
        }
    }

    @Test
    fun diaryEntryEditScreen_enter_value_for_myself() {
        val labelString = composeTestRule.activity.getString(R.string.diary_entry_for_myself)
        val resultText = "result"
        helper_test_input_into_text_input_field_with_label(labelString, resultText)
    }

    @Test
    fun diaryEntryEditScreen_enter_value_for_others() {
        val labelString = composeTestRule.activity.getString(R.string.diary_entry_for_others)
        val resultText = "result"
        helper_test_input_into_text_input_field_with_label(labelString, resultText)
    }

    @Test
    fun diaryEntryEditScreen_enter_value_unexpressed_emotion() {
        val labelString = composeTestRule.activity.getString(R.string.diary_entry_unexpressed_emotions)
        val resultText = "result"
        helper_test_input_into_text_input_field_with_label(labelString, resultText)
    }

    @Test
    fun diaryEntryEditScreen_enter_value_something_good() {
        val labelString = composeTestRule.activity.getString(R.string.diary_entry_something_good)
        val resultText = "result"
        helper_test_input_into_text_input_field_with_label(labelString, resultText)
    }

    @Test
    fun diaryEntryEditScreen_enter_value_anticipation() {
        val labelString = composeTestRule.activity.getString(R.string.diary_entry_anticipation)
        val resultText = "result"
        helper_test_input_into_text_input_field_with_label(labelString, resultText)
    }

}