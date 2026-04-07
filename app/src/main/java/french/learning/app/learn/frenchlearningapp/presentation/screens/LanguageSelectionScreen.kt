package french.learning.app.learn.frenchlearningapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import french.learning.app.learn.frenchlearningapp.domain.model.LanguageOption

@Composable
fun LanguageSelectionScreen(onLanguageSelected: (LanguageOption) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Choisissez votre langue native", style = MaterialTheme.typography.headlineSmall)
        LanguageOption.entries.forEach { option ->
            Button(
                onClick = { onLanguageSelected(option) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(option.name.lowercase().replaceFirstChar { it.uppercase() })
            }
        }
    }
}
