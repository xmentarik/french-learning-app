package french.learning.app.learn.frenchlearningapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import french.learning.app.learn.frenchlearningapp.presentation.screens.LanguageSelectionScreen
import french.learning.app.learn.frenchlearningapp.presentation.screens.LessonScreen
import french.learning.app.learn.frenchlearningapp.presentation.screens.MainLearningPath
import french.learning.app.learn.frenchlearningapp.presentation.viewmodel.LearningViewModel

object Routes {
    const val LANGUAGE = "language"
    const val PATH = "path"
    const val LESSON = "lesson/{lessonId}"

    fun lesson(lessonId: String) = "lesson/$lessonId"
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val vm: LearningViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = Routes.LANGUAGE) {
        composable(Routes.LANGUAGE) {
            LanguageSelectionScreen(
                onLanguageSelected = {
                    vm.setNativeLanguage(it)
                    navController.navigate(Routes.PATH)
                }
            )
        }

        composable(Routes.PATH) {
            MainLearningPath(
                viewModel = vm,
                onLessonClick = { lessonId -> navController.navigate(Routes.lesson(lessonId)) }
            )
        }

        composable(
            route = Routes.LESSON,
            arguments = listOf(navArgument("lessonId") { type = NavType.StringType })
        ) { backStackEntry ->
            val lessonId = backStackEntry.arguments?.getString("lessonId").orEmpty()
            LessonScreen(
                lessonId = lessonId,
                viewModel = vm,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
