package rob.dacadoo.photodownloaded.feature_photo_download.data

import io.ktor.client.HttpClient
import rob.dacadoo.photodownloaded.core.data.networking.get
import rob.dacadoo.photodownloaded.core.domain.util.DataError
import rob.dacadoo.photodownloaded.core.domain.util.Result
import rob.dacadoo.photodownloaded.feature_photo_download.data.model.PhotoResponse
import rob.dacadoo.photodownloaded.feature_photo_download.di.IoDispatcher
import rob.dacadoo.photodownloaded.feature_photo_download.domain.PhotoRepository
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DefaultPhotoRepository @Inject constructor(
    val httpClient: HttpClient,
    @IoDispatcher private val ioDispatcher: CoroutineContext
) : PhotoRepository {

    override suspend fun getPhotosByName(name: String): Result<PhotoResponse, DataError.Network> {
        return httpClient.get<PhotoResponse>(
            route = "/search/photos",
            queryParameters = mapOf(
                "query" to name,
                "client_id" to "api-key"
            )
        )
    }

}