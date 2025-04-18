package rob.dacadoo.photodownloaded.feature_photo_download.ui.main.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.util.DebugLogger
import rob.dacadoo.photodownloaded.R

@Composable
fun PhotoItem(
    modifier: Modifier = Modifier,
    photoUrl: String,
    navigateToDetailsScreen: (String) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .border(width = 2.dp, Color.Blue),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val context = LocalContext.current
            val imageLoader = ImageLoader.Builder(context)
                .logger(DebugLogger())
                .build()

            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(photoUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_loading),
                imageLoader = imageLoader,
                error = painterResource(R.drawable.ic_error),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(150.dp)
                    .height(250.dp)
                    .clickable {
                        navigateToDetailsScreen(photoUrl)
                    },
            )
        }
    }
}