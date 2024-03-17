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

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import de.blumesladen.data.local.di.DiaryEntryDao
import de.blumesladen.data.local.di.HabitDao
import java.time.LocalDate
import java.time.LocalDateTime

@Database(entities = [DiaryEntry::class, Habit::class],version= 3)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val diaryEntryDao: DiaryEntryDao
    abstract val habitDao: HabitDao
}
class Converters {
    /**
     * Convert a string value to a [LocalDate]
     */
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }

    @TypeConverter
    fun toTimestamp(date: LocalDate?): String? {
        return date?.toString()
    }

    /**
     * Convert a string value to a [LocalDateTime]
     */
    @TypeConverter
    fun fromDateTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun toDateTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }
}