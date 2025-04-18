package rob.dacadoo.photodownloaded.feature_photo_download.data

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import rob.dacadoo.photodownloaded.core.data.networking.di.ApiKey
import rob.dacadoo.photodownloaded.core.data.networking.get
import rob.dacadoo.photodownloaded.core.di.IoDispatcher
import rob.dacadoo.photodownloaded.core.domain.util.DataError
import rob.dacadoo.photodownloaded.core.domain.util.Result
import rob.dacadoo.photodownloaded.core.domain.util.map
import rob.dacadoo.photodownloaded.feature_photo_download.data.model.PhotoResponse
import rob.dacadoo.photodownloaded.feature_photo_download.data.model.toDomainPhotos
import rob.dacadoo.photodownloaded.feature_photo_download.domain.PhotoRepository
import rob.dacadoo.photodownloaded.feature_photo_download.domain.model.Photo
import javax.inject.Inject

class DefaultPhotoRepository @Inject constructor(
    val httpClient: HttpClient,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApiKey private val apiKey: String
) : PhotoRepository {

    override suspend fun getPhotosByName(name: String): Result<List<Photo>, DataError.Network> {
        return withContext(ioDispatcher) {
            httpClient.get<PhotoResponse>(
                route = "/search/photos",
                queryParameters = mapOf(
                    "query" to name,
                    "client_id" to apiKey
                )
            ).map {
                it.toDomainPhotos()
            }
        }
    }

}