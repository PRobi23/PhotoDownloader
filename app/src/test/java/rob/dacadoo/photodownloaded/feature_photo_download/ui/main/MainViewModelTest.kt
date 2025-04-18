package rob.dacadoo.photodownloaded.feature_photo_download.ui.main

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import rob.dacadoo.photodownloaded.core.domain.util.DataError
import rob.dacadoo.photodownloaded.core.domain.util.Result
import rob.dacadoo.photodownloaded.core.ui.util.UiEvent
import rob.dacadoo.photodownloaded.feature_photo_download.domain.model.Photo
import rob.dacadoo.photodownloaded.feature_photo_download.domain.usecase.GetPhotosByNameUseCase

class MainViewModelTest {

    private val getPhotosByNameUseCase: GetPhotosByNameUseCase = mockk()

    private fun createViewModel() = MainViewModel(
        getPhotosByNameUseCase
    )

    @Test
    fun `when calling set ui event then the ui event is set`() = runTest {
        val uiEvent = UiEvent.ShowErrorToTheUser(DataError.Network.SERVER_ERROR)

        val viewModel = createViewModel()

        viewModel.uiEvent.test {
            viewModel.handleIntent(MainViewModelIntent.SetUiEventState(uiEvent))
            val item = awaitItem()
            assertThat(item).isEqualTo(uiEvent)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when search function and use case succeeds then the fetched photos are set to the state`() =
        runTest {
            val name = "ios"
            val photos = listOf(Photo("photo1"), Photo("photo2"))
            coEvery {
                getPhotosByNameUseCase(name)
            } returns Result.Success(photos)

            val viewModel = createViewModel()

            viewModel.uiState.test {
                viewModel.handleIntent(MainViewModelIntent.OnSearchClick(name))
                skipItems(1)
                val loadingItem = awaitItem()
                assertThat(loadingItem.isLoading).isTrue()

                val itemWithPhotos = awaitItem()
                assertThat(itemWithPhotos.isLoading).isFalse()
                assertThat(itemWithPhotos.photos).isEqualTo(photos)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `when search function and use case fails then error is shown to the user`() =
        runTest {
            val error = DataError.Network.SERVER_ERROR
            val name = "ios"
            coEvery {
                getPhotosByNameUseCase(name)
            } returns Result.Error(error)

            val viewModel = createViewModel()

            viewModel.uiState.test {
                viewModel.handleIntent(MainViewModelIntent.OnSearchClick(name))
                skipItems(1)
                val loadingItem = awaitItem()
                assertThat(loadingItem.isLoading).isTrue()

                val itemWithPhotos = awaitItem()
                viewModel.uiEvent.test {
                    val item = awaitItem()
                    assertThat(item).isEqualTo(UiEvent.ShowErrorToTheUser(error))

                    cancelAndIgnoreRemainingEvents()
                }
                assertThat(itemWithPhotos.isLoading).isFalse()

                cancelAndIgnoreRemainingEvents()
            }
        }
}