package rob.dacadoo.photodownloaded.feature_photo_download.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rob.dacadoo.photodownloaded.core.domain.util.fold
import rob.dacadoo.photodownloaded.core.ui.util.UiEvent
import rob.dacadoo.photodownloaded.feature_photo_download.domain.usecase.GetPhotosByNameUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPhotosByNameUseCase: GetPhotosByNameUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<MainState> =
        MutableStateFlow(MainState())
    val uiState: StateFlow<MainState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>(capacity = Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    fun handleIntent(intent: MainViewModelIntent) {
        when (intent) {
            is MainViewModelIntent.OnSearchClick -> {
                searchForPhotosByName(intent.name)
            }

            is MainViewModelIntent.SetUiEventState -> setUiEventState(intent.uiEvent)
        }
    }

    private fun searchForPhotosByName(name: String) {
        _uiState.update { state ->
            state.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            getPhotosByNameUseCase(name).fold(
                onFailure = { error ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false
                        )
                    }
                    _uiEvent.send(UiEvent.ShowErrorToTheUser(error))
                },
                onSuccess = { photos ->
                    _uiState.update { state ->
                        state.copy(
                            photos = photos,
                            isLoading = false
                        )
                    }
                }
            )
        }
    }

    private fun setUiEventState(uiEvent: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }
}