package french.learning.app.learn.frenchlearningapp.presentation.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import french.learning.app.learn.frenchlearningapp.presentation.viewmodel.LearningViewModel
import java.util.Locale


@Composable
fun LessonScreen(lessonId: String, viewModel: LearningViewModel, onBack: () -> Unit) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()
    val lesson = state.lessons.firstOrNull { it.id == lessonId }

    val tts = remember {
        TextToSpeech(context) { status ->
            // Initialisation TTS
        }
    }

    // Configuration de la langue après initialisation
    LaunchedEffect(tts) {
        tts.language = Locale.FRENCH
    }

    DisposableEffect(Unit) {
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    if (lesson == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            Text("Leçon introuvable")
        }
        return
    }

    // Appel au ViewModel
    LaunchedEffect(lessonId) {
        viewModel.selectLesson(lessonId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = lesson.title, style = MaterialTheme.typography.headlineMedium)

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f) // Fonctionne car enfant direct de Column
        ) {
            items(lesson.dialogues) { dialogue ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f) // Fonctionne car enfant direct de Row
                        ) {
                            Text(text = dialogue.frenchText, style = MaterialTheme.typography.bodyLarge)
                            Text(text = dialogue.translation, style = MaterialTheme.typography.bodyMedium, color = androidx.compose.ui.graphics.Color.Gray)
                        }

                        IconButton(onClick = {
                            tts.speak(dialogue.frenchText, TextToSpeech.QUEUE_FLUSH, null, dialogue.id)
                        }) {
                            // Si VolumeUp ne fonctionne pas, utilisez PlayArrow
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Lire l'audio")
                        }
                    }
                }
            }
        }

        InterstitialAdPlaceholder()
        QuizComponent(quiz = lesson.quiz, onQuizComplete = {
            viewModel.completeSelectedLesson()
            onBack()
        })
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Retour") }
    }
}
