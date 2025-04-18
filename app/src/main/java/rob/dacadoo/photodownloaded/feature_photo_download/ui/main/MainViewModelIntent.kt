package rob.dacadoo.photodownloaded.feature_photo_download.ui.main

import rob.dacadoo.photodownloaded.core.ui.util.UiEvent

sealed interface MainViewModelIntent {
    data class OnSearchClick(val name: String) : MainViewModelIntent

    data class SetUiEventState(val uiEvent: UiEvent) : MainViewModelIntent
}