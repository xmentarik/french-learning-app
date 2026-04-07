package french.learning.app.learn.frenchlearningapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import french.learning.app.learn.frenchlearningapp.domain.model.LanguageOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserPreferencesDataStore(private val context: Context) {

    fun nativeLanguageFlow(): Flow<LanguageOption> {
        return context.dataStore.data.map { pref ->
            pref[NATIVE_LANGUAGE_KEY]
                ?.let { runCatching { LanguageOption.valueOf(it) }.getOrNull() }
                ?: LanguageOption.ENGLISH
        }
    }

    suspend fun setNativeLanguage(option: LanguageOption) {
        context.dataStore.edit { pref ->
            pref[NATIVE_LANGUAGE_KEY] = option.name
        }
    }

    private companion object {
        val NATIVE_LANGUAGE_KEY = stringPreferencesKey("native_language")
    }
}
