package french.learning.app.learn.frenchlearningapp.presentation.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
            if (status == TextToSpeech.SUCCESS) {
                it.language = Locale.FRENCH
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    if (lesson == null) {
        Text("Leçon introuvable", modifier = Modifier.padding(16.dp))
        return
    }

    viewModel.selectLesson(lessonId)

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(text = lesson.title)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
            items(lesson.dialogues) { dialogue ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(dialogue.frenchText)
                            Text(dialogue.translation)
                        }
                        IconButton(onClick = {
                            tts.speak(dialogue.frenchText, TextToSpeech.QUEUE_FLUSH, null, dialogue.id)
                        }) {
                            Icon(imageVector = Icons.Default.VolumeUp, contentDescription = "Read audio")
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
