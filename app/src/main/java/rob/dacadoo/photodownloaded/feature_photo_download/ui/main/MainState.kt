@file:OptIn(ExperimentalFoundationApi::class)

package rob.dacadoo.photodownloaded.feature_photo_download.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import rob.dacadoo.photodownloaded.feature_photo_download.domain.model.Photo

data class MainState(
    val photos: List<Photo>? = null,
    val isLoading: Boolean = false
)