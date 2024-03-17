package de.blumesladen.ui.diaryentry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.blumesladen.data.local.database.DiaryEntry
import de.blumesladen.data.local.di.DiaryEntryRepository
import de.blumesladen.ui.diaryentry.DiaryEntriesUiState.Error
import de.blumesladen.ui.diaryentry.DiaryEntriesUiState.Loading
import de.blumesladen.ui.diaryentry.DiaryEntriesUiState.Success
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class DiaryEntriesViewModel @Inject constructor(
    private val diaryEntryRepository: DiaryEntryRepository
) : ViewModel() {

    val uiState: StateFlow<DiaryEntriesUiState> = diaryEntryRepository
        .getDiaryEntriesMostRecent().map<List<DiaryEntry>, DiaryEntriesUiState>(::Success)
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    private val selectionFlow = MutableStateFlow(listOf(LocalDate.now()))

    var diaryEntryFlow = diaryEntryRepository.getDiaryEntriesMostRecent()


    fun onSelectionChanged(selection: List<LocalDate>) {
        selectionFlow.value = selection
    }

    fun onMonthChanged(newMonth: java.time.YearMonth) = viewModelScope.launch {
        diaryEntryFlow = diaryEntryRepository.getDiaryEntriesByMonth(newMonth.format(DateTimeFormatter.ofPattern("yyyy-MM")))
    }
}

sealed interface DiaryEntriesUiState {
    object Loading : DiaryEntriesUiState
    data class Error(val throwable: Throwable) : DiaryEntriesUiState
    data class Success(val data: List<DiaryEntry>) : DiaryEntriesUiState
}