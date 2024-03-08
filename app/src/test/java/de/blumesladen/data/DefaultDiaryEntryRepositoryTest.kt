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

package de.blumesladen.data

/**
 * Unit tests for [DefaultDiaryEntryRepository].
 */

import de.blumesladen.data.local.database.DiaryEntry
import de.blumesladen.ui.diaryentry.DiaryEntryDao
import de.blumesladen.ui.diaryentry.DiaryEntryRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.time.LocalDate

class DiaryEntryRepositoryImplTest {

    @Mock
    private lateinit var diaryEntryDao: DiaryEntryDao

    private lateinit var diaryEntryRepository: DiaryEntryRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        diaryEntryRepository = DiaryEntryRepositoryImpl(diaryEntryDao)
    }

    @Test
    fun getDiaryEntryByUid_returnsCorrectDiaryEntry() = runBlocking {
        val expectedDiaryEntry = DiaryEntry(uid = 1)
        Mockito.`when`(diaryEntryDao.getDiaryEntryByUid(1)).thenReturn(flowOf(expectedDiaryEntry))

        val actualDiaryEntry = diaryEntryRepository.getDiaryEntryByUid(1).first()

        assertEquals(expectedDiaryEntry, actualDiaryEntry)
    }

    @Test
    fun getDiaryEntryByDate_returnsCorrectDiaryEntry() = runBlocking {
        val expectedDiaryEntry = DiaryEntry(entryDate = LocalDate.now())
        Mockito.`when`(diaryEntryDao.getDiaryEntryByDate(LocalDate.now())).thenReturn(flowOf(expectedDiaryEntry))

        val actualDiaryEntry = diaryEntryRepository.getDiaryEntryByDate(LocalDate.now()).first()

        assertEquals(expectedDiaryEntry, actualDiaryEntry)
    }

    @Test
    fun getDiaryEntriesMostRecent_returnsCorrectDiaryEntries() = runBlocking {
        val expectedDiaryEntries = listOf(DiaryEntry(uid = 1), DiaryEntry(uid = 2))
        Mockito.`when`(diaryEntryDao.getDiaryEntriesMostRecent()).thenReturn(flowOf(expectedDiaryEntries))

        val actualDiaryEntries = diaryEntryRepository.getDiaryEntriesMostRecent().first()

        assertEquals(expectedDiaryEntries, actualDiaryEntries)
    }

    @Test
    fun getDiaryEntriesByMonth_returnsCorrectDiaryEntries() = runBlocking {
        val expectedDiaryEntries = listOf(DiaryEntry(uid = 1), DiaryEntry(uid = 2))
        Mockito.`when`(diaryEntryDao.getDiaryEntriesByMonth("2022-12")).thenReturn(flowOf(expectedDiaryEntries))

        val actualDiaryEntries = diaryEntryRepository.getDiaryEntriesByMonth("2022-12").first()

        assertEquals(expectedDiaryEntries, actualDiaryEntries)
    }

    @Test
    fun getAllDiaryEntries_returnsCorrectDiaryEntries() = runBlocking {
        val expectedDiaryEntries = listOf(DiaryEntry(uid = 1), DiaryEntry(uid = 2))
        Mockito.`when`(diaryEntryDao.getAllDiaryEntries()).thenReturn(flowOf(expectedDiaryEntries))

        val actualDiaryEntries = diaryEntryRepository.getAllDiaryEntries().first()

        assertEquals(expectedDiaryEntries, actualDiaryEntries)
    }

    @Test
    fun insertDiaryEntry_insertsDiaryEntryCorrectly() = runBlocking {
        val diaryEntry = DiaryEntry(uid = 1)

        diaryEntryRepository.insertDiaryEntry(diaryEntry)

        Mockito.verify(diaryEntryDao).insertDiaryEntry(diaryEntry)
    }

    @Test
    fun deleteDiaryEntryByUid_deletesDiaryEntryCorrectly() = runBlocking {
        val uid = 1

        diaryEntryRepository.deleteDiaryEntryByUid(uid)

        Mockito.verify(diaryEntryDao).deleteDiaryEntryByUid(uid)
    }
}