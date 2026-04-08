package french.learning.app.learn.frenchlearningapp.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import french.learning.app.learn.frenchlearningapp.domain.model.Dialogue
import french.learning.app.learn.frenchlearningapp.domain.model.Lesson
import french.learning.app.learn.frenchlearningapp.domain.model.Quiz
import french.learning.app.learn.frenchlearningapp.domain.model.QuizQuestion
import french.learning.app.learn.frenchlearningapp.domain.model.QuizType
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore?
) {

    suspend fun fetchLessons(): List<Lesson> {
        val activeFirestore = firestore ?: return generateSeedLessons()
        return runCatching {
            val snapshot = activeFirestore.collection("lessons").get().await()
            if (snapshot.isEmpty) {
                generateSeedLessons()
            } else {
                snapshot.documents.mapNotNull { doc ->
                    val lessonId = doc.id
                    val level = doc.getString("level") ?: "A1"
                    val order = (doc.getLong("order") ?: 1L).toInt()
                    val title = doc.getString("title") ?: "Lesson $order"
                    Lesson(
                        id = lessonId,
                        level = level,
                        order = order,
                        title = title,
                        dialogues = buildDialogues(order),
                        quiz = buildQuiz(lessonId, order),
                        xpReward = (doc.getLong("xpReward") ?: 10L).toInt(),
                        isCompleted = false
                    )
                }
            }
        }.getOrElse {
            generateSeedLessons()
        }
    }

    private fun generateSeedLessons(): List<Lesson> {
        return (1..200).map { index ->
            val level = when (index) {
                in 1..50 -> "A1"
                in 51..100 -> "A2"
                in 101..140 -> "B1"
                else -> "B2"
            }
            val lessonId = "lesson_$index"
            Lesson(
                id = lessonId,
                level = level,
                order = index,
                title = "Leçon $index",
                dialogues = buildDialogues(index),
                quiz = buildQuiz(lessonId, index),
                xpReward = 15
            )
        }
    }

    private fun buildDialogues(index: Int): List<Dialogue> {
        return (1..5).map { turn ->
            Dialogue(
                id = "d_${index}_$turn",
                frenchText = "Bonjour, ceci est la phrase $turn de la leçon $index.",
                translation = "Hello, this is sentence $turn from lesson $index."
            )
        }
    }

    private fun buildQuiz(lessonId: String, index: Int): Quiz {
        return Quiz(
            lessonId = lessonId,
            questions = listOf(
                QuizQuestion(
                    id = "q_${index}_1",
                    type = QuizType.MULTIPLE_CHOICE,
                    prompt = "Choisissez la bonne traduction de 'Bonjour'.",
                    options = listOf("Hello", "Good night", "Thank you", "Goodbye"),
                    correctAnswer = listOf("Hello")
                ),
                QuizQuestion(
                    id = "q_${index}_2",
                    type = QuizType.REORDER_SENTENCE,
                    prompt = "Remettez la phrase dans l'ordre.",
                    options = listOf("m'appelle", "je", "Luc"),
                    correctAnswer = listOf("je", "m'appelle", "Luc")
                )
            )
        )
    }
}
