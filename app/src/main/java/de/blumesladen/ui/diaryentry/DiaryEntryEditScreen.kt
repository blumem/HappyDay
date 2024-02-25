package de.blumesladen.ui.diaryentry


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import de.blumesladen.R
import de.blumesladen.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

@Composable
fun DiaryEntryEditScreen(modifier: Modifier = Modifier, viewModel: DiaryEntryViewModel = hiltViewModel()) {
    val coroutineScope = rememberCoroutineScope()

    Column (
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        DiaryEntryEditDialog(
            entry = viewModel.uiState,
            onValueChange = viewModel::updateUiState,
            modifier = modifier.fillMaxWidth()
        )
        Button(
            onClick = {  coroutineScope.launch {
                viewModel.addDiaryEntry(viewModel.uiState.diaryEntryDetails.toDiaryEntry())
                //navigateBack()
            }},
            enabled = true,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save_action))
        }
    }
}
@Composable
fun DiaryEntryEditDialog(
    entry : DiaryEntryUiState,
    onValueChange: (DiaryEntryDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    val details : DiaryEntryDetails = entry.diaryEntryDetails
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {

        OutlinedTextField(
            value = details.forMyself,
            onValueChange = { onValueChange(details.copy(forMyself = it)) },
            label = { Text(stringResource(R.string.diary_entry_for_myself)) },
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
    }

}


@Preview(showBackground = true)
@Composable
fun DiaryEntryEditScreenPreview() {
    MyApplicationTheme {
        DiaryEntryEditDialog(entry = fakeDiaryUiState, onValueChange = {})
    }
}


@SuppressLint("NewApi")
val fakeDiaryUiState : DiaryEntryUiState = DiaryEntryUiState(DiaryEntryDetails(0,
//    LocalDateTime.now(),
//    0,
//    0,
    "forMyself",
    "forOthers",
//    "emotion",
//    "",
//    ""
    ))