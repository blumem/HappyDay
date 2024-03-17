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

package de.blumesladen.data.local.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.blumesladen.data.local.database.AppDatabase
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {


    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context
//                           habitProvider: Provider<HabitDao>
    ): AppDatabase {
        return Room.databaseBuilder(
            appContext.applicationContext,
            AppDatabase::class.java,
            "HappyDayDatabase.db",
        )
            .fallbackToDestructiveMigration()
//            .addCallback(RoomDbInitializer(habitProvider = habitProvider))
            .build()
    }

    @Provides
    fun provideDiaryEntryDao(db: AppDatabase): DiaryEntryDao = db.diaryEntryDao


    @Provides
    fun provideHabitDao(db: AppDatabase): HabitDao = db.habitDao

}

// INSERT INTO Habit (id, name, description, createDateTime, nextDueDateTime, priority, secondsUntilNext, isFavourite, isArchived, isDeleted, isSnoozed,snoozeDuration, snoozeDurationUnit,snoozeCount)
//    VALUES (1, "abstinent", "Did I stay abstinent today?",'2021-12-01T14:30:15', '2024-03-16T14:30:15', 0,  86400, 1,0,0,0,0,"days",0)
//INSERT INTO Habit (id, name, description, createDateTime, nextDueDateTime, priority, secondsUntilNext, isFavourite, isArchived, isDeleted, isSnoozed,snoozeDuration, snoozeDurationUnit,snoozeCount)
//   VALUES (2, "exercised", "Did I exercise today?",'2021-12-01T14:30:15', '2024-03-16T14:30:15', 0,  86400, 1,0,0,0,0,"days",0)

//class RoomDbInitializer(
//    private val habitProvider: Provider<HabitDao>,
//) : RoomDatabase.Callback() {
//    private val applicationScope = CoroutineScope(SupervisorJob())
//
//    override fun onCreate(db: SupportSQLiteDatabase) {
//        super.onCreate(db)
//        applicationScope.launch(Dispatchers.IO) {
//            populateDatabase()
//        }
//    }
//
//    private suspend fun populateDatabase() {
//        // Insert your default Habit rows here
//        val defaultHabit1 = Habit(name = "Abstinent", description = "Have I stayed abstinent today?")
//        val defaultHabit2 = Habit(name = "Exercised", description = "Have I exercised today?")
//
//        habitProvider.get().insertHabit(defaultHabit1)
//        habitProvider.get().insertHabit(defaultHabit2)
//    }
//}




