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

package de.blumesladen.data.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(indices = [Index(value=["entry_date"], unique = true)])
data class DiaryEntry (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    val uid: Int = 0,

    @ColumnInfo(name = "entry_date")
    val entryDate: LocalDate = LocalDate.now(),
    @ColumnInfo(name = "abstinent", defaultValue = "0")
    val abstinent: Int = 0,
    @ColumnInfo(name = "exercised", defaultValue = "0")
    val exercised: Int = 0,
    @ColumnInfo(name = "for_myself")
    val forMyself: String = "",
    @ColumnInfo(name = "for_others")
    val forOthers: String = "",
    @ColumnInfo(name = "unexpressed_emotions", defaultValue = "NULL")
    val unexpressedEmotions: String? = null,
    @ColumnInfo(name = "something_good", defaultValue = "NULL")
    val somethingGood: String? = null,
    @ColumnInfo(name = "anticipation", defaultValue = "NULL")
    val anticipation: String? = null
) {
    override fun toString() : String {
        return "$entryDate:\nFor myself: $forMyself\nFor others: $forOthers\n"
    }
}

/**
 * *****************************************************************************************
 * Github co-pilot prompt for Data Layer:
 * *****************************************************************************************
  Generate a DAO and DiaryEntryRepository to load, insert and delete single DiaryEntries.
  Loading single records should be possible by uid and by entryDate. Loading multiple entries should
  be possible by loading the most recent 30 days, by loading all entries where entryDate is from a
  given month and one function to load all entries. Use Flow where necessary.

  Generate a DateEntryRepository interface and one implementing class using the DiaryEntryDao.
    It should implement the following functions to load, insert
    and delete single record as well as to load a given month and the last 30 days. The DiaryEntryDao
    should get injected by Hilt.
  Generate a fake implementation of the DateEntryRepository interface for testing purposes
    that is taking a injected hilt constructor and
    gets via init populated with 10 fake DiaryEntry objects.
 * *****************************************************************************************
 * Manual steps:
 *   in the loop to populate fake entries, you need to change
 *      entryDate = LocalDate.now().minusDays(i.toLong()),
 *   to
 *       entryDate = LocalDate.now().minusDays(i.toLong() - 1),
 *   to avoid that we don't have today inside the test set.
 * *****************************************************************************************

Create a HiltViewModel with a SavedStateHandle for the string parameter "date" of type LocalDate
  that uses the DiaryEntryRepository to load and insert DiaryEntries. The date parameter shall be
  stored in a local private variable and used to initialize the diaryEntry.
  If an object is loaded by entryDate it should return the corresponding object from the database
  or a new object with the given entryDate parameter.
  Moreover, the ViewModel should provide a function to update the current
  diaryEntry value of the ViewModel, that can be called from the Compose UI without storing it back into the repository.
  Finally, the ViewModel should have a function to insert
  an DiaryEntry into the DiaryEntryRepository and a function to delete the Entry.
  Each DiaryEntryViewModel function should update the private date variable if necessary.
 *
 */

/**
 * *****************************************************************************************
 * Github co-pilot prompt for Compose UI:
 * *****************************************************************************************
 Create a jetpack Compose function called DiaryEntryEditor that takes a DiaryEntry and a
 function onValueChange calling viewModel::updateDiaryEntry as parameters.
The function does not create it's own mutable state of the DiaryEntry but uses the one passed as parameter.
Therefore every onValueChange inside the function for each component should call the onValueChange-parameter function
just passing the diaryEntry.copy() with the new value.
For all labels, use the XML string resources.
 The function should use a column layout.
 At the top, there is a Row Layout with verticalAlignment = Alignment.CenterVertically holding two
 components:
 1) For the entryDate use a disabled TextField and a modifier set weight to 0.75f (75% of the available space).
 2) and a IconButton showing a calendar icon using the function calendarTodayIcon() and
    the contentDescription set to  stringResource(R.string.calendar_button_content_description),
    that toggles a boolean variable showDatePickerDialog.

 This variable controls whether the MyDatePickerDialog is displayed to change the date of the
 DiaryEntry below the entryDate field.
 Using Material3 OutlinedTextField to edit the forMyself, forOthers, unexpressedEmotions,
 somethingGood and anticipation fields of the DiaryEntry with the modifier set to Modifier.fillMaxWidth()
 and two Row Layouts using verticalAlignment = Alignment.CenterVertically to display checkboxes
 with text labels to edit the abstinent and exercised fields of the DiaryEntry.
 The function DiaryEntryEditor should not contain any buttons.

 Create another jetpack compose function called DiaryEntryEditScreen that takes 3 parameter:
 a DiaryEntryViewModel using hiltViewModel(), a String called "date", and a navController.
 The first line is val diaryEntry by remember { viewModel.diaryEntry }. diaryEntry will be passed
 to the DiaryEntryEditor later as parameter.
 The function uses a Column Layout with vertical scroll enabled to display the
 DiaryEntryEditor and two buttons.
 The Buttons are in a Row Layout with horizontalArrangement = Arrangement.spacedBy(16.dp).
 The first button should call "save" and onClick call
     first viewModel::insertDiaryEntry with the current DiaryEntry followed by navController.popBackStack().
 The second button should be called "cancel" and call onClick the navController.popBackStack().

 create for both functions preview functions that show the DiaryEntryEditor and DiaryEntryEditScreen.
 */

/**
 * *****************************************************************************************
 * Github co-pilot prompt for tests:
 * *****************************************************************************************
 * create unit test for all functions of DiaryEntryRepositoryImpl.
 */
