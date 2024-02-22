package de.blumesladen.ui.diaryentry

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import de.blumesladen.R
import de.blumesladen.data.di.fakeDiaryEntrys
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
            DiaryEntryEditDialog(entry= DiaryEntryUiState(fakeDiaryEntrys.get(0)), onSave = {})
        }
    }

    @Test
    fun diaryEntryEditScreen() {
        val labelString = composeTestRule.activity.getString(R.string.diary_entry_for_myself)
        composeTestRule.onNodeWithText(labelString).assertExists().performClick()
    }

}