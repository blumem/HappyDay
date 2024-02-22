package de.blumesladen.ui.diaryentry


import android.annotation.SuppressLint
import de.blumesladen.ui.theme.MyApplicationTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.blumesladen.R
import de.blumesladen.data.di.fakeDiaryEntrys
import de.blumesladen.data.local.database.DiaryEntry
import java.time.LocalDateTime

@Composable
fun DiaryEntryEditScreen(modifier: Modifier = Modifier, viewModel: DiaryEntryViewModel = hiltViewModel()) {
    val item by viewModel.uiState.collectAsStateWithLifecycle()

    DiaryEntryEditDialog(
        entry = item,
        onSave = viewModel::addDiaryEntry,
        // onValueChange = viewModel.,
        modifier = modifier
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiaryEntryEditDialog(
    entry : DiaryEntryUiState,
    onSave : (diaryEntry: DiaryEntry) -> Unit,
    // onValueChange: (DiaryEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {

        OutlinedTextField(
            value = entry.diaryEntry.forMyself,
            onValueChange = { entry.diaryEntry = entry.diaryEntry.copy(forMyself = it) },
            label = { Text(stringResource(R.string.diary_entry_formyself)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )
        Button(
            onClick = { onSave(entry.diaryEntry) },
            // enabled = itemUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save_action))
        }
    }

}


@Preview(showBackground = true)
@Composable
fun DiaryEntryEditScreenPreview() {
    MyApplicationTheme {
        DiaryEntryEditDialog(entry = fakeDiaryEntry, onSave = {})
    }
}


@SuppressLint("NewApi")
val fakeDiaryEntry : DiaryEntryUiState = DiaryEntryUiState(DiaryEntry(0,
    LocalDateTime.now(),
    0,
    0,
    "forMyself",
    "forOthers",
    "emotion",
    "",
    ""))