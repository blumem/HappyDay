package de.blumesladen.ui.diaryentry


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import de.blumesladen.R
import de.blumesladen.data.di.FakeDiaryEntryRepository
import de.blumesladen.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun DiaryEntryEditDialog(
    navController : NavController,
    modifier: Modifier = Modifier,
    viewModel: DiaryEntryViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    Column (
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        DiaryEntryEditInputFields(
            entry = viewModel.uiState,
            onValueChange = viewModel::updateUiState,
            modifier = modifier.fillMaxWidth()
        )
        Row() {
            Button(
                onClick = {  coroutineScope.launch {
                    viewModel.addDiaryEntry(viewModel.uiState.diaryEntryDetails.toDiaryEntry())
                    Log.i("SAVE_DIARY_ENTRY", viewModel.uiState.diaryEntryDetails.toString())
                    navController.popBackStack()
                }},
                enabled = true,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.weight(0.5f)
            ) {
                Text(text = stringResource(R.string.save_action))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    navController.popBackStack()
                },
                enabled = true,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.weight(0.5f)
            ) {
                Text(text = stringResource(R.string.cancel_action))
            }
        }
    }
}
@Composable
fun DiaryEntryEditInputFields(
    entry : DiaryEntryUiState,
    onValueChange: (DiaryEntryDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    val details : DiaryEntryDetails = entry.diaryEntryDetails
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
        modifier = modifier.padding(vertical = dimensionResource(id = R.dimen.padding_small))
    ) {
        val selectedDate = remember { mutableStateOf(details.entryDate)}
        val showDatePicker = remember { mutableStateOf(false) }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {

            TextField(
                value = selectedDate.value.format(DateTimeFormatter.ISO_DATE),
                onValueChange = {  },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                label = { Text("Date:") },
                enabled = false,
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { showDatePicker.value=true },
                modifier = Modifier
                    .size(width = 160.dp, height = 60.dp)
                    .weight(0.25f),
                content = {
                    Icon(painterResource(id = R.drawable.calendar_month),
                        contentDescription = stringResource(R.string.calendar_button_content_description)
                    )
                }
            )
        }
        if (showDatePicker.value) {
            MyDatePickerDialog(
                onDismiss = { showDatePicker.value = false },
                onDateSelected =  { selectedDate.value = LocalDate.parse(it) }
            )
        }
        OutlinedTextField(
            value = details.forMyself,
            onValueChange = { onValueChange(details.copy(forMyself = it)) },
            label = { Text(stringResource(R.string.diary_entry_for_myself)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("TEST_TAG"),
            enabled = true,
            singleLine = true
        )
        OutlinedTextField(
            value = details.forOthers,
            onValueChange = { onValueChange(details.copy(forOthers = it)) },
            label = { Text(stringResource(R.string.diary_entry_for_others)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )
        OutlinedTextField(
            value = details.unexpressedEmotions ?: "",
            onValueChange = { onValueChange(details.copy(unexpressedEmotions = it)) },
            label = { Text(stringResource(R.string.diary_entry_unexpressed_emotions)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )
        OutlinedTextField(
            value = details.somethingGood?: "",
            onValueChange = { onValueChange(details.copy(somethingGood = it)) },
            label = { Text(stringResource(R.string.diary_entry_something_good)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )
        OutlinedTextField(
            value = details.anticipation?: "",
            onValueChange = { onValueChange(details.copy(anticipation = it)) },
            label = { Text(stringResource(R.string.diary_entry_anticipation)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePickerDialog(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis <= System.currentTimeMillis()
        }
    })

    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onDateSelected(selectedDate)
                onDismiss()
            }

            ) {
                Text(text = stringResource(id = R.string.ok_action))
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = stringResource(id = R.string.cancel_action))
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

private fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    return formatter.format(Date(millis))
}

@Preview(showBackground = true)
@Composable
fun DiaryEntryEditInputFieldsPreview() {
    MyApplicationTheme {
        DiaryEntryEditInputFields(entry = fakeDiaryUiState, onValueChange = {})
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DiaryEntryEditScreenPreview() {
    val viewModel = DiaryEntryViewModel(FakeDiaryEntryRepository())
    MyApplicationTheme {
        DiaryEntryEditDialog(navController = rememberNavController(), viewModel=viewModel)
    }
}


@SuppressLint("NewApi")
val fakeDiaryUiState : DiaryEntryUiState = DiaryEntryUiState(DiaryEntryDetails(
    0,
    LocalDate.now(),
//    0,
//    0,
    "forMyself",
    "forOthers",
//    "emotion",
//    "",
//    ""
))