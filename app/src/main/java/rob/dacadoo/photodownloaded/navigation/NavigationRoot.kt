package rob.dacadoo.photodownloaded.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import rob.dacadoo.photodownloaded.feature_photo_download.ui.main.MainScreenRoot
import rob.dacadoo.photodownloaded.navigation.NavigationNames.MAIN_SCREEN_NAME
import rob.dacadoo.photodownloaded.navigation.NavigationNames.PHOTO_ROUTE_NAME

@Composable
fun NavigationRoot(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = PHOTO_ROUTE_NAME
    ) {
        photoGraph(navController)
    }
}

private fun NavGraphBuilder.photoGraph(navController: NavHostController) {
    navigation(
        startDestination = MAIN_SCREEN_NAME,
        route = PHOTO_ROUTE_NAME
    ) {
        composable(route = MAIN_SCREEN_NAME) {
            MainScreenRoot()
        }
    }
}