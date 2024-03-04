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

import de.blumesladen.data.local.database.DiaryEntry
import de.blumesladen.data.local.database.DiaryEntryDao
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.YearMonth
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import java.time.LocalDate

interface DiaryEntryRepository {
    val diaryEntrys: Flow<List<DiaryEntry>>

    suspend fun add(diaryEntry: DiaryEntry)

    suspend fun loadMonth(month: LocalDate) : Flow<List<DiaryEntry>>
}

class DefaultDiaryEntryRepository @Inject constructor(
    private val diaryEntryDao: DiaryEntryDao
) : DiaryEntryRepository {

    override val diaryEntrys: Flow<List<DiaryEntry>> =
        diaryEntryDao.getDiaryEntriesMostRecent() // .map { items -> items.map { it.name } }

    override suspend fun add(diaryEntry: DiaryEntry) {
        diaryEntryDao.insertDiaryEntry(diaryEntry)
    }

    override suspend fun loadMonth(month: LocalDate) : Flow<List<DiaryEntry>> {
        return diaryEntryDao.getDiaryEntriesForMonth(YearMonth.of(year=month.year, month=month.month))
    }
}