package french.learning.app.learn.frenchlearningapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lessons")
data class LessonEntity(
    @PrimaryKey val id: String,
    val level: String,
    val order: Int,
    val title: String,
    val dialoguesJson: String,
    val quizJson: String,
    val xpReward: Int,
    val isCompleted: Boolean
)

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey val userId: String,
    val xp: Int,
    val streakDays: Int,
    val completedLessonIdsJson: String
)
