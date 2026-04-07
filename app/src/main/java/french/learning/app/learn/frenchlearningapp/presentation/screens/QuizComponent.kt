package french.learning.app.learn.frenchlearningapp.presentation.screens

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import french.learning.app.learn.frenchlearningapp.domain.model.Quiz
import french.learning.app.learn.frenchlearningapp.domain.model.QuizType

@Composable
fun QuizComponent(
    quiz: Quiz,
    onQuizComplete: () -> Unit
) {
    val context = LocalContext.current
    var index by remember { mutableIntStateOf(0) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    val reorderAnswer = remember { mutableStateListOf<String>() }

    val question = quiz.questions[index]

    fun vibrate() {
        val vibrator = context.getSystemService(Vibrator::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(80, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(80)
        }
    }

    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp)) {
        AnimatedContent(targetState = question.id, label = "quiz_animation") {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(question.prompt)

                when (question.type) {
                    QuizType.MULTIPLE_CHOICE -> {
                        question.options.forEach { option ->
                            val bg = when (isCorrect) {
                                true -> Color(0xFF58CC02)
                                false -> Color(0xFFE74C3C)
                                null -> Color(0xFF1CB0F6)
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(bg, RoundedCornerShape(24.dp))
                                    .clickable {
                                        val answerOk = option == question.correctAnswer.first()
                                        isCorrect = answerOk
                                        if (!answerOk) vibrate()
                                    }
                                    .padding(12.dp)
                            ) {
                                Text(option, color = Color.White)
                            }
                        }
                    }

                    QuizType.REORDER_SENTENCE -> {
                        question.options.shuffled().forEach { token ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFFFC800), RoundedCornerShape(24.dp))
                                    .clickable {
                                        reorderAnswer.add(token)
                                        isCorrect = reorderAnswer == question.correctAnswer
                                        if (reorderAnswer.size == question.correctAnswer.size && isCorrect == false) {
                                            vibrate()
                                        }
                                    }
                                    .padding(12.dp)
                            ) {
                                Text(token)
                            }
                        }
                        Text("Réponse: ${reorderAnswer.joinToString(" ")}")
                    }
                }

                Button(onClick = {
                    if (index < quiz.questions.lastIndex) {
                        index += 1
                        isCorrect = null
                        reorderAnswer.clear()
                    } else {
                        onQuizComplete()
                    }
                }) {
                    Text(if (index < quiz.questions.lastIndex) "Suivant" else "Terminer")
                }
            }
        }
    }
}
