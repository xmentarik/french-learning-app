package french.learning.app.learn.frenchlearningapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import french.learning.app.learn.frenchlearningapp.presentation.viewmodel.LearningViewModel

@Composable
fun MainLearningPath(
    viewModel: LearningViewModel,
    onLessonClick: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val total = state.lessons.size.coerceAtLeast(1)
    val progress = state.completedLessons.toFloat() / total

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1CB0F6))) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("XP: ${state.xp} | Streak: ${state.streakDays} jours", color = Color.White)
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    color = Color(0xFF58CC02)
                )
            }
        }


        BannerAdPlaceholder()

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(state.lessons) { lesson ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLessonClick(lesson.id) }
                ) {
                    Column(
                        modifier = Modifier
                            .background(
                                color = if (lesson.isCompleted) Color(0xFF58CC02) else Color(0xFFFFC800),
                                shape = CircleShape
                            )
                            .padding(20.dp)
                    ) {
                        Text(text = lesson.order.toString(), color = Color.White)
                    }
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text(text = lesson.title, style = MaterialTheme.typography.titleMedium)
                        Text(text = "Niveau ${lesson.level} • ${lesson.xpReward} XP")
                    }
                }
            }
        }
    }
}
