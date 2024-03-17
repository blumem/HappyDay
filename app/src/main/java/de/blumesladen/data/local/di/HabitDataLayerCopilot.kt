package de.blumesladen.data.local.di

import androidx.lifecycle.ViewModel
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import de.blumesladen.data.local.database.Habit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDateTime
import javax.inject.Inject

@Dao
interface HabitDao {
    @Query("SELECT * FROM habit WHERE isArchived = 0 AND isDeleted = 0 AND nextDueDateTime <= :currentDateTime ORDER BY isFavourite DESC, priority DESC")
    fun getAllHabits(currentDateTime: LocalDateTime = LocalDateTime.now()): Flow<List<Habit>>

    @Query("SELECT * FROM habit WHERE isArchived = 0 AND isDeleted = 0 AND isSnoozed = 0 AND nextDueDateTime <= :currentDateTime ORDER BY isFavourite DESC, priority DESC")
    fun getAllHabitsNotSnoozed(currentDateTime: LocalDateTime = LocalDateTime.now()): Flow<List<Habit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabits(habits: List<Habit>)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Delete
    suspend fun deleteHabits(habits: List<Habit>)
}

interface HabitRepository {
    fun getAllHabits(): Flow<List<Habit>>
    fun getAllHabitsNotSnoozed(): Flow<List<Habit>>
    suspend fun insertHabit(habit: Habit)
    suspend fun insertHabits(habits: List<Habit>)
    suspend fun deleteHabit(habit: Habit)
    suspend fun deleteHabits(habits: List<Habit>)
}


class HabitRepositoryImpl @Inject constructor(private val habitDao: HabitDao) : HabitRepository {
    override fun getAllHabits(): Flow<List<Habit>> = habitDao.getAllHabits()
    override fun getAllHabitsNotSnoozed(): Flow<List<Habit>> = habitDao.getAllHabitsNotSnoozed()
    override suspend fun insertHabit(habit: Habit) = habitDao.insertHabit(habit)
    override suspend fun insertHabits(habits: List<Habit>) = habitDao.insertHabits(habits)
    override suspend fun deleteHabit(habit: Habit) = habitDao.deleteHabit(habit)
    override suspend fun deleteHabits(habits: List<Habit>) = habitDao.deleteHabits(habits)
}

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val habitRepository: HabitRepository
) : ViewModel() {
    val habits = MutableStateFlow(habitRepository.getAllHabits())
    var currentHabit: Habit? = null

    fun updateCurrentHabit(habit: Habit) {
        currentHabit = habit
    }

    suspend fun insertHabit(habit: Habit) {
        habitRepository.insertHabit(habit)
    }

    suspend fun updateHabitAsDone(habit: Habit) {
        // habitRepository.insertHabit(habit.copy(isDone = true))
        // TODO(mblume) implement using HabitEntry
    }

    suspend fun deleteHabit(habit: Habit) {
        habitRepository.deleteHabit(habit)
    }
}
class FakeHabitRepository @Inject constructor() : HabitRepository {
    private val fakeHabits = listOf(
        Habit(name = "Fake Habit 1"),
        Habit(name = "Fake Habit 2")
    )

    override fun getAllHabits(): Flow<List<Habit>> = flowOf(fakeHabits)
    override fun getAllHabitsNotSnoozed(): Flow<List<Habit>> = flowOf(fakeHabits.filter { !it.isSnoozed })
    override suspend fun insertHabit(habit: Habit) { /* no-op */ }
    override suspend fun insertHabits(habits: List<Habit>) { /* no-op */ }
    override suspend fun deleteHabit(habit: Habit) { /* no-op */ }
    override suspend fun deleteHabits(habits: List<Habit>) { /* no-op */ }
}