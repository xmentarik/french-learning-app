package french.learning.app.learn.frenchlearningapp.data.local

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import french.learning.app.learn.frenchlearningapp.domain.model.Dialogue
import french.learning.app.learn.frenchlearningapp.domain.model.Lesson
import french.learning.app.learn.frenchlearningapp.domain.model.Quiz

private val gson = Gson()

fun LessonEntity.toDomain(): Lesson {
    val dialogueType = object : TypeToken<List<Dialogue>>() {}.type
    return Lesson(
        id = id,
        level = level,
        order = order,
        title = title,
        dialogues = gson.fromJson(dialoguesJson, dialogueType),
        quiz = gson.fromJson(quizJson, Quiz::class.java),
        xpReward = xpReward,
        isCompleted = isCompleted
    )
}

fun Lesson.toEntity(): LessonEntity {
    return LessonEntity(
        id = id,
        level = level,
        order = order,
        title = title,
        dialoguesJson = gson.toJson(dialogues),
        quizJson = gson.toJson(quiz),
        xpReward = xpReward,
        isCompleted = isCompleted
    )
}
