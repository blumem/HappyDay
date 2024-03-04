package de.blumesladen.ui.diaryentry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.blumesladen.data.DiaryEntryRepository
import de.blumesladen.data.local.database.DiaryEntry
import de.blumesladen.ui.diaryentry.DiaryEntriesUiState.Error
import de.blumesladen.ui.diaryentry.DiaryEntriesUiState.Loading
import de.blumesladen.ui.diaryentry.DiaryEntriesUiState.Success
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DiaryEntriesViewModel @Inject constructor(
    private val diaryEntryRepository: DiaryEntryRepository
) : ViewModel() {

    val uiState: StateFlow<DiaryEntriesUiState> = diaryEntryRepository
        .diaryEntrys.map<List<DiaryEntry>, DiaryEntriesUiState>(::Success)
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    private val selectionFlow = MutableStateFlow(emptyList<LocalDate>())

    val diaryEntryFlow = MutableStateFlow(diaryEntryRepository.diaryEntrys)

    val selectedEntryTextFlow = diaryEntryFlow.combine(selectionFlow) { entries, selection ->
        entries.filter { it.first().entryDate in selection }.toString()
    }

    fun onSelectionChanged(selection: List<LocalDate>) {
        selectionFlow.value = selection
    }

    fun onMonthChanged(newMonth: java.time.YearMonth) = viewModelScope.launch {
        diaryEntryFlow.value = diaryEntryRepository.loadMonth(newMonth.atDay(15))
    }
}

sealed interface DiaryEntriesUiState {
    object Loading : DiaryEntriesUiState
    data class Error(val throwable: Throwable) : DiaryEntriesUiState
    data class Success(val data: List<DiaryEntry>) : DiaryEntriesUiState
}