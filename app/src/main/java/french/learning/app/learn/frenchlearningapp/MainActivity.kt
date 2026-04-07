package french.learning.app.learn.frenchlearningapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import french.learning.app.learn.frenchlearningapp.presentation.navigation.AppNavHost
import french.learning.app.learn.frenchlearningapp.ui.theme.FrenchLearningAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FrenchLearningAppTheme {
                AppNavHost()
            }
        }
    }
}
