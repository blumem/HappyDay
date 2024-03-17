package de.blumesladen.data.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime


@Entity(tableName = "habit")
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

/**
 * *****************************************************************************************
 * Github co-pilot prompt for Data Layer:
 * *****************************************************************************************
Generate a DAO that allows to load, insert and delete single and multiple Habit records.
Create a create a second getAllHabits function loading multiple records,
the result should be ordered by isFavourite first and second by priority in descending order
and should be filtering out isArchived and isDeleted records and records which nextDueDateTime>LocalDateTime.now().
Create a second function getAllHabitsNotSnoozed function that on top filters out snoozed habits.
For inserts use  @Insert(onConflict = OnConflictStrategy.REPLACE) as a strategy.


Generate a HabitRepository Interface with a default implementation injecting the Dao via
hilt constructor to load, insert and delete single and multiple Habit records
including support for getAllHabitsNotSnoozed..
Create a fake implementation of the HabitRepository that returns a Flow of a list of Habits and
is initialized with 2 fake Habit records for testing purposes.

Create a HabitViewModel based on HiltViewModel that takes a HabitRepository as a constructor parameter.
It loads all Habits using the HabitRepository using getAllHabitsNotSnoozed as view model state.

Moreover, the ViewModel should provide a function to update the current habit value of the ViewModel,
that can be called from the Compose UI without storing it back into the repository.
Finally, the ViewModel should have a function to insert an DiaryEntry into the HabitRepository and
a function to delete the Entry.
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

Create a jetpack Compose function called HabitEditor that takes a Habit and a
function onValueChange calling viewModel::updateHabit as parameters.
The function does not create it's own mutable state of the DiaryEntry but uses the one passed as parameter.
Therefore every onValueChange inside the function for each component should call the onValueChange-parameter function
just passing the diaryEntry.copy() with the new value.
For all labels, use the XML string resources.
*/

@Entity(tableName = "habit_entry",
    foreignKeys = [ForeignKey(
        entity = Habit::class,
        parentColumns = ["id"],
        childColumns = ["habit_id"],
        onDelete = ForeignKey.CASCADE
    )])
data class HabitEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "habit_id") val habitId: Long,
    val doneDateTime: LocalDateTime,
)