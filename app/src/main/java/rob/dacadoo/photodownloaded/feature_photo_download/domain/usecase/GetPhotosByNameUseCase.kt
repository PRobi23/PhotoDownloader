package rob.dacadoo.photodownloaded.feature_photo_download.domain.usecase

import rob.dacadoo.photodownloaded.feature_photo_download.domain.PhotoRepository
import javax.inject.Inject

class GetPhotosByNameUseCase @Inject constructor(private val photoRepository: PhotoRepository) {
    suspend operator fun invoke(name: String) = photoRepository.getPhotosByName(name)
}