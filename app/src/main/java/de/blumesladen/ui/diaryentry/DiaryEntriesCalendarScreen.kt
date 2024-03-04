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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import de.blumesladen.data.di.fakeDiaryEntrys
import de.blumesladen.data.local.database.DiaryEntry
import de.blumesladen.ui.Screen
import de.blumesladen.ui.diaryentry.DiaryEntriesUiState.Success
import de.blumesladen.ui.theme.MyApplicationTheme
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.header.MonthState
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun DiaryEntryScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DiaryEntriesViewModel = hiltViewModel()
) {
    val items by viewModel.uiState.collectAsStateWithLifecycle()
    val itemsFlow by viewModel.diaryEntryFlow.collectAsState(0)
    if (items is Success) {
        DiaryEntryScreenForm(
            items = (items as Success).data,
            // itemsFlow = itemsFlow,
            onSelectionChanged = {
                viewModel.onSelectionChanged(it)
                navController.navigate(Screen.DiaryEntryEditRoute.route) },
            onMonthChanged = viewModel::onMonthChanged,
        )
    }
}

@Composable
internal fun DiaryEntryScreenForm(
    items: List<DiaryEntry>,
    // itemsFlow: Flow<List<DiaryEntry>>,
    onSelectionChanged: (diaryEntry: List<LocalDate>) -> Unit,
    onMonthChanged: (newMonth: YearMonth) -> Unit,
    // modifier: Modifier = Modifier
) {
    val monthState = rememberMonthStateDefault()
    LaunchedEffect(monthState) {
        snapshotFlow { monthState.currentMonth }
            .onEach { onMonthChanged(it) }
            .launchIn(this)
    }
    val state = rememberSelectableCalendarState(
        confirmSelectionChange = { onSelectionChanged(it); true },
        monthState = monthState,
        initialSelectionMode = SelectionMode.Single,
    )

    Column(
        Modifier.verticalScroll(rememberScrollState())
    ) {
        SelectableCalendar(
            calendarState = state,
            dayContent = { dayState ->
                DayContent(
                    dayState = dayState,
                    item = items.firstOrNull { it.entryDate == dayState.date },
                )
            }
        )
//        Spacer(modifier = Modifier.height(20.dp))
//        Text(
//            text = "Selected recipes price: $selectedPrice",
//            style = MaterialTheme.typography.h6,
//        )
//        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun rememberMonthStateDefault() = rememberSaveable(saver = MonthState.Saver()) {
    MonthState(
        initialMonth = YearMonth.now(),
        minMonth = YearMonth.now().minusMonths(10000),
        maxMonth = YearMonth.now().plusMonths(10000),
    )
}

@Composable
private fun DayContent(
    dayState: DayState<DynamicSelectionState>,
    item: DiaryEntry?,
    modifier: Modifier = Modifier,
) {
    Card (
        modifier = modifier
        .aspectRatio(1f)
        .padding(2.dp),
        elevation = if (dayState.isFromCurrentMonth) 4.dp else 0.dp,
        border = if (dayState.isCurrentDay) BorderStroke(1.dp, MaterialTheme.colors.primary) else null,
        contentColor = if (dayState.selectionState.isDateSelected(dayState.date)) MaterialTheme.colors.secondary else contentColorFor(
            backgroundColor = MaterialTheme.colors.surface
        )
    )
    {
        Column(
            modifier = Modifier
                .padding(vertical = 2.dp)
                .clickable { dayState.selectionState.onDateSelected(dayState.date) },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            androidx.compose.material.Text(
                text = dayState.date.dayOfMonth.toString(),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6,
            )
            // Divider(color = Color.Blue, thickness = 4.dp)
            if (item != null) {
                Divider(color = Color.Green,
                    thickness = 4.dp,
                    modifier = Modifier.padding(vertical = 2.dp))
            }
        }
    }

}


// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        DiaryEntryScreenForm(
            fakeDiaryEntrys,
            onMonthChanged = {},
            onSelectionChanged = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        DiaryEntryScreenForm(
            fakeDiaryEntrys,
            onMonthChanged = {},
            onSelectionChanged = {}
        )
    }
}
