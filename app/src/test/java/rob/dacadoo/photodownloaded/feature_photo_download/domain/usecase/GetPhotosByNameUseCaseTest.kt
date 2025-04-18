package rob.dacadoo.photodownloaded.feature_photo_download.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import rob.dacadoo.photodownloaded.core.domain.util.Result
import rob.dacadoo.photodownloaded.feature_photo_download.domain.PhotoRepository

class GetPhotosByNameUseCaseTest {
    private val photoRepository: PhotoRepository = mockk()
    private fun createUseCase() = GetPhotosByNameUseCase(photoRepository)

    @Test
    fun `when use case is called then the proper function is called in the repository`() = runTest {
        val name = "ios"
        coEvery {
            photoRepository.getPhotosByName(name)
        } returns Result.Success(emptyList())
        val useCase = createUseCase()
        useCase(name)

        coVerify(exactly = 1) {
            photoRepository.getPhotosByName(name)
        }
    }
}