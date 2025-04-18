package rob.dacadoo.photodownloaded.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object MainScreen : Route


    @Serializable
    data class DetailsScreen(
        val photoUrl: String,
    ) : Route
}
