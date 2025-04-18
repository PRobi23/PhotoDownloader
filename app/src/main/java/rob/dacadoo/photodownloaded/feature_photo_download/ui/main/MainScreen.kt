package rob.dacadoo.photodownloaded.feature_photo_download.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import rob.dacadoo.photodownloaded.R
import rob.dacadoo.photodownloaded.core.ui.util.ObserveUiEvents
import rob.dacadoo.photodownloaded.feature_photo_download.ui.main.component.PhotoItem
import rob.dacadoo.photodownloaded.ui.theme.PhotoDownloadedTheme

@Composable
fun MainScreenRoot(
    modifier: Modifier = Modifier,
    navigateToDetailsScreen: (String) -> Unit
) {
    val mainViewModel: MainViewModel = hiltViewModel()
    val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveUiEvents(
        uiEvents = mainViewModel.uiEvent,
        snackbarHostState = snackbarHostState,
        setUiEventState = { uiEvent ->
            mainViewModel.handleIntent(MainViewModelIntent.SetUiEventState(uiEvent))
        },
    )
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { contentPadding ->
        MainScreen(
            modifier = modifier.padding(contentPadding),
            uiState = uiState,
            handleIntent = mainViewModel::handleIntent,
            navigateToDetailsScreen = navigateToDetailsScreen
        )
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    uiState: MainState,
    handleIntent: (MainViewModelIntent) -> Unit,
    navigateToDetailsScreen: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier.height(48.dp))
        val textFieldState = rememberTextFieldState()
        BasicTextField(
            state = textFieldState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.LightGray)
                .padding(8.dp),
            textStyle = TextStyle(color = Color.Black, fontSize = 16.sp)

        )
        Spacer(modifier.height(16.dp))
        Button(
            enabled = textFieldState.text.isNotEmpty(),
            onClick = {
                handleIntent(
                    MainViewModelIntent.OnSearchClick(
                        textFieldState.text.toString()
                    )
                )
            }
        ) {
            Text(text = stringResource(R.string.search_button))
        }
        uiState.photos?.let { photos ->
            if (photos.isEmpty()) {
                Text(text = stringResource(R.string.empty_photos_list))
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(count = photos.size) { index ->
                        PhotoItem(
                            photoUrl = photos[index].photoUrl,
                            navigateToDetailsScreen = navigateToDetailsScreen
                        )
                    }
                }
            }
        }
        if (uiState.isLoading) {
            CircularProgressIndicator()
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    PhotoDownloadedTheme {
        MainScreen(
            uiState = MainState(),
            handleIntent = {},
            navigateToDetailsScreen = {}
        )
    }
}

@Preview
@Composable
private fun MainScreenLoadingPreview() {
    PhotoDownloadedTheme {
        MainScreen(
            uiState = MainState(isLoading = true),
            handleIntent = {},
            navigateToDetailsScreen = {}
        )
    }
}