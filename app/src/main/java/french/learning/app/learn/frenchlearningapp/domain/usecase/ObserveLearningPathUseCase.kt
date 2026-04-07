package french.learning.app.learn.frenchlearningapp.domain.usecase

import french.learning.app.learn.frenchlearningapp.domain.model.LanguageOption
import french.learning.app.learn.frenchlearningapp.domain.model.Lesson
import french.learning.app.learn.frenchlearningapp.domain.model.User
import french.learning.app.learn.frenchlearningapp.domain.repository.LearningRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class LearningPathState(
    val lessons: List<Lesson> = emptyList(),
    val user: User = User(id = "local_user", nativeLanguage = LanguageOption.ENGLISH)
)

class ObserveLearningPathUseCase @Inject constructor(
    private val repository: LearningRepository
) {
    operator fun invoke(): Flow<LearningPathState> {
        return combine(repository.observeLessons(), repository.observeUserProgress()) { lessons, user ->
            LearningPathState(lessons = lessons, user = user)
        }
    }
}
