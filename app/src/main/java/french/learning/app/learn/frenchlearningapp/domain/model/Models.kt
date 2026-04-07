package french.learning.app.learn.frenchlearningapp.domain.model

enum class LanguageOption { ENGLISH, ARABIC, SPANISH }

data class User(
    val id: String,
    val nativeLanguage: LanguageOption,
    val xp: Int = 0,
    val streakDays: Int = 0,
    val completedLessonIds: List<String> = emptyList()
)

data class Dialogue(
    val id: String,
    val frenchText: String,
    val translation: String
)

enum class QuizType { MULTIPLE_CHOICE, REORDER_SENTENCE }

data class QuizQuestion(
    val id: String,
    val type: QuizType,
    val prompt: String,
    val options: List<String>,
    val correctAnswer: List<String>
)

data class Quiz(
    val lessonId: String,
    val questions: List<QuizQuestion>
)

data class Lesson(
    val id: String,
    val level: String,
    val order: Int,
    val title: String,
    val dialogues: List<Dialogue>,
    val quiz: Quiz,
    val xpReward: Int,
    val isCompleted: Boolean = false
)
