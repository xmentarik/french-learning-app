package french.learning.app.learn.frenchlearningapp.data.repository

import french.learning.app.learn.frenchlearningapp.data.local.LearningDao
import french.learning.app.learn.frenchlearningapp.data.local.UserPreferencesDataStore
import french.learning.app.learn.frenchlearningapp.data.local.UserProgressEntity
import french.learning.app.learn.frenchlearningapp.data.local.toDomain
import french.learning.app.learn.frenchlearningapp.data.local.toEntity
import french.learning.app.learn.frenchlearningapp.data.remote.FirestoreDataSource
import french.learning.app.learn.frenchlearningapp.domain.model.LanguageOption
import french.learning.app.learn.frenchlearningapp.domain.model.Lesson
import french.learning.app.learn.frenchlearningapp.domain.model.User
import french.learning.app.learn.frenchlearningapp.domain.repository.LearningRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LearningRepositoryImpl @Inject constructor(
    private val dao: LearningDao,
    private val firestoreDataSource: FirestoreDataSource,
    private val preferences: UserPreferencesDataStore
) : LearningRepository {

    private val defaultUserId = "local_user"

    override fun observeLessons(): Flow<List<Lesson>> = dao.observeLessons().map { entities ->
        entities.map { it.toDomain() }
    }

    override suspend fun refreshLessonsFromRemote() {
        val remoteLessons = firestoreDataSource.fetchLessons()
        val existingCompletionByLessonId = dao.observeLessons()
            .first()
            .associate { lessonEntity -> lessonEntity.id to lessonEntity.isCompleted }
        val mergedLessons = remoteLessons.map { lesson ->
            lesson.copy(
                isCompleted = existingCompletionByLessonId[lesson.id] ?: lesson.isCompleted
            )
        }
        dao.upsertLessons(mergedLessons.map { it.toEntity() })
    }

    override suspend fun getLessonById(lessonId: String): Lesson? = dao.getLessonById(lessonId)?.toDomain()

    override fun observeUserProgress(): Flow<User> {
        return dao.observeUserProgress(defaultUserId).map { entity ->
            val completed = entity?.completedLessonIdsJson?.split(",")?.filter { it.isNotBlank() }.orEmpty()
            User(
                id = defaultUserId,
                nativeLanguage = LanguageOption.ENGLISH,
                xp = entity?.xp ?: 0,
                streakDays = entity?.streakDays ?: 0,
                completedLessonIds = completed
            )
        }
    }

    override suspend fun updateUserProgress(completedLessonId: String, xpDelta: Int) {
        val existing = dao.observeUserProgress(defaultUserId).first() ?: UserProgressEntity(defaultUserId, 0, 0, "")
        val updatedCompletedLessons = (existing.completedLessonIdsJson.split(",") + completedLessonId)
            .filter { it.isNotBlank() }
            .distinct()
        dao.markLessonAsCompleted(completedLessonId)
        dao.upsertUserProgress(
            existing.copy(
                xp = existing.xp + xpDelta,
                streakDays = existing.streakDays + 1,
                completedLessonIdsJson = updatedCompletedLessons.joinToString(",")
            )
        )
    }

    override fun observeNativeLanguage(): Flow<LanguageOption> = preferences.nativeLanguageFlow()

    override suspend fun setNativeLanguage(languageOption: LanguageOption) {
        preferences.setNativeLanguage(languageOption)
    }
}
