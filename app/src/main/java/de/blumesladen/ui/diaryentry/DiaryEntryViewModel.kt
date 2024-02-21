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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import de.blumesladen.data.DiaryEntryRepository
import de.blumesladen.data.local.database.DiaryEntry
import de.blumesladen.ui.diaryentry.DiaryEntryUiState.Error
import de.blumesladen.ui.diaryentry.DiaryEntryUiState.Loading
import de.blumesladen.ui.diaryentry.DiaryEntryUiState.Success
import javax.inject.Inject

@HiltViewModel
class DiaryEntryViewModel @Inject constructor(
    private val diaryEntryRepository: DiaryEntryRepository
) : ViewModel() {

    val uiState: StateFlow<DiaryEntryUiState> = diaryEntryRepository
        .diaryEntrys.map<List<DiaryEntry>, DiaryEntryUiState>(::Success)
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun addDiaryEntry(diaryEntry: DiaryEntry) {
        viewModelScope.launch {
            diaryEntryRepository.add(diaryEntry)
        }
    }
}

sealed interface DiaryEntryUiState {
    object Loading : DiaryEntryUiState
    data class Error(val throwable: Throwable) : DiaryEntryUiState
    data class Success(val data: List<DiaryEntry>) : DiaryEntryUiState
}