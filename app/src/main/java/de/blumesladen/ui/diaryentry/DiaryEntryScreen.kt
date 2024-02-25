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

package de.blumesladen.ui.diaryentry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.blumesladen.R
import de.blumesladen.data.di.fakeDiaryEntrys
import de.blumesladen.data.local.database.DiaryEntry
import de.blumesladen.ui.diaryentry.DiaryEntriesUiState.Success
import de.blumesladen.ui.theme.MyApplicationTheme

@Composable
fun DiaryEntryScreen(modifier: Modifier = Modifier, viewModel: DiaryEntriesViewModel = hiltViewModel()) {
    val items by viewModel.uiState.collectAsStateWithLifecycle()
    if (items is Success) {
        DiaryEntryScreen(
            items = (items as Success).data,
            // onSave = viewModel::addDiaryEntry,
            modifier = modifier
        )
    }
}

@Composable
internal fun DiaryEntryScreen(
    items: List<DiaryEntry>,
    // onSave: (diaryEntry: DiaryEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        var nameDiaryEntry by remember { mutableStateOf("Compose") }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = nameDiaryEntry,
                onValueChange = { nameDiaryEntry = it }
            )

            /* @TODO(mblume) to implement onSave(null) to onSave(diaryEntry) */
            Button(modifier = Modifier.width(96.dp), onClick = { /* to be implemented */ }) {
                Text(stringResource(id = R.string.save_action))
            }
        }
        items.forEach {
            Text("${it.forMyself}")
        }
    }
}

// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        DiaryEntryScreen(fakeDiaryEntrys)
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        DiaryEntryScreen(fakeDiaryEntrys)
    }
}
