package com.vnhanh.common.compose.image

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage


enum class ImageLoadingState {
    IDLE, LOADING, SUCCESS, FAILURE
}

@Composable
fun ProgressAsyncImage(
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
    data: Any,
    contentScale: ContentScale = ContentScale.Fit,
    contentDescription: String,
    overlayComposable: @Composable (modifier: Modifier, isLoadingProvider: () -> ImageLoadingState) -> Unit,
) {
    var loadState by remember {
        mutableStateOf(ImageLoadingState.IDLE)
    }

    Box(modifier = modifier) {
        AsyncImage(
            modifier = imageModifier
                .align(Alignment.Center),
            model = data,
            contentScale = contentScale,
            contentDescription = contentDescription,
            onLoading = {
                loadState = ImageLoadingState.LOADING
            },
            onSuccess = {
                loadState = ImageLoadingState.SUCCESS
            },
            onError = {
                loadState = ImageLoadingState.FAILURE
            },
        )
        overlayComposable(
            // modifier
            Modifier.align(alignment = Alignment.Center)
        ) {
            loadState
        }
    }
}
