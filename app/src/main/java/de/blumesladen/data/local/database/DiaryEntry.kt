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
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.TypeConverter
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity
data class DiaryEntry constructor(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    val uid: Int = 0,

    @ColumnInfo(name = "entry_date")
    val entryDate: LocalDateTime = LocalDateTime.now(),
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
    val entryDateFormatted : String
        get() = entryDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
}

@Dao
interface DiaryEntryDao {
    @Query("SELECT * FROM diaryentry ORDER BY uid DESC LIMIT 10")
    fun getDiaryEntrys(): Flow<List<DiaryEntry>>

    @Insert
    suspend fun insertDiaryEntry(item: DiaryEntry)
}


class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }
}