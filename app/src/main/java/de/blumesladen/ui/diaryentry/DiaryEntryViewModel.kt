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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.blumesladen.data.DiaryEntryRepository
import de.blumesladen.data.local.database.DiaryEntry
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DiaryEntryViewModel @Inject constructor(
    private val diaryEntryRepository: DiaryEntryRepository
) : ViewModel() {

    // Backing property to avoid state updates from other classes
    var uiState by mutableStateOf(DiaryEntryUiState())
        private set

    /**
     * Updates the [DiaryEntryUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(diaryEntryDetails : DiaryEntryDetails) {
       uiState = DiaryEntryUiState(diaryEntryDetails)
    }

    fun addDiaryEntry(diaryEntry: DiaryEntry) {
        viewModelScope.launch {
            diaryEntryRepository.add(diaryEntry)
        }
    }
}

data class DiaryEntryUiState (
    val diaryEntryDetails: DiaryEntryDetails = DiaryEntryDetails()
)

data class DiaryEntryDetails(
    val id: Int =0,
    val entryDate: LocalDate = LocalDate.now(),
    val forMyself: String = "",
    val forOthers: String = "",
    val unexpressedEmotions: String? = "",
    val somethingGood: String? = "",
    val anticipation: String? = ""
)

fun DiaryEntryDetails.toDiaryEntry(): DiaryEntry = DiaryEntry(
    uid = id,
    entryDate = entryDate,
    forMyself = forMyself,
    forOthers = forOthers,
    unexpressedEmotions = unexpressedEmotions,
    somethingGood = somethingGood,
    anticipation = anticipation)

fun DiaryEntry.toDiaryEntryUiState() : DiaryEntryUiState = DiaryEntryUiState(this.toDiaryEntryDetails())

fun DiaryEntry.toDiaryEntryDetails(): DiaryEntryDetails = DiaryEntryDetails(
    id = uid,
    entryDate = entryDate,
    forMyself = forMyself,
    forOthers = forOthers,
    unexpressedEmotions = unexpressedEmotions,
    somethingGood = somethingGood,
    anticipation = anticipation
)