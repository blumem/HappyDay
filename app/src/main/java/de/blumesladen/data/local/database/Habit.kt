package de.blumesladen.data.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime


@Entity
data class Habit (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String = "",
    val description: String = "",
    val createDateTime: LocalDateTime = LocalDateTime.now(),
    val nextDueDateTime: LocalDateTime = LocalDateTime.now(),
    val priority: Int = 0,
    val secondsUntilNext: Long = 0, // once it's done, this will be added to now() and stored in nextDueDateTime

    val isFavourite: Boolean = false,
    val isArchived: Boolean = false,
    val isDeleted: Boolean = false,
    val isSnoozed: Boolean = false,

    val snoozeDateTime: LocalDateTime? = null,
    val snoozeDuration: Int = 0,
    val snoozeDurationUnit: String = "",
    val snoozeCount: Int = 0
)

@Entity(tableName = "habit_entry",
    foreignKeys = [ForeignKey(
        entity = Habit::class,
        parentColumns = ["id"],
        childColumns = ["habit_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [androidx.room.Index(value = ["done_when"], unique = true)])
data class HabitEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "habit_id") val habitId: Long,
    @ColumnInfo(name = "done_when") val doneDateTime: LocalDateTime,
)
/**
 * *****************************************************************************************
 * Github co-pilot prompt for Data Layer:
 * *****************************************************************************************
Generate a DAO that allows to load, insert and delete single and multiple Habit records.
Create a create a second getAllHabits function returning multiple records as a Flow of List<Habit>.,
the result should be ordered by isFavourite first and second by priority in descending order
and should be filtering out isArchived and isDeleted records and records which nextDueDateTime>LocalDateTime.now().
Create a second function getAllHabitsNotSnoozed function also returning a Flow of List<Habit>,
that on top filters out snoozed habits.
For inserts use  @Insert(onConflict = OnConflictStrategy.REPLACE) as a strategy and return a the id
as Long.
Include a function updateHabit() to update a single Habit record.
Include a function markHabitAsDone() that does the following:
1) updates the nextDueDateTime of a Habit record adding secondsUntilNext to LocalDateTime.now().
2) create a HabitEntry with the current Habit as foreign key and the current LocalDateTime as doneDateTime
   and save it into the HabitEntry table.
Add a function getAllHabitEntries() to load all HabitEntries for a given Habit.
Add a function to load all HabitEntries for a given Habit and more recent than a provided date.
Add a function to delete the most recent HabitEntry for a given Habit.


Generate a HabitRepository Interface with a default implementation injecting the Dao via
hilt constructor to offering all functions of the HabitDao (including
 * markHabitAsDone(),
 * getAllHabitEntries(),
 * getAllHabitsNotSnoozed(now: LocalDateTime = LocalDateTime.now()),
 * getAllHabitEntriesAfterDate(),
 * deleteMostRecentHabitEntry()
).
It is important to have the getHabitsNotSnoozed() function in the repository.
Create a fake implementation of the HabitRepository as well with a hilt injected constructor
that returns a Flow of a list of Habits and is initialized with 2 fake Habit records for testing purposes
as well as for one habit returns 3 fake HabitEntries with random dates within the last 30 days.


Create a HabitViewModel based on HiltViewModel that takes a HabitRepository as a constructor parameter.
It loads all Habits using the HabitRepository using getAllHabitsNotSnoozed as view model state. For each Habit
it loaded, it also loads all HabitEntries for the Habit and stores them in a Map<Long, List<HabitEntry>> where
the Long is the Habit id.
Moreover, the ViewModel should provide a function to update the current habit value of the ViewModel,
that can be called from the Compose UI without storing it back into the repository.
Finally, the ViewModel should have a function to markHabitAsDone using the HabitRepository and
a function to delete the most recent HabitEntry deleteMostRecentHabitEntry().
 */

/**
* *****************************************************************************************
* Github co-pilot prompt for Compose UI:
* *****************************************************************************************
Create a jetpack Compose Function HabitCard() that renders a single Habit record using the a Material 3 card.
The text is displaying the habit name and in smaller font the line description below.
If the Habit isFavourite, the card should have a Icons.Rounded.Favorite icon in the top right corner.
If the Habit isSnoozed, the card should have a Icons.Rounded.Snooze before the text.
   Both icon and the text itself should be in case of isSnoozed in MaterialTheme.colorScheme.secondary.

Create two preview function for the Composable function that shows the HabitCard once with a
isSnoozed=true record and once with a isSnoozed=false fake Habit record.

Create a jetpack Compose function called HabitEditor that takes a HabitViewModel as a parameter.
 The function lists all habits in a LazyColumn using the HabitCard Composable.
  Each Card can be swiped left to snooze the habit.
  Each Card can be swiped right to mark the habit as done.
 Show in the lazy column a stickyHeader with the following content:
        Icons.Default.Snooze, contentDescription = "Snoozed")
        Icon(Icons.Default.KeyboardDoubleArrowLeft, contentDescription = "Left")
        Text("Habits")
        Icon(Icons.Default.KeyboardDoubleArrowRight, contentDescription = "Right")
        Icon(Icons.Default.Check, contentDescription = "Done")
 End of LazyColumn header.


*/

