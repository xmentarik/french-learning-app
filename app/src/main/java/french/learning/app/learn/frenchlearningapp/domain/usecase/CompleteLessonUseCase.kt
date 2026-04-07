package french.learning.app.learn.frenchlearningapp.domain.usecase

import french.learning.app.learn.frenchlearningapp.domain.repository.LearningRepository
import javax.inject.Inject

class CompleteLessonUseCase @Inject constructor(
    private val repository: LearningRepository
) {
    suspend operator fun invoke(lessonId: String, xpReward: Int) {
        repository.updateUserProgress(lessonId, xpReward)
    }
}
