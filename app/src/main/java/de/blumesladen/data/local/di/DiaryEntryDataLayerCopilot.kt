package de.blumesladen.data.local.di

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import de.blumesladen.data.local.database.DiaryEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@Dao
interface DiaryEntryDao {
    @Query("SELECT * FROM diaryentry WHERE uid = :uid")
    fun getDiaryEntryByUid(uid: Int): Flow<DiaryEntry>

    @Query("SELECT * FROM diaryentry WHERE entry_date = :entryDate")
    fun getDiaryEntryByDate(entryDate: LocalDate): Flow<DiaryEntry>

    @Query("SELECT * FROM diaryentry ORDER BY entry_date DESC LIMIT 30")
    fun getDiaryEntriesMostRecent(): Flow<List<DiaryEntry>>

    @Query("SELECT * FROM diaryentry WHERE strftime('%Y-%m', entry_date) = :month")
    fun getDiaryEntriesByMonth(month: String): Flow<List<DiaryEntry>>

    @Query("SELECT * FROM diaryentry")
    fun getAllDiaryEntries(): Flow<List<DiaryEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiaryEntry(diaryEntry: DiaryEntry)

    @Query("DELETE FROM diaryentry WHERE uid = :uid")
    suspend fun deleteDiaryEntryByUid(uid: Int)
}

interface DiaryEntryRepository {
    fun getDiaryEntryByUid(uid: Int): Flow<DiaryEntry>
    fun getDiaryEntryByDate(entryDate: LocalDate): Flow<DiaryEntry>
    fun getDiaryEntriesMostRecent(): Flow<List<DiaryEntry>>
    fun getDiaryEntriesByMonth(month: String): Flow<List<DiaryEntry>>
    fun getAllDiaryEntries(): Flow<List<DiaryEntry>>
    suspend fun insertDiaryEntry(diaryEntry: DiaryEntry)
    suspend fun deleteDiaryEntryByUid(uid: Int)
}

class DiaryEntryRepositoryImpl @Inject constructor(
    private val diaryEntryDao: DiaryEntryDao
) : DiaryEntryRepository {
    override fun getDiaryEntryByUid(uid: Int): Flow<DiaryEntry> {
        return diaryEntryDao.getDiaryEntryByUid(uid)
    }

    override fun getDiaryEntryByDate(entryDate: LocalDate): Flow<DiaryEntry> {
        return diaryEntryDao.getDiaryEntryByDate(entryDate)
    }

    override fun getDiaryEntriesMostRecent(): Flow<List<DiaryEntry>> {
        return diaryEntryDao.getDiaryEntriesMostRecent()
    }

    override fun getDiaryEntriesByMonth(month: String): Flow<List<DiaryEntry>> {
        return diaryEntryDao.getDiaryEntriesByMonth(month)
    }

    override fun getAllDiaryEntries(): Flow<List<DiaryEntry>> {
        return diaryEntryDao.getAllDiaryEntries()
    }

    override suspend fun insertDiaryEntry(diaryEntry: DiaryEntry) {
        diaryEntryDao.insertDiaryEntry(diaryEntry)
    }

    override suspend fun deleteDiaryEntryByUid(uid: Int) {
        diaryEntryDao.deleteDiaryEntryByUid(uid)
    }
}

class FakeDiaryEntryRepository @Inject constructor() : DiaryEntryRepository {
    private val diaryEntries = mutableListOf<DiaryEntry>()
    private val diaryEntriesFlow = MutableStateFlow<List<DiaryEntry>>(diaryEntries)

    init {
        populateWithFakeData()
    }

    private fun populateWithFakeData() {
        for (i in 1..10) {
            val entry = DiaryEntry(
                uid = i,
                entryDate = LocalDate.now().minusDays(i.toLong()-1),
                forMyself = "For myself $i",
                forOthers = "For others $i",
                unexpressedEmotions = "Unexpressed emotions $i",
                somethingGood = "Something good $i",
                anticipation = "Anticipation $i",
                abstinent = i % 2,
                exercised = i % 2
            )
            diaryEntries.add(entry)
        }
        diaryEntriesFlow.value = diaryEntries
    }

    override fun getDiaryEntryByUid(uid: Int): Flow<DiaryEntry> {
        return diaryEntriesFlow.map { entries -> entries.first { it.uid == uid } }
    }

    override fun getDiaryEntryByDate(entryDate: LocalDate): Flow<DiaryEntry> {
        return diaryEntriesFlow.map { entries -> entries.first { it.entryDate == entryDate } }
    }

    override fun getDiaryEntriesMostRecent(): Flow<List<DiaryEntry>> {
        return diaryEntriesFlow.map { entries -> entries.sortedByDescending { it.entryDate }.take(30) }
    }

    override fun getDiaryEntriesByMonth(month: String): Flow<List<DiaryEntry>> {
        return diaryEntriesFlow.map { entries -> entries.filter { it.entryDate.format(
            DateTimeFormatter.ofPattern("yyyy-MM")) == month } }
    }

    override fun getAllDiaryEntries(): Flow<List<DiaryEntry>> {
        return diaryEntriesFlow
    }

    override suspend fun insertDiaryEntry(diaryEntry: DiaryEntry) {
        diaryEntries.add(diaryEntry)
        diaryEntriesFlow.value = diaryEntries
    }

    override suspend fun deleteDiaryEntryByUid(uid: Int) {
        diaryEntries.removeIf { it.uid == uid }
        diaryEntriesFlow.value = diaryEntries
    }
}

@HiltViewModel
class DiaryEntryViewModel @Inject constructor(
    private val diaryEntryRepository: DiaryEntryRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var date = savedStateHandle.get<String>("date")?.let { LocalDate.parse(it) } ?: LocalDate.now()

    var diaryEntry = mutableStateOf(DiaryEntry(entryDate=date))
        private set

    init {
        getDiaryEntry(date)
    }

    fun getDiaryEntry(date: LocalDate) {
        this.date = date
        Log.d("MBL", "DiaryEntryViewModel.getDiaryEntry: fetching entry for $date")
        viewModelScope.launch {
            val entry = diaryEntryRepository.getDiaryEntryByDate(date).firstOrNull()
            diaryEntry.value = entry ?: DiaryEntry(entryDate = date)
            Log.d("MBL", "DiaryEntryViewModel.getDiaryEntry: $diaryEntry")
        }
    }

    fun updateDiaryEntry(diaryEntry: DiaryEntry) {
        this.date = diaryEntry.entryDate
        this.diaryEntry.value = diaryEntry
    }

    fun insertDiaryEntry(diaryEntry: DiaryEntry) {
        this.date = diaryEntry.entryDate
        viewModelScope.launch {
            diaryEntryRepository.insertDiaryEntry(diaryEntry)
        }
    }

    fun deleteDiaryEntry(uid: Int) {
        viewModelScope.launch {
            diaryEntryRepository.deleteDiaryEntryByUid(uid)
        }
    }
}