package rob.dacadoo.photodownloaded.core.ui.util

import rob.dacadoo.photodownloaded.core.domain.util.DataError

sealed interface UiEvent {
    data object Init : UiEvent

    data class ShowErrorToTheUser(
        val error: DataError,
    ) : UiEvent
}