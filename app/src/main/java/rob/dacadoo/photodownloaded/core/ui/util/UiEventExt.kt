package rob.dacadoo.photodownloaded.core.ui.util

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Observes a [Flow] of [UiEvent]s and handles them accordingly within the UI layer.
 *
 * This composable is typically used to display transient UI effects such as snackbars, toasts,
 * or navigation triggers. It ensures events are collected only when the UI is in a [Lifecycle.State.STARTED]
 * state, and collects them on the main dispatcher for safe interaction with Compose.
 *
 * @param uiEvents A [Flow] of [UiEvent]s to observe, typically exposed from a [ViewModel].
 * @param snackbarHostState The [SnackbarHostState] used to show snackbars when a [UiEvent.ShowErrorToTheUser] is emitted.
 *
 * @sample
 * ```
 * ObserveUiEvents(
 *     uiEvents = viewModel.uiEvents,
 *     snackbarHostState = scaffoldState.snackbarHostState
 * )
 * ```
 *
 * Example usage includes showing error messages from a ViewModel:
 * - When `UiEvent.ShowErrorToTheUser` is received, a snackbar is shown using the provided [snackbarHostState].
 * - After the snackbar is dismissed or acted upon, the [UiEvent] state is reset via `setUiEventState(UiEvent.Init)`.
 *
 * Note: The function assumes `setUiEventState` and `asStringResource()` are available in the scope.
 */
@Composable
fun ObserveUiEvents(
    uiEvents: Flow<UiEvent>,
    snackbarHostState: SnackbarHostState,
    setUiEventState: (UiEvent) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(uiEvents, lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                uiEvents.collect { event ->
                    when (event) {
                        is UiEvent.ShowErrorToTheUser -> {
                            showSnackbarWithCallback(
                                snackbarHostState = snackbarHostState,
                                message = context.getString(event.error.asStringResource()),
                                onDismissedOrActionPerformed = {
                                    setUiEventState(
                                        UiEvent.Init,
                                    )
                                },
                            )
                        }

                        UiEvent.Init -> Unit
                    }
                }
            }
        }
    }
}

/**
 * Extension funciton to handle show snackbar.
 * @param onDismissedOrActionPerformed is called when the dialog is called with SnackbarResult.ActionPerformed or SnackbarResult.Dismissed
 * @see SnackbarResult.Dismissed
 * @see SnackbarResult.ActionPerformed
 */
suspend fun showSnackbarWithCallback(
    snackbarHostState: SnackbarHostState,
    message: String,
    duration: SnackbarDuration = SnackbarDuration.Short,
    onDismissedOrActionPerformed: () -> Unit,
) {
    val snackbarResult =
        snackbarHostState.showSnackbar(
            message = message,
            duration = duration,
        )
    if (snackbarResult == SnackbarResult.ActionPerformed || snackbarResult == SnackbarResult.Dismissed) {
        onDismissedOrActionPerformed()
    }
}
