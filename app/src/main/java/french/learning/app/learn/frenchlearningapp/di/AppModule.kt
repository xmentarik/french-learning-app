package french.learning.app.learn.frenchlearningapp.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import french.learning.app.learn.frenchlearningapp.data.local.LearningDao
import french.learning.app.learn.frenchlearningapp.data.local.LearningDatabase
import french.learning.app.learn.frenchlearningapp.data.local.UserPreferencesDataStore
import french.learning.app.learn.frenchlearningapp.data.remote.FirestoreDataSource
import french.learning.app.learn.frenchlearningapp.data.repository.LearningRepositoryImpl
import french.learning.app.learn.frenchlearningapp.domain.repository.LearningRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideLearningDatabase(@ApplicationContext context: Context): LearningDatabase {
        return Room.databaseBuilder(
            context,
            LearningDatabase::class.java,
            "french_learning.db"
        ).build()
    }

    @Provides
    fun provideLearningDao(database: LearningDatabase): LearningDao = database.learningDao()

    @Provides
    @Singleton
    fun providePreferences(@ApplicationContext context: Context): UserPreferencesDataStore {
        return UserPreferencesDataStore(context)
    }

    @Provides
    @Singleton
    fun provideRepository(
        dao: LearningDao,
        remote: FirestoreDataSource,
        preferences: UserPreferencesDataStore
    ): LearningRepository {
        return LearningRepositoryImpl(dao, remote, preferences)
    }
}
