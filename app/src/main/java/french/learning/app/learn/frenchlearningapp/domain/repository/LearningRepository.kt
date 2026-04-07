package french.learning.app.learn.frenchlearningapp.domain.repository

import french.learning.app.learn.frenchlearningapp.domain.model.LanguageOption
import french.learning.app.learn.frenchlearningapp.domain.model.Lesson
import french.learning.app.learn.frenchlearningapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface LearningRepository {
    fun observeLessons(): Flow<List<Lesson>>
    suspend fun refreshLessonsFromRemote()
    suspend fun getLessonById(lessonId: String): Lesson?

    fun observeUserProgress(): Flow<User>
    suspend fun updateUserProgress(completedLessonId: String, xpDelta: Int)

    fun observeNativeLanguage(): Flow<LanguageOption>
    suspend fun setNativeLanguage(languageOption: LanguageOption)
}
