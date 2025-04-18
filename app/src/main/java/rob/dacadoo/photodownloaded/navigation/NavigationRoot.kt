package rob.dacadoo.photodownloaded.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import rob.dacadoo.photodownloaded.feature_photo_download.ui.detail.PhotoDetailScreen
import rob.dacadoo.photodownloaded.feature_photo_download.ui.main.MainScreenRoot

@Composable
fun NavigationRoot(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Route.MainScreen
    ) {
        composable<Route.MainScreen> {
            MainScreenRoot(
                navigateToDetailsScreen = { photoUrl ->
                    navController.navigate(Route.DetailsScreen(photoUrl))
                }
            )
        }
        composable<Route.DetailsScreen> {
            val args = it.toRoute<Route.DetailsScreen>()

            PhotoDetailScreen(
                photoUrl = args.photoUrl
            )
        }
    }
}
