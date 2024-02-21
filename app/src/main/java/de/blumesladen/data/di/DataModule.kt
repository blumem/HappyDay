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

package de.blumesladen.data.di

import android.annotation.SuppressLint
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import de.blumesladen.data.DiaryEntryRepository
import de.blumesladen.data.DefaultDiaryEntryRepository
import de.blumesladen.data.local.database.DiaryEntry
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsDiaryEntryRepository(
        diaryEntryRepository: DefaultDiaryEntryRepository
    ): DiaryEntryRepository
}

class FakeDiaryEntryRepository @Inject constructor() : DiaryEntryRepository {
    override val diaryEntrys: Flow<List<DiaryEntry>> = flowOf(fakeDiaryEntrys)

    override suspend fun add(diaryEntry: DiaryEntry) {
        throw NotImplementedError()
    }
}

@SuppressLint("NewApi")
val fakeDiaryEntrys = listOf(
    DiaryEntry(0, LocalDateTime.now(),1,1,"bike tour","made cashier smile","frustration","cookies"),
    DiaryEntry(1, LocalDateTime.of(2024,1,1,0,0,0,0),1,1,"small walk","brought cookies to ping pong","anger",""),
    DiaryEntry(2, LocalDateTime.of(2024,1,2,0,0,0,0),1,1,"Yoga","cleaned the floor","honry","")
)
