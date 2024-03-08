package de.blumesladen.ui.diaryentry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import de.blumesladen.R
import de.blumesladen.data.local.database.DiaryEntry
import java.time.LocalDate


@Composable
fun DiaryEntryEditor(
    diaryEntry: DiaryEntry,
    onValueChange: (DiaryEntry) -> Unit
) {
    var showDatePickerDialog by remember { mutableStateOf(false) }

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = diaryEntry.entryDate.toString(),
                onValueChange = { },
                label = { Text(stringResource(R.string.diary_entry_date)) },
                readOnly = true,
                modifier = Modifier.weight(0.75f)
            )

            IconButton(onClick = { showDatePickerDialog = true }) {
                Icon(CalendarTodayIcon(), contentDescription = stringResource(R.string.calendar_button_content_description))
            }
        }

        if (showDatePickerDialog) {
            MyDatePickerDialog(
                onDateSelected = { newDate ->
                    onValueChange(diaryEntry.copy(entryDate = LocalDate.parse(newDate)))
                    showDatePickerDialog = false
                },
                onDismiss = { showDatePickerDialog = false }
            )
        }

        OutlinedTextField(
            value = diaryEntry.forMyself,
            onValueChange = { newValue ->
                onValueChange(diaryEntry.copy(forMyself = newValue))
            },
            label = { Text(stringResource(R.string.diary_entry_for_myself)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = diaryEntry.forOthers,
            onValueChange = { newValue ->
                onValueChange(diaryEntry.copy(forOthers = newValue))
            },
            label = { Text(stringResource(R.string.diary_entry_for_others)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = diaryEntry.unexpressedEmotions ?: "",
            onValueChange = { newValue ->
                onValueChange(diaryEntry.copy(unexpressedEmotions = newValue))
            },
            label = { Text(stringResource(R.string.diary_entry_unexpressed_emotions)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = diaryEntry.somethingGood ?: "",
            onValueChange = { newValue ->
                onValueChange(diaryEntry.copy(somethingGood = newValue))
            },
            label = { Text(stringResource(R.string.diary_entry_something_good)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = diaryEntry.anticipation ?: "",
            onValueChange = { newValue ->
                onValueChange(diaryEntry.copy(anticipation = newValue))
            },
            label = { Text(stringResource(R.string.diary_entry_anticipation)) },
            modifier = Modifier.fillMaxWidth()
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = diaryEntry.abstinent == 1,
                onCheckedChange = { isChecked ->
                    onValueChange(diaryEntry.copy(abstinent = if (isChecked) 1 else 0))
                }
            )
            Text(stringResource(R.string.diary_entry_abstinent))
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = diaryEntry.exercised == 1,
                onCheckedChange = { isChecked ->
                    onValueChange(diaryEntry.copy(exercised = if (isChecked) 1 else 0))
                }
            )
            Text(stringResource(R.string.diary_entry_exercised))
        }
    }
}

@Composable
fun DiaryEntryEditScreen(
    viewModel: DiaryEntryViewModel = hiltViewModel(),
    date: LocalDate,
    navController: NavController
) {
    val diaryEntry by remember { viewModel.diaryEntry }

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        DiaryEntryEditor(diaryEntry, viewModel::updateDiaryEntry)

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = {
                viewModel.insertDiaryEntry(diaryEntry)
                navController.popBackStack()
            }) {
                Text(stringResource(R.string.save))
            }

            Button(onClick = { navController.popBackStack() }) {
                Text(stringResource(R.string.cancel))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiaryEntryEditorPreview() {
    val dummyDiaryEntry = DiaryEntry()
    val dummyOnValueChange: (DiaryEntry) -> Unit = {}

    DiaryEntryEditor(
        diaryEntry = dummyDiaryEntry,
        onValueChange = dummyOnValueChange
    )
}

@Preview(showBackground = true)
@Composable
fun DiaryEntryEditScreenPreview() {
    val dummyViewModel = DiaryEntryViewModel(
        diaryEntryRepository = FakeDiaryEntryRepository(),
        savedStateHandle = SavedStateHandle()
    )
    val dummyDate = LocalDate.now()

    DiaryEntryEditScreen(
        viewModel = dummyViewModel,
        date = dummyDate,
        navController = rememberNavController()
    )
}