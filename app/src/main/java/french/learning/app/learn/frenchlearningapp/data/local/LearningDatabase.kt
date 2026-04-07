package french.learning.app.learn.frenchlearningapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [LessonEntity::class, UserProgressEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LearningDatabase : RoomDatabase() {
    abstract fun learningDao(): LearningDao
}
