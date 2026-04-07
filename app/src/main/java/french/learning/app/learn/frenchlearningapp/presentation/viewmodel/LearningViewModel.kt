package french.learning.app.learn.frenchlearningapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import french.learning.app.learn.frenchlearningapp.domain.model.LanguageOption
import french.learning.app.learn.frenchlearningapp.domain.model.Lesson
import french.learning.app.learn.frenchlearningapp.domain.repository.LearningRepository
import french.learning.app.learn.frenchlearningapp.domain.usecase.CompleteLessonUseCase
import french.learning.app.learn.frenchlearningapp.domain.usecase.ObserveLearningPathUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LearningUiState(
    val isLoading: Boolean = true,
    val lessons: List<Lesson> = emptyList(),
    val selectedLesson: Lesson? = null,
    val xp: Int = 0,
    val streakDays: Int = 0,
    val completedLessons: Int = 0,
    val nativeLanguage: LanguageOption = LanguageOption.ENGLISH,
    val errorMessage: String? = null
)

@HiltViewModel
class LearningViewModel @Inject constructor(
    private val repository: LearningRepository,
    private val observeLearningPathUseCase: ObserveLearningPathUseCase,
    private val completeLessonUseCase: CompleteLessonUseCase
) : ViewModel() {

    private val internalUiState = MutableStateFlow(LearningUiState())
    val uiState: StateFlow<LearningUiState> = internalUiState.asStateFlow()

    val learningPath = observeLearningPathUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        observeState()
        refreshLessons()
    }

    private fun observeState() {
        viewModelScope.launch {
            repository.observeNativeLanguage().collect { language ->
                internalUiState.value = internalUiState.value.copy(nativeLanguage = language)
            }
        }

        viewModelScope.launch {
            observeLearningPathUseCase().collect { state ->
                internalUiState.value = internalUiState.value.copy(
                    isLoading = false,
                    lessons = state.lessons,
                    xp = state.user.xp,
                    streakDays = state.user.streakDays,
                    completedLessons = state.user.completedLessonIds.size
                )
            }
        }
    }

    fun selectLesson(lessonId: String) {
        val lesson = internalUiState.value.lessons.firstOrNull { it.id == lessonId }
        internalUiState.value = internalUiState.value.copy(selectedLesson = lesson)
    }

    fun completeSelectedLesson() {
        val lesson = internalUiState.value.selectedLesson ?: return
        viewModelScope.launch {
            completeLessonUseCase(lesson.id, lesson.xpReward)
        }
    }

    fun setNativeLanguage(option: LanguageOption) {
        viewModelScope.launch {
            repository.setNativeLanguage(option)
        }
    }

    private fun refreshLessons() {
        viewModelScope.launch {
            runCatching { repository.refreshLessonsFromRemote() }
                .onFailure {
                    internalUiState.value = internalUiState.value.copy(
                        isLoading = false,
                        errorMessage = "Impossible de charger les leçons: ${it.message}"
                    )
                }
        }
    }
}
