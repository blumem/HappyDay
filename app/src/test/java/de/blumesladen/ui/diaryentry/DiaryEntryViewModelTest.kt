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


import de.blumesladen.data.DiaryEntryRepository
import de.blumesladen.data.local.database.DiaryEntry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class DiaryEntryViewModelTest {
    @Test
    fun uiState_initiallyLoading() = runTest {
        val viewModel = DiaryEntriesViewModel(FakeDiaryEntryRepository())
        assertEquals(viewModel.uiState.first(), DiaryEntriesUiState.Loading)
    }

    @Test
    fun uiState_onItemSaved_isDisplayed() = runTest {
        val viewModel = DiaryEntriesViewModel(FakeDiaryEntryRepository())
        assertEquals(viewModel.uiState.first(), DiaryEntriesUiState.Loading)
    }
}

private class FakeDiaryEntryRepository : DiaryEntryRepository {

    private val data = mutableListOf<DiaryEntry>()

    override val diaryEntrys: Flow<List<DiaryEntry>>
        get() = flow { emit(data.toList()) }

    override suspend fun add(entry: DiaryEntry) {
        data.add(0, entry)
    }
}
