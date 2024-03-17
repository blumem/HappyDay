package de.blumesladen.ui.habit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material.icons.filled.Snooze
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Snooze
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import de.blumesladen.data.local.database.Habit
import de.blumesladen.data.local.di.FakeHabitRepository
import de.blumesladen.data.local.di.HabitViewModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)

@Composable
fun HabitListView(viewModel: HabitViewModel = hiltViewModel()) {
    val habitsFlow by viewModel.habits.collectAsState(initial = flowOf(emptyList()))
    val habits by habitsFlow.collectAsState(initial = emptyList())
    val state = rememberLazyListState()
    val scope = rememberCoroutineScope()
    LazyColumn (
        state = state,
        verticalArrangement = Arrangement.Top,
        userScrollEnabled = true
    )  {
        stickyHeader {
            Row(horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.inversePrimary)
                .padding(vertical = 10.dp)
                .clickable {
                    scope.launch { state.animateScrollToItem(2) }
                }
            ) {
                Icon(Icons.Default.Snooze, contentDescription = "Snoozed")
                Icon(Icons.Default.KeyboardDoubleArrowLeft, contentDescription = "Left")
                Text("Habits")
                Icon(Icons.Default.KeyboardDoubleArrowRight, contentDescription = "Right")
                Icon(Icons.Default.Check, contentDescription = "Done")
            }
        }
        items(habits) { habit ->
            val dismissState = rememberDismissState()
            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
                background = { },
                dismissContent = {
                    HabitCard(habit)
                    Text(habit.name)
                    Text("Hello World")
                }
            )
            when (dismissState.currentValue) {
                DismissValue.DismissedToEnd -> viewModel.updateCurrentHabit(habit)
                DismissValue.DismissedToStart -> viewModel.updateCurrentHabit(habit.copy(isSnoozed = true))
                else -> { /* Do nothing */ }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHabitListView() {
    val fakeHabitViewModel = HabitViewModel(FakeHabitRepository())
    HabitListView(fakeHabitViewModel)
}


@Composable
public fun HabitCard(habit: Habit) {
    Card(modifier = Modifier.padding(8.dp)) {
        Row {
            if (habit.isSnoozed) {
                Icon(
                    Icons.Rounded.Snooze,
                    contentDescription = "Snoozed",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
            Text(
                text = habit.name,
                style = MaterialTheme.typography.bodyMedium,
                color = if (habit.isSnoozed) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            if (habit.isFavourite) {
                Icon(
                    Icons.Rounded.Favorite,
                    contentDescription = "Favourite",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Text(
            text = habit.description,
            style = MaterialTheme.typography.bodySmall,
            color = if (habit.isSnoozed) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
public fun PreviewSnoozedHabitCard() {
    val snoozedHabit = Habit(name = "Snoozed Habit", isSnoozed = true)
    HabitCard(habit = snoozedHabit)
}

@Preview(showBackground = true)
@Composable
public fun PreviewNotSnoozedHabitCard() {
    val notSnoozedHabit = Habit(name = "Not Snoozed Habit", isSnoozed = false)
    HabitCard(habit = notSnoozedHabit)
}