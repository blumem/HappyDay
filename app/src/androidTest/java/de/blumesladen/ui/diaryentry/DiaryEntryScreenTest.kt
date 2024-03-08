/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.blumesladen.ui.diaryentry

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import de.blumesladen.data.local.database.DiaryEntry
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * UI tests for [DiaryEntriesCalendarScreen].
 */
@RunWith(AndroidJUnit4::class)
class DiaryEntryScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setup() {
        composeTestRule.setContent {
            DiaryEntryScreenForm(FAKE_DATA_ENTRIES,
                onSelectionChanged = {},
                onMonthChanged = {})
        }
    }

    @Test
    fun firstItem_exists() {
        composeTestRule.onNodeWithText(LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM"))).assertExists().performClick()
    }
}

private val FAKE_DATA = listOf("bike tour", "small walk", "Yoga")

@SuppressLint("NewApi")
private val FAKE_DATA_ENTRIES = listOf(
    DiaryEntry(0, LocalDate.now(),1,1, forMyself = FAKE_DATA.get(0),"made cashier smile","frustration","cookies"),
    DiaryEntry(1, LocalDate.of(2024,1,1),1,1,forMyself = FAKE_DATA.get(1),"brought cookies to ping pong","anger",""),
    DiaryEntry(2, LocalDate.of(2024,1,2),1,1,forMyself = FAKE_DATA.get(2),"cleaned the floor","honry","")
)
