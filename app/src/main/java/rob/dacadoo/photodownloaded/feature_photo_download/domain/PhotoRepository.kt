package rob.dacadoo.photodownloaded.feature_photo_download.domain

import rob.dacadoo.photodownloaded.core.domain.util.DataError
import rob.dacadoo.photodownloaded.core.domain.util.Result
import rob.dacadoo.photodownloaded.feature_photo_download.data.model.PhotoResponse

/**
 * Repository interface responsible for fetching photos.
 */
interface PhotoRepository {

    /**
     * Retrieves a list of photos matching the given name.
     *
     * @param name The name or keyword used to search for photos.
     * @return A [Result] containing either the data or an error.
     * @see PhotoResponse if the api call succeeds
     * @see DataError.Network if the api call fails
     */
    suspend fun getPhotosByName(name: String): Result<PhotoResponse, DataError.Network>
}